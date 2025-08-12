package ch.celia.domine.arithmentally.game;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "game_session")
public class GameSession {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String playerName;

    @Column(nullable = false)
    private Instant startedAt = Instant.now();

    private int score = 0;
    private int totalQuestions;
    private int currentIndex = 0;
    private boolean finished = false;

    // --- Getter/Setter ---
    public Long getId() { return id; }

    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public Instant getStartedAt() { return startedAt; }
    public void setStartedAt(Instant startedAt) { this.startedAt = startedAt; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public int getCurrentIndex() { return currentIndex; }
    public void setCurrentIndex(int currentIndex) { this.currentIndex = currentIndex; }

    public boolean isFinished() { return finished; }
    public void setFinished(boolean finished) { this.finished = finished; }
}
