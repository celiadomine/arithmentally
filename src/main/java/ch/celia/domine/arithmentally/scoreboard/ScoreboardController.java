package ch.celia.domine.arithmentally.scoreboard;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scoreboard")
@Validated
public class ScoreboardController {

    private final ScoreboardService scoreboardService;

    public ScoreboardController(ScoreboardService scoreboardService) {
        this.scoreboardService = scoreboardService;
    }

    @GetMapping("/top")
    public ResponseEntity<List<ScoreboardEntry>> getTopScores() {
        return ResponseEntity.ok(scoreboardService.getTop10());
    }
}
