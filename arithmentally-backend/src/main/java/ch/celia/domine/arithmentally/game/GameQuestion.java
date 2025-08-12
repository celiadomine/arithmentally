package ch.celia.domine.arithmentally.game;

import jakarta.persistence.*;

@Entity
@Table(name = "game_question")
public class GameQuestion {

    public enum Op { ADD, SUB, MUL, DIV }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private GameSession session;

    @Column(name = "idx")
    private int idx;

    private int a;
    private int b;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Op op = Op.ADD; // Default

    private Integer correctAnswer;    // gespeichert für History/Konsistenz
    private Integer userAnswer;       // null bis beantwortet
    private Boolean isCorrect;        // null bis beantwortet

    // --- Getter/Setter ---
    public Long getId() { return id; }

    public GameSession getSession() { return session; }
    public void setSession(GameSession session) { this.session = session; }

    public int getIdx() { return idx; }
    public void setIdx(int idx) { this.idx = idx; }

    public int getA() { return a; }
    public void setA(int a) { this.a = a; }

    public int getB() { return b; }
    public void setB(int b) { this.b = b; }

    public Op getOp() { return op; }
    public void setOp(Op op) { this.op = op; }

    public Integer getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(Integer correctAnswer) { this.correctAnswer = correctAnswer; }

    public Integer getUserAnswer() { return userAnswer; }
    public void setUserAnswer(Integer userAnswer) { this.userAnswer = userAnswer; }

    public Boolean getIsCorrect() { return isCorrect; }
    public void setIsCorrect(Boolean correct) { isCorrect = correct; }
}
