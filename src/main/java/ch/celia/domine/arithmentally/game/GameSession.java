package ch.celia.domine.arithmentally.game;

import java.time.LocalDateTime;
import java.util.List;

import ch.celia.domine.arithmentally.player.Player;
import ch.celia.domine.arithmentally.question.Question;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
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

//    @Embedded
  //  private RoundConfiguration configuration;

    @OneToMany(mappedBy = "gameSession", cascade = CascadeType.ALL)
    private List<Question> questions;
}
