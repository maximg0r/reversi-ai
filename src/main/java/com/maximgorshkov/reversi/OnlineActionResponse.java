package com.maximgorshkov.reversi;

public class OnlineActionResponse {
    private final Long gameId;
    private final int[][] board;
    private final int currPlayer; // 1=black, 2=white
    private final int num_of_blacks;
    private final int num_of_whites;
    private final boolean isValidMove;
    private final boolean isTerminalState;
    private final int gameUtil;

    public OnlineActionResponse(Long gameId, int[][] board, int currPlayer, int num_of_blacks, int num_of_whites,
            boolean isValidMove, boolean isTerminalState, int gameUtil) {
        this.gameId = gameId;
        this.board = board;
        this.currPlayer = currPlayer;
        this.num_of_blacks = num_of_blacks;
        this.num_of_whites = num_of_whites;
        this.isValidMove = isValidMove;
        this.isTerminalState = isTerminalState;
        this.gameUtil = gameUtil;
    }

    public Long getGameId() {
        return gameId;
    }

    public int[][] getBoard() {
        return board;
    }

    public int getCurrPlayer() {
        return currPlayer;
    }

    public int getNum_of_blacks() {
        return num_of_blacks;
    }

    public int getNum_of_whites() {
        return num_of_whites;
    }

    public boolean isValidMove() {
        return isValidMove;
    }

    public boolean isTerminalState() {
        return isTerminalState;
    }

    public int getGameUtil() {
        return gameUtil;
    }
}
