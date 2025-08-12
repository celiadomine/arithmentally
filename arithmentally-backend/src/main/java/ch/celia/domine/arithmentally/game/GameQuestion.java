package ch.celia.domine.arithmentally.game;

import jakarta.persistence.*;

@Entity
public class GameQuestion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private GameSession session;

    private int idx; // 0-basierter Index innerhalb der Session
    private int a;
    private int b;
    private int correctAnswer;

    private Integer userAnswer; // null, falls noch nicht beantwortet
    private Boolean isCorrect;  // null, falls noch nicht beantwortet

    // getters/setters
    public Long getId() { return id; }
    public GameSession getSession() { return session; }
    public void setSession(GameSession session) { this.session = session; }
    public int getIdx() { return idx; }
    public void setIdx(int idx) { this.idx = idx; }
    public int getA() { return a; }
    public void setA(int a) { this.a = a; }
    public int getB() { return b; }
    public void setB(int b) { this.b = b; }
    public int getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(int correctAnswer) { this.correctAnswer = correctAnswer; }
    public Integer getUserAnswer() { return userAnswer; }
    public void setUserAnswer(Integer userAnswer) { this.userAnswer = userAnswer; }
    public Boolean getIsCorrect() { return isCorrect; }
    public void setIsCorrect(Boolean isCorrect) { this.isCorrect = isCorrect; }
}