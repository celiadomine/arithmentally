package ch.celia.domine.arithmentally.scoreboard;

import ch.celia.domine.arithmentally.game.GameSession;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScoreboardService {

    private final ScoreboardRepository scoreboardRepository;

    public ScoreboardService(ScoreboardRepository scoreboardRepository) {
        this.scoreboardRepository = scoreboardRepository;
    }

    public void saveEntry(String username, String email, int score, int duration, GameSession gameSession) {
        ScoreboardEntry entry = new ScoreboardEntry();
        entry.setUsername(username);
        entry.setEmail(email);
        entry.setScore(score);
        entry.setDuration(duration);
        entry.setDate(LocalDateTime.now());
        entry.setGameSession(gameSession);

        scoreboardRepository.save(entry);
    }

    public List<ScoreboardEntry> getTop10() {
        return scoreboardRepository.findTop10ByOrderByScoreDesc();
    }
}
