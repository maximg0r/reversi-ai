package com.maximgorshkov.reversi;

public class NewGameInfo {
    private final int boardSize;
    private final String agentType;
    private final int depthLimit; // for H_Minimax only
    private final int playerColor; // 1 = 'x', 2 = 'o'

    public NewGameInfo(int boardSize, String agentType, int depthLimit, char playerColor) {
        this.boardSize = boardSize;
        this.agentType = agentType;
        this.depthLimit = depthLimit;
        this.playerColor = playerColor;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public String getAgentType() {
        return agentType;
    }

    public int getDepthLimit() {
        return depthLimit;
    }

    public int getPlayerColor() {
        return playerColor;
    }
}
