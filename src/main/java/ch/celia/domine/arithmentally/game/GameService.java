package ch.celia.domine.arithmentally.game;

import ch.celia.domine.arithmentally.player.Player;
import ch.celia.domine.arithmentally.player.PlayerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    public GameService(GameRepository gameRepository, PlayerRepository playerRepository) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
    }

    public GameSession startNewGame(Player player, RoundConfiguration config) {
        GameSession game = new GameSession();
        game.setPlayer(player);
        game.setConfiguration(config);
        game.setDate(LocalDateTime.now());
        game.setScore(0);
        game.setDuration(0);

        return gameRepository.save(game);
    }

    public List<GameSession> getGamesForPlayer(Long playerId) {
        return gameRepository.findByPlayerId(playerId);
    }

    public GameSession updateGameScore(Long gameId, int score, int duration) {
        return gameRepository.findById(gameId).map(game -> {
            game.setScore(score);
            game.setDuration(duration);
            return gameRepository.save(game);
        }).orElseThrow(() -> new RuntimeException("Game not found"));
    }

    public void deleteGame(Long id) {
        gameRepository.deleteById(id);
    }
}
