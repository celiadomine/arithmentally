package ch.celia.domine.arithmentally.game;

import ch.celia.domine.arithmentally.player.Player;
import ch.celia.domine.arithmentally.player.PlayerService;
import ch.celia.domine.arithmentally.security.Roles;
import jakarta.annotation.security.RolesAllowed;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
@Validated
public class GameController {

    private final GameService gameService;
    private final PlayerService playerService;

    public GameController(GameService gameService, PlayerService playerService) {
        this.gameService = gameService;
        this.playerService = playerService;
    }

    @PostMapping("/start")
    @RolesAllowed({Roles.Read})
    public ResponseEntity<GameSession> startGame(@AuthenticationPrincipal Jwt jwt, @RequestBody GameStartDTO dto) {
        Player player = playerService.getOrCreatePlayer(jwt.getSubject(), jwt.getClaim("preferred_username"), jwt.getClaim("email"));
        GameSession session = gameService.startNewGame(player, dto.toConfiguration());
        return ResponseEntity.status(201).body(session);
    }

    @GetMapping("/history")
    @RolesAllowed({Roles.Read})
    public ResponseEntity<List<GameSession>> getMyGameHistory(@AuthenticationPrincipal Jwt jwt) {
        Player player = playerService.getOrCreatePlayer(jwt.getSubject(), jwt.getClaim("preferred_username"), jwt.getClaim("email"));
        return ResponseEntity.ok(gameService.getGamesForPlayer(player.getId()));
    }
}
