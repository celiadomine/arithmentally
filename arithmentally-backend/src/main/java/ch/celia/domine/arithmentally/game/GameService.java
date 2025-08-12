package ch.celia.domine.arithmentally.game;

import ch.celia.domine.arithmentally.game.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.ArrayList;


import java.time.Instant;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class GameService {

    private final GameSessionRepository sessionRepo;
    private final GameQuestionRepository questionRepo;

    public GameService(GameSessionRepository sessionRepo, GameQuestionRepository questionRepo) {
        this.sessionRepo = sessionRepo;
        this.questionRepo = questionRepo;
    }

    @Transactional
    public StartGameResponse start(StartGameRequest req) {
        String player = (req != null && req.playerName != null && !req.playerName.isBlank())
                ? req.playerName : "Player";
        int total = clampOrDefault(req != null ? req.numQuestions : null, 1, 100, 10);
        int min = clampOrDefault(req != null ? req.min : null, -99, 99, 1);
        int max = clampOrDefault(req != null ? req.max : null, -99, 999, 9);
        if (min > max) { int t = min; min = max; max = t; }

        List<GameQuestion.Op> allowedOps = parseOps(req != null ? req.ops : null);

        GameSession s = new GameSession();
        s.setPlayerName(player);
        s.setTotalQuestions(total);
        s.setCurrentIndex(0);
        s.setScore(0);
        s.setStartedAt(Instant.now()); // <= braucht setStartedAt in GameSession
        sessionRepo.save(s);

        for (int i = 0; i < total; i++) {
            GameQuestion q = new GameQuestion();
            q.setSession(s);
            q.setIdx(i);

            GameQuestion.Op op = allowedOps.get(rand(0, allowedOps.size() - 1));
            q.setOp(op);

            int a; int b;

            if (op == GameQuestion.Op.DIV) {
                b = Math.max(1, rand(Math.max(1, min), Math.max(1, max)));
                int res = rand(1, Math.max(1, Math.min(9, Math.abs(max))));
                a = res * b;
                q.setA(a);
                q.setB(b);
                q.setCorrectAnswer(res);
            } else {
                a = rand(min, max);
                b = rand(min, max);
                if (op == GameQuestion.Op.SUB && b > a) { int t = a; a = b; b = t; }
                q.setA(a);
                q.setB(b);
                q.setCorrectAnswer(switch (op) {
                    case ADD -> a + b;
                    case SUB -> a - b;
                    case MUL -> a * b;
                    case DIV -> 0; // schon gesetzt
                });
            }
            questionRepo.save(q);
        }

        return new StartGameResponse(s.getId());
    }

    @Transactional(readOnly = true)
    public QuestionResponse current(Long sessionId) {
        GameSession s = sessionRepo.findById(sessionId).orElseThrow();
        QuestionResponse dto = new QuestionResponse();
        dto.index = s.getCurrentIndex();
        dto.total = s.getTotalQuestions();

        if (s.getCurrentIndex() >= s.getTotalQuestions()) {
            dto.finished = true;
            return dto;
        }

        GameQuestion q = questionRepo.findBySessionIdAndIdx(sessionId, s.getCurrentIndex());
        dto.a = q.getA();
        dto.b = q.getB();
        dto.op = symbol(q.getOp());   // <- Operator ins DTO
        dto.finished = false;
        return dto;
    }

    @Transactional
    public ScoreResponse answer(Long sessionId, AnswerRequest body) {
        GameSession s = sessionRepo.findById(sessionId).orElseThrow();

        if (s.getCurrentIndex() >= s.getTotalQuestions()) {
            return new ScoreResponse(s.getScore(), s.getTotalQuestions(), true);
        }

        GameQuestion q = questionRepo.findBySessionIdAndIdx(sessionId, s.getCurrentIndex());
        Integer user = (body == null) ? null : body.answer;
        boolean correct = (user != null) && user.equals(q.getCorrectAnswer());

        q.setUserAnswer(user);
        q.setIsCorrect(correct);
        if (correct) {
            s.setScore(s.getScore() + 1);
        }

        s.setCurrentIndex(s.getCurrentIndex() + 1);
        boolean finished = (s.getCurrentIndex() >= s.getTotalQuestions());

        questionRepo.save(q);
        sessionRepo.save(s);

        return new ScoreResponse(s.getScore(), s.getTotalQuestions(), finished);
    }

    @Transactional(readOnly = true)
    public HistoryResponse history(Long sessionId) {
        GameSession s = sessionRepo.findById(sessionId).orElseThrow();
        List<GameQuestion> list = questionRepo.findBySessionIdOrderByIdxAsc(sessionId);

        HistoryResponse resp = new HistoryResponse();
        resp.gameId = s.getId();
        resp.playerName = s.getPlayerName();
        resp.score = s.getScore();
        resp.total = s.getTotalQuestions();
        resp.questions = new ArrayList<>(list.size());
        for (GameQuestion q : list) {
            HistoryItem hi = new HistoryItem();
            hi.a = q.getA();
            hi.b = q.getB();
            hi.correctAnswer = q.getCorrectAnswer();
            hi.userAnswer = q.getUserAnswer();
            hi.isCorrect = q.getIsCorrect();
            resp.questions.add(hi);
        }
        return resp;
    }

    // --------- Helpers ---------
    private static int clampOrDefault(Integer v, int min, int max, int dflt) {
        if (v == null) return dflt;
        return Math.max(min, Math.min(max, v));
    }

    private static int rand(int min, int max) {
        if (min > max) { int t = min; min = max; max = t; }
        return ThreadLocalRandom.current().nextInt(max - min + 1) + min;
    }

    private static List<GameQuestion.Op> parseOps(String opsCsv) {
        if (opsCsv == null || opsCsv.isBlank()) return List.of(GameQuestion.Op.ADD);
        List<GameQuestion.Op> list = new ArrayList<>();
        for (String p : opsCsv.split(",")) {
            String token = p.trim().toUpperCase(Locale.ROOT);
            try { list.add(GameQuestion.Op.valueOf(token)); }
            catch (IllegalArgumentException ignored) { }
        }
        return list.isEmpty() ? List.of(GameQuestion.Op.ADD) : list;
    }

    private static String symbol(GameQuestion.Op op) {
        return switch (op) {
            case ADD -> "+";
            case SUB -> "-";
            case MUL -> "×";
            case DIV -> "÷";
        };
    }
}
