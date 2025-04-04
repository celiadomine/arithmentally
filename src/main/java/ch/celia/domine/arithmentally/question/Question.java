package ch.celia.domine.arithmentally.question;
import ch.celia.domine.arithmentally.game.GameSession;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String task;
    private String correctAnswer;
    private String userAnswer;
    private int timeTaken;

    @ManyToOne
    @JoinColumn(name = "game_session_id")
    private GameSession gameSession;
}
