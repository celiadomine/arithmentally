package ch.celia.domine.arithmentally.game;

import ch.celia.domine.arithmentally.game.dto.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


class GameServiceWhiteBoxTest {

  GameSessionRepository sessionRepo;
  GameQuestionRepository questionRepo;
  GameService service;

  private final AtomicLong ids = new AtomicLong(1000);

  @BeforeEach
  void setup() {
    sessionRepo  = mock(GameSessionRepository.class);
    questionRepo = mock(GameQuestionRepository.class);
    service = new GameService(sessionRepo, questionRepo);

    // save(session) vergibt testweise eine ID (JPA-Effekt simulieren)
    when(sessionRepo.save(any(GameSession.class))).thenAnswer(inv -> {
      GameSession s = inv.getArgument(0);
      if (s.getId() == null) {
        ReflectionTestUtils.setField(s, "id", ids.getAndIncrement());
      }
      return s;
    });
    // save(question) passthrough
    when(questionRepo.save(any(GameQuestion.class))).thenAnswer(inv -> inv.getArgument(0));
  }

  // ---------- start(): Operator-Zweige & Invarianten ----------

  @Test
  @DisplayName("start(DIV): Ganzzahldivision; b>=1; correctAnswer=a/b")
  void start_div_allInvariants() {
    StartGameRequest req = new StartGameRequest();
    req.playerName = "Alice";
    req.numQuestions = 7;
    req.min = 1; req.max = 9;
    req.ops = "DIV";

    StartGameResponse resp = service.start(req);
    assertNotNull(resp.gameId);

    var cap = ArgumentCaptor.forClass(GameQuestion.class);
    verify(questionRepo, times(7)).save(cap.capture());
    int i = 0;
    for (GameQuestion q : cap.getAllValues()) {
      assertEquals(GameQuestion.Op.DIV, q.getOp());
      assertEquals(i++, q.getIdx());
      assertTrue(q.getB() >= 1, "Divisor b >= 1");
      assertEquals(0, q.getA() % q.getB(), "a ist Vielfaches von b");
      assertEquals(Integer.valueOf(q.getA() / q.getB()), q.getCorrectAnswer());
    }
  }

  @Test
  @DisplayName("start(SUB): a>=b wird erzwungen; correctAnswer=a-b")
  void start_sub_swapsWhenNeeded() {
    StartGameRequest req = new StartGameRequest();
    req.playerName = "Bob";
    req.numQuestions = 6;
    req.min = 0; req.max = 9;
    req.ops = "SUB";

    service.start(req);

    var cap = ArgumentCaptor.forClass(GameQuestion.class);
    verify(questionRepo, times(6)).save(cap.capture());
    for (GameQuestion q : cap.getAllValues()) {
      assertEquals(GameQuestion.Op.SUB, q.getOp());
      assertTrue(q.getA() >= q.getB());
      assertEquals(Integer.valueOf(q.getA() - q.getB()), q.getCorrectAnswer());
    }
  }

  @Test
  @DisplayName("start(ADD): correctAnswer=a+b; Swap von min/max; Default-Name")
  void start_add_correctAndBoundsSwapAndDefaultName() {
    StartGameRequest req = new StartGameRequest();
    req.playerName = "  "; // blank -> Default
    req.numQuestions = 4;
    req.min = 20; req.max = 10; // wird getauscht
    req.ops = "ADD";

    service.start(req);

    // Session-Defaults geprüft
    var sCap = ArgumentCaptor.forClass(GameSession.class);
    verify(sessionRepo, atLeastOnce()).save(sCap.capture());
    var s = sCap.getValue();
    assertEquals("Player", s.getPlayerName());
    assertEquals(4, s.getTotalQuestions());

    // Fragen geprüft
    var qCap = ArgumentCaptor.forClass(GameQuestion.class);
    verify(questionRepo, times(4)).save(qCap.capture());
    for (GameQuestion q : qCap.getAllValues()) {
      assertEquals(GameQuestion.Op.ADD, q.getOp());
      assertTrue(q.getA() >= 10 && q.getA() <= 20);
      assertTrue(q.getB() >= 10 && q.getB() <= 20);
      assertEquals(Integer.valueOf(q.getA() + q.getB()), q.getCorrectAnswer());
    }
  }

