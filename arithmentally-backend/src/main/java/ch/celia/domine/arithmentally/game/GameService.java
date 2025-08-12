package ch.celia.domine.arithmentally.game;

import ch.celia.domine.arithmentally.game.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class GameService {
    private final GameSessionRepository sessionRepo;
    private final GameQuestionRepository questionRepo;
    private final Random rnd = new Random();

    public GameService(GameSessionRepository sessionRepo, GameQuestionRepository questionRepo) {
        this.sessionRepo = sessionRepo;
        this.questionRepo = questionRepo;
    }

    @Transactional
    public StartGameResponse start(StartGameRequest req) {
        int total = req.numQuestions == null ? 10 : Math.max(1, req.numQuestions);
        int min = req.min == null ? 1 : req.min;
        int max = req.max == null ? 9 : Math.max(min, req.max);

        GameSession s = new GameSession();
        s.setPlayerName(req.playerName == null ? "Player" : req.playerName);
        s.setTotalQuestions(total);
        sessionRepo.save(s);

        for (int i = 0; i < total; i++) {
            int a = rng(min, max); int b = rng(min, max);
            GameQuestion q = new GameQuestion();
            q.setSession(s);
            q.setIdx(i);
            q.setA(a); q.setB(b);
            q.setCorrectAnswer(a + b);
            questionRepo.save(q);
        }
        return new StartGameResponse(s.getId());
    }

    @Transactional(readOnly = true)
    public QuestionResponse currentQuestion(Long gameId) {
        GameSession s = sessionRepo.findById(gameId).orElseThrow();
        QuestionResponse dto = new QuestionResponse();
        dto.total = s.getTotalQuestions();
        dto.finished = s.isFinished();
        if (!s.isFinished()) {
            GameQuestion q = questionRepo.findBySessionIdAndIdx(gameId, s.getCurrentIndex());
            dto.index = s.getCurrentIndex();
            dto.a = q.getA(); dto.b = q.getB();
        }
        return dto;
    }

    @Transactional
    public ScoreResponse answer(Long gameId, AnswerRequest body) {
        GameSession s = sessionRepo.findById(gameId).orElseThrow();
        if (s.isFinished()) return new ScoreResponse(s.getScore(), s.getTotalQuestions(), true);

        GameQuestion q = questionRepo.findBySessionIdAndIdx(gameId, s.getCurrentIndex());
        q.setUserAnswer(body.answer);
        boolean correct = body.answer == q.getCorrectAnswer();
        q.setIsCorrect(correct);
        if (correct) s.setScore(s.getScore() + 1);
        questionRepo.save(q);

        int next = s.getCurrentIndex() + 1;
        if (next >= s.getTotalQuestions()) {
            s.setFinished(true);
        } else {
            s.setCurrentIndex(next);
        }
        sessionRepo.save(s);
        return new ScoreResponse(s.getScore(), s.getTotalQuestions(), s.isFinished());
    }

    @Transactional(readOnly = true)
    public HistoryResponse history(Long gameId) {
        GameSession s = sessionRepo.findById(gameId).orElseThrow();
        List<GameQuestion> list = questionRepo.findBySessionIdOrderByIdxAsc(gameId);
        List<HistoryItem> items = new ArrayList<>();
        for (GameQuestion q : list) {
            HistoryItem hi = new HistoryItem();
            hi.a = q.getA(); hi.b = q.getB(); hi.correctAnswer = q.getCorrectAnswer();
            hi.userAnswer = q.getUserAnswer(); hi.isCorrect = q.getIsCorrect();
            items.add(hi);
        }
        HistoryResponse hr = new HistoryResponse();
        hr.gameId = s.getId(); hr.playerName = s.getPlayerName();
        hr.score = s.getScore(); hr.total = s.getTotalQuestions();
        hr.questions = items;
        return hr;
    }

    private int rng(int min, int max) { return min + rnd.nextInt((max - min) + 1); }
}