package main.java.game.ai;

import main.java.game.gameworld.PieceManager;

import java.util.Arrays;

class Environment {
    private AgentTools toolbox;
    private PieceManager pMgr;

    int[] gameStateArr;
    int[] gameStateArrPrime;

    public Environment(AgentTools toolbox, PieceManager pMgr) {
        this.pMgr = pMgr;
        this.toolbox = toolbox;
        generateStatePrime();
        updateEnvironment();
        updateEnvironment();
    }

    public int getNumAlliedPieces() {
        return (int) Arrays.stream(gameStateArrPrime)
                .filter(num -> num >= 1)
                .count();
    }

    public int getNumEnemyPieces() {
        return (int) Arrays.stream(gameStateArrPrime)
                .filter(num -> num <= -1)
                .count();
    }

    public String getEncodedGameState(PieceManager pMgr) {
        return toolbox.getHexadecimalEncodingOfArr(getStateArray(pMgr));
    }

    public void generateStatePrime() {
        this.gameStateArrPrime = getStateArray(pMgr);
    }

    private void updateEnvironment() {
        this.gameStateArr = getStateArray(pMgr);
    }

    private int[] getStateArray(PieceManager pMgr) {
        int[] gameState = new int[64];
        for (int j = 0; j < 8; ++j) {
            for (int i = 0; i < 8; ++i) {
                gameState[j * 8 + i] = toolbox.pieceToInt(pMgr.getPiece(i, j));
            }
        }
        return gameState;
    }
}