  @Test
  @DisplayName("start(MUL): correctAnswer=a*b")
  void start_mul_product() {
    StartGameRequest req = new StartGameRequest();
    req.playerName = "Eve";
    req.numQuestions = 5;
    req.min = -3; req.max = 3;
    req.ops = "MUL";

    service.start(req);

    var cap = ArgumentCaptor.forClass(GameQuestion.class);
    verify(questionRepo, times(5)).save(cap.capture());
    for (GameQuestion q : cap.getAllValues()) {
      assertEquals(GameQuestion.Op.MUL, q.getOp());
      assertEquals(Integer.valueOf(q.getA() * q.getB()), q.getCorrectAnswer());
    }
  }

  @Test
  @DisplayName("start(mixed ADD,SUB): nur erlaubte Ops; Invarianten je Op")
  void start_mixedOps_allowedOnly_andInvariants() {
    StartGameRequest req = new StartGameRequest();
    req.playerName = "Mix";
    req.numQuestions = 12;
    req.min = 0; req.max = 5;
    req.ops = " add , Sub "; // case-insensitive, trimming

    service.start(req);

    var cap = ArgumentCaptor.forClass(GameQuestion.class);
    verify(questionRepo, times(12)).save(cap.capture());
    Set<GameQuestion.Op> allowed = Set.of(GameQuestion.Op.ADD, GameQuestion.Op.SUB);
    for (GameQuestion q : cap.getAllValues()) {
      assertTrue(allowed.contains(q.getOp()), "nur ADD/SUB erlaubt");
      if (q.getOp() == GameQuestion.Op.ADD) {
        assertEquals(Integer.valueOf(q.getA() + q.getB()), q.getCorrectAnswer());
      } else {
        assertTrue(q.getA() >= q.getB());
        assertEquals(Integer.valueOf(q.getA() - q.getB()), q.getCorrectAnswer());
      }
    }
  }

  // ---------- start(): parseOps & Clamping & Defaults ----------

  @Test
  @DisplayName("start(null): Defaults (total=10, min=1, max=9, ops=ADD)")
  void start_nullRequest_defaults() {
    StartGameResponse resp = service.start(null);
    assertNotNull(resp.gameId);

    var cap = ArgumentCaptor.forClass(GameQuestion.class);
    verify(questionRepo, times(10)).save(cap.capture());
    for (GameQuestion q : cap.getAllValues()) {
      assertEquals(GameQuestion.Op.ADD, q.getOp());
      assertTrue(q.getA() >= 1 && q.getA() <= 9);
      assertTrue(q.getB() >= 1 && q.getB() <= 9);
    }
  }

  @Test
  @DisplayName("start(ops nur ungültig): Fallback auf ADD")
  void start_opsInvalidOnly_fallbackAdd() {
    StartGameRequest req = new StartGameRequest();
    req.playerName = "Foo";
    req.numQuestions = 7;
    req.min = 1; req.max = 3;
    req.ops = "LOL,??"; // nur ungültig -> parseOps liefert leer -> Fallback ADD

    service.start(req);

    var cap = ArgumentCaptor.forClass(GameQuestion.class);
    verify(questionRepo, times(7)).save(cap.capture());
    for (GameQuestion q : cap.getAllValues()) {
      assertEquals(GameQuestion.Op.ADD, q.getOp());
    }
  }

  @Test
  @DisplayName("start(numQuestions clamp): 0->1, 1000->100")
  void start_numQuestions_clamped() {
    // Untergrenze
    StartGameRequest low = new StartGameRequest();
    low.playerName = "L";
    low.numQuestions = 0; // clamp -> 1
    low.min = 1; low.max = 1; low.ops = "ADD";
    service.start(low);
    verify(questionRepo, times(1)).save(any(GameQuestion.class));

    // Obergrenze
    reset(questionRepo);
    when(questionRepo.save(any(GameQuestion.class))).thenAnswer(inv -> inv.getArgument(0));
    StartGameRequest high = new StartGameRequest();
    high.playerName = "H";
    high.numQuestions = 1000; // clamp -> 100
    high.min = 1; high.max = 1; high.ops = "ADD";
    service.start(high);
    verify(questionRepo, times(100)).save(any(GameQuestion.class));
  }

  @Test
  @DisplayName("start(range clamp): min/max außerhalb -> geklemmt [-99..999]")
  void start_rangeClamping() {
    StartGameRequest req = new StartGameRequest();
    req.playerName = "Clamp";
    req.numQuestions = 8;
    req.min = -9999; // -> -99
    req.max = 99999; // -> 999
    req.ops = "ADD";

    service.start(req);

    var cap = ArgumentCaptor.forClass(GameQuestion.class);
    verify(questionRepo, times(8)).save(cap.capture());
    for (GameQuestion q : cap.getAllValues()) {
      assertTrue(q.getA() >= -99 && q.getA() <= 999);
      assertTrue(q.getB() >= -99 && q.getB() <= 999);
    }
  }

