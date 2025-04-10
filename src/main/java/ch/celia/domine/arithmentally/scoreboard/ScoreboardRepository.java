package ch.celia.domine.arithmentally.scoreboard;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreboardRepository extends JpaRepository<ScoreboardEntry, Long> {

    // Get top 10 scores
    List<ScoreboardEntry> findTop10ByOrderByScoreDesc();
}
