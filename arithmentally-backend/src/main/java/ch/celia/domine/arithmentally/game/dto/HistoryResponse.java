package ch.celia.domine.arithmentally.game.dto;

import java.util.List;

public class HistoryResponse {
    public Long gameId;
    public String playerName;
    public int score;
    public int total;
    public List<HistoryItem> questions;
}