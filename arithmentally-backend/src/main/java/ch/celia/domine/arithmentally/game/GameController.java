package ch.celia.domine.arithmentally.game;

import ch.celia.domine.arithmentally.game.dto.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService service;

    public GameController(GameService service) {
        this.service = service;
    }

    @PostMapping("/start")
    public StartGameResponse start(@RequestBody(required = false) StartGameRequest body) {
        if (body == null) body = new StartGameRequest();
        return service.start(body);
    }

    @GetMapping("/{id}/current")
    public QuestionResponse current(@PathVariable("id") Long id) {
        return service.current(id);
    }

    @PostMapping("/{id}/answer")
    public ScoreResponse answer(@PathVariable("id") Long id, @RequestBody AnswerRequest body) {
        return service.answer(id, body);
    }

    @GetMapping("/{id}/history")
    public HistoryResponse history(@PathVariable("id") Long id) {
        return service.history(id);
    }

    @GetMapping("/{id}/score")
    public ScoreResponse score(@PathVariable("id") Long id) {
        // Für den Score reicht History; finished holen wir aus current()
        QuestionResponse cur = service.current(id);
        HistoryResponse h = service.history(id);
        return new ScoreResponse(h.score, h.total, cur.finished);
    }
}
