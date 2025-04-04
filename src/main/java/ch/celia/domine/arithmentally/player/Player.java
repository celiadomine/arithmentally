package ch.celia.domine.arithmentally.player;

import java.util.List;

import org.springframework.data.annotation.Id;

import ch.celia.domine.arithmentally.game.GameSession;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
    @Entity
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
