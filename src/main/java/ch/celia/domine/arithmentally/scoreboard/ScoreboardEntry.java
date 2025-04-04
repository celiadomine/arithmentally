package ch.celia.domine.arithmentally.scoreboard;


import java.time.LocalDateTime;

import ch.celia.domine.arithmentally.game.GameSession;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class ScoreboardEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private int score;
    private int duration;
    private LocalDateTime date;

    @OneToOne
    @JoinColumn(name = "game_session_id")
    private GameSession gameSession;
}