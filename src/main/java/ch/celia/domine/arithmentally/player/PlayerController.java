package ch.celia.domine.arithmentally.player;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/player")
@Validated
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/me")
    public ResponseEntity<Player> getCurrentPlayer(@AuthenticationPrincipal Jwt jwt) {
        String keycloakId = jwt.getSubject();
        String name = jwt.getClaim("preferred_username");
        String email = jwt.getClaim("email");

        Player player = playerService.getOrCreatePlayer(keycloakId, name, email);
        return ResponseEntity.ok(player);
    }
}