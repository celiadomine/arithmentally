package ch.celia.domine.arithmentally.game.dto;

public class StartGameRequest {
    public String playerName;
    public Integer numQuestions; // default 10
    public Integer min;          // default 1
    public Integer max;          // default 9
}