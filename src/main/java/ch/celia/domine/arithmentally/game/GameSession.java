package ch.celia.domine.arithmentally.game;

import ch.celia.domine.arithmentally.player.Player;
import ch.celia.domine.arithmentally.question.Question;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class GameSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    private int score;
    private int duration;
    private LocalDateTime date;

    @Embedded
    private RoundConfiguration configuration;

    @OneToMany(mappedBy = "gameSession", cascade = CascadeType.ALL)
    private List<Question> questions;
}
