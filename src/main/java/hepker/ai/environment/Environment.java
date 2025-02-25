package hepker.ai.environment;

import hepker.ai.utils.AITools;
import hepker.game.gameworld.PieceManager;

public class Environment {
    private AITools toolbox;
    private PieceManager pMgr;

    int[] gameStateArr;
    int[] gameStateArrPrime;

    public Environment(AITools toolbox, PieceManager pMgr) {
        this.pMgr = pMgr;
        this.toolbox = toolbox;
        generateStatePrime();
        updateEnvironment();
    }

    public int getNumAlliedPieces() {
        if (toolbox.isDusky()) {
            return pMgr.getNumDusky();
        } else {
            return pMgr.getNumLight();
        }
    }

    public int getNumEnemyPieces() {
        if (toolbox.isDusky()) {
            return pMgr.getNumLight();
        } else {
            return pMgr.getNumDusky();
        }
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
