package ch.celia.domine.arithmentally.game.dto;

public class ScoreResponse {
    public int score;
    public int total;
    public boolean finished;
    public ScoreResponse(int score, int total, boolean finished) {
        this.score = score; this.total = total; this.finished = finished;
    }
}