package ch.celia.domine.arithmentally.game.dto;

/**
 * Optional: ops = "ADD,SUB,MUL,DIV"
 * Fehlt das Feld, wird nur ADD verwendet.
 */
public class StartGameRequest {
    public String playerName;
    public Integer numQuestions; // default 10
    public Integer min;          // default 1
    public Integer max;          // default 9
    public String ops;           // optional: CSV aus {ADD,SUB,MUL,DIV}
}
