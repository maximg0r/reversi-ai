package com.maximgorshkov.reversi;

public class OnlineActionRequest {
    private final Long gameId;
    private final int row;
    private final int col;

    public OnlineActionRequest(Long gameId, int row, int col) {
        this.gameId = gameId;
        this.row = row;
        this.col = col;
    }

    public Long getGameId() {
        return gameId;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
