package ch.celia.domine.arithmentally.game;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<GameSession, Long> {

    // Get all games of a specific player
    List<GameSession> findByPlayerId(Long playerId);

    // Top scoring games
    List<GameSession> findTop10ByOrderByScoreDesc();
}
