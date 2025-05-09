package ch.celia.domine.arithmentally.question;

import ch.celia.domine.arithmentally.game.GameSession;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String task;
    private String correctAnswer;
    private String userAnswer;
    private int timeTaken;
    private boolean correct;

    @ManyToOne
    @JoinColumn(name = "game_session_id")
    private GameSession gameSession;
}
