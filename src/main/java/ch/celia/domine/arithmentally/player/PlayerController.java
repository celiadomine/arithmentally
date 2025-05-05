package ch.celia.domine.arithmentally.player;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ch.celia.domine.arithmentally.security.Roles;
import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/api/player")
@Validated
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/me")
    @RolesAllowed({Roles.Read})
    public ResponseEntity<Player> getCurrentPlayer(@AuthenticationPrincipal Jwt jwt) {
        String keycloakId = jwt.getSubject();
        String name = jwt.getClaim("preferred_username");
        String email = jwt.getClaim("email");

        Player player = playerService.getOrCreatePlayer(keycloakId, name, email);
        return ResponseEntity.ok(player);
    }

    @DeleteMapping("/{id}")
    @RolesAllowed({Roles.Read})
    public ResponseEntity<?> deletePlayer(@PathVariable Long id) {
        playerService.deletePlayerById(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @PutMapping("/{id}")
    @RolesAllowed({Roles.Read})
    public ResponseEntity<Player> updatePlayer(@PathVariable Long id, @RequestBody Player updatedPlayer) {
        return ResponseEntity.ok(playerService.updatePlayerById(id, updatedPlayer));
    }

}