  // ---------- current(): beide Pfade + not found + Symbols ----------

  @Test
  @DisplayName("current(): finished=true bei index>=total (kein Question-Repo Call)")
  void current_finishedEarly() {
    GameSession s = new GameSession();
    ReflectionTestUtils.setField(s, "id", 200L);
    s.setTotalQuestions(3);
    s.setCurrentIndex(3);
    when(sessionRepo.findById(200L)).thenReturn(Optional.of(s));

    QuestionResponse dto = service.current(200L);
    assertTrue(dto.finished);
    assertEquals(3, dto.index);
    assertEquals(3, dto.total);
    verify(questionRepo, never()).findBySessionIdAndIdx(anyLong(), anyInt());
  }

  @ParameterizedTest(name = "current(): op={0} -> symbol='{1}'")
  @CsvSource({
      "ADD,+",
      "SUB,-",
      "MUL,×",
      "DIV,÷"
  })
  void current_notFinished_loadsQuestion_andSymbol(String opName, String symbol) {
    GameSession s = new GameSession();
    ReflectionTestUtils.setField(s, "id", 210L);
    s.setTotalQuestions(2);
    s.setCurrentIndex(1);
    when(sessionRepo.findById(210L)).thenReturn(Optional.of(s));

    GameQuestion q = new GameQuestion();
    q.setSession(s); q.setIdx(1);
    q.setA(4); q.setB(2);
    q.setOp(GameQuestion.Op.valueOf(opName));
    q.setCorrectAnswer(42);
    when(questionRepo.findBySessionIdAndIdx(210L, 1)).thenReturn(q);

    QuestionResponse dto = service.current(210L);
    assertFalse(dto.finished);
    assertEquals(1, dto.index);
    assertEquals(2, dto.total);
    assertEquals(4, dto.a);
    assertEquals(2, dto.b);
    assertEquals(symbol, dto.op);
  }

  @Test
  @DisplayName("current(): Session not found -> Exception")
  void current_notFound_throws() {
    when(sessionRepo.findById(999L)).thenReturn(Optional.empty());
    assertThrows(NoSuchElementException.class, () -> service.current(999L));
  }

  // ---------- answer(): alle Pfade ----------

  @Test
  @DisplayName("answer(): bereits fertig -> Early return; Question-Repo unberührt")
  void answer_alreadyFinished_earlyReturn() {
    GameSession s = new GameSession();
    ReflectionTestUtils.setField(s, "id", 300L);
    s.setTotalQuestions(3);
    s.setCurrentIndex(3);
    s.setScore(2);
    when(sessionRepo.findById(300L)).thenReturn(Optional.of(s));

    var out = service.answer(300L, new AnswerRequest());
    assertEquals(2, out.score);
    assertEquals(3, out.total);
    assertTrue(out.finished);

    verify(questionRepo, never()).findBySessionIdAndIdx(anyLong(), anyInt());
    verify(questionRepo, never()).save(any());
    verify(sessionRepo, never()).save(any());
  }

