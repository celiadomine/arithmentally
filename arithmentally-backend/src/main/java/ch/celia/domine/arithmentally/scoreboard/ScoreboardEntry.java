package ch.celia.domine.arithmentally.scoreboard;

import ch.celia.domine.arithmentally.game.GameSession;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
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
    @JsonIgnore
    private GameSession gameSession;
}
