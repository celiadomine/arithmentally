package ch.celia.domine.arithmentally.player;

import ch.celia.domine.arithmentally.game.GameSession;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String keycloakId;
    private String name;
    private String email;
    private String role; // USER or ADMIN

    @OneToMany(mappedBy = "player")
    private List<GameSession> gameSessions;
}
