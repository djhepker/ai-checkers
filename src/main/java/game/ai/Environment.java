package main.java.game.ai;

import main.java.game.gameworld.PieceManager;

class Environment {
    private AgentTools toolbox;
    private PieceManager pMgr;

    int[] gameStateArr;
    int[] gameStateArrPrime;

    public Environment(AgentTools toolbox, PieceManager pMgr) {
        this.pMgr = pMgr;
        this.toolbox = toolbox;
        updateEnvironment();
    }

    private void updateEnvironment() {
        this.gameStateArr = getStateArray(pMgr);
    }

    public String getEncodedGameState(PieceManager pMgr) {
        return toolbox.getHexadecimalEncodingOfArr(getStateArray(pMgr));
    }

    public void generateStatePrime() {
        this.gameStateArrPrime = getStateArray(pMgr);
    }

    public int[] getStateArray(PieceManager pMgr) {
        int[] gameState = new int[64];
        for (int j = 0; j < 8; ++j) {
            for (int i = 0; i < 8; ++i) {
                gameState[j * 8 + i] = toolbox.pieceToInt(pMgr.getPiece(i, j));
            }
        }
        return gameState;
    }
}