  @Test
  @DisplayName("answer(): korrekt -> Score++ & Index++; finished bei letzter Frage")
  void answer_correct_incrementsAndFinishes() {
    GameSession s = new GameSession();
    ReflectionTestUtils.setField(s, "id", 310L);
    s.setTotalQuestions(1);
    s.setCurrentIndex(0);
    s.setScore(0);
    when(sessionRepo.findById(310L)).thenReturn(Optional.of(s));

    GameQuestion q = new GameQuestion();
    q.setSession(s); q.setIdx(0);
    q.setA(5); q.setB(2); q.setOp(GameQuestion.Op.ADD);
    q.setCorrectAnswer(7);
    when(questionRepo.findBySessionIdAndIdx(310L, 0)).thenReturn(q);
    when(sessionRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
    when(questionRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

    AnswerRequest body = new AnswerRequest();
    body.answer = 7;

    ScoreResponse out = service.answer(310L, body);
    assertEquals(1, out.score);
    assertEquals(1, out.total);
    assertTrue(out.finished);

    assertEquals(1, s.getScore());
    assertEquals(1, s.getCurrentIndex());
    assertEquals(Boolean.TRUE, q.getIsCorrect());
    assertEquals(Integer.valueOf(7), q.getUserAnswer());
  }

  @Test
  @DisplayName("answer(): falsch -> Score unverändert; finished=false (nicht letzte Frage)")
  void answer_wrong_keepsScore() {
    GameSession s = new GameSession();
    ReflectionTestUtils.setField(s, "id", 320L);
    s.setTotalQuestions(2);
    s.setCurrentIndex(0);
    s.setScore(5);
    when(sessionRepo.findById(320L)).thenReturn(Optional.of(s));

    GameQuestion q = new GameQuestion();
    q.setSession(s); q.setIdx(0);
    q.setA(1); q.setB(1); q.setOp(GameQuestion.Op.ADD);
    q.setCorrectAnswer(2);
    when(questionRepo.findBySessionIdAndIdx(320L, 0)).thenReturn(q);
    when(sessionRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
    when(questionRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

    AnswerRequest body = new AnswerRequest();
    body.answer = 999; // falsch

    ScoreResponse out = service.answer(320L, body);
    assertEquals(5, out.score);
    assertEquals(2, out.total);
    assertFalse(out.finished);

    assertEquals(1, s.getCurrentIndex());
    assertEquals(Boolean.FALSE, q.getIsCorrect());
    assertEquals(Integer.valueOf(999), q.getUserAnswer());
  }

  @Test
  @DisplayName("answer(): body==null -> userAnswer=null; isCorrect=false")
  void answer_nullBody_setsNullUser_andFalseCorrect() {
    GameSession s = new GameSession();
    ReflectionTestUtils.setField(s, "id", 330L);
    s.setTotalQuestions(2);
    s.setCurrentIndex(0);
    s.setScore(0);
    when(sessionRepo.findById(330L)).thenReturn(Optional.of(s));

    GameQuestion q = new GameQuestion();
    q.setSession(s); q.setIdx(0);
    q.setA(3); q.setB(4); q.setOp(GameQuestion.Op.ADD);
    q.setCorrectAnswer(7);
    when(questionRepo.findBySessionIdAndIdx(330L, 0)).thenReturn(q);
    when(sessionRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
    when(questionRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

    ScoreResponse out = service.answer(330L, null);

    assertEquals(0, out.score);
    assertEquals(2, out.total);
    assertFalse(out.finished);
    assertEquals(1, s.getCurrentIndex());

    assertNull(q.getUserAnswer());
    assertEquals(Boolean.FALSE, q.getIsCorrect());
  }

  @Test
  @DisplayName("answer(): Session not found -> Exception")
  void answer_sessionNotFound_throws() {
    when(sessionRepo.findById(404L)).thenReturn(Optional.empty());
    assertThrows(NoSuchElementException.class, () -> service.answer(404L, new AnswerRequest()));
  }

  // ---------- history(): Mapping + not found ----------

  @Test
  @DisplayName("history(): mappt Felder & Reihenfolge der Fragen")
  void history_mapsAllFields() {
    GameSession s = new GameSession();
    ReflectionTestUtils.setField(s, "id", 777L);
    s.setPlayerName("P");
    s.setScore(3);
    s.setTotalQuestions(5);
    when(sessionRepo.findById(777L)).thenReturn(Optional.of(s));

    GameQuestion q0 = new GameQuestion();
    q0.setA(1); q0.setB(2); q0.setCorrectAnswer(3); q0.setUserAnswer(3); q0.setIsCorrect(true);

    GameQuestion q1 = new GameQuestion();
    q1.setA(4); q1.setB(5); q1.setCorrectAnswer(9); q1.setUserAnswer(7); q1.setIsCorrect(false);

    when(questionRepo.findBySessionIdOrderByIdxAsc(777L)).thenReturn(List.of(q0, q1));

    HistoryResponse resp = service.history(777L);

    assertEquals(777L, resp.gameId);
    assertEquals("P", resp.playerName);
    assertEquals(3, resp.score);
    assertEquals(5, resp.total);
    assertEquals(2, resp.questions.size());

    // Reihenfolge und Werte prüfen
    HistoryItem h0 = resp.questions.get(0);
    assertEquals(1, h0.a); assertEquals(2, h0.b);
    assertEquals(3, h0.correctAnswer); assertEquals(3, h0.userAnswer);
    assertTrue(h0.isCorrect);

    HistoryItem h1 = resp.questions.get(1);
    assertEquals(4, h1.a); assertEquals(5, h1.b);
    assertEquals(9, h1.correctAnswer); assertEquals(7, h1.userAnswer);
    assertFalse(h1.isCorrect);
  }

  @Test
  @DisplayName("history(): Session not found -> Exception")
  void history_notFound_throws() {
    when(sessionRepo.findById(888L)).thenReturn(Optional.empty());
    assertThrows(NoSuchElementException.class, () -> service.history(888L));
  }
}
