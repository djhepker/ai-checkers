package hepker.engine.agentintegration;

import hepker.ai.utils.AITools;
import hepker.game.gameworld.PieceManager;

public final class Environment {

    private final AITools toolbox;
    private PieceManager pMgr;

    public Environment(AITools tools, PieceManager inputPMgr) {
        this.pMgr = inputPMgr;
        this.toolbox = tools;
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

    public String getEncodedGameState(PieceManager inputPMgr) {
        return toolbox.intArrTo64Encoding(getStateArray(inputPMgr));
    }

    private int[] getStateArray(PieceManager inputPMgr) {
        int[] gameState = new int[64];
        for (int j = 0; j < 8; ++j) {
            for (int i = 0; i < 8; ++i) {
                gameState[j * 8 + i] = toolbox.pieceToInt(inputPMgr.getPiece(i, j));
            }
        }
        return gameState;
    }
}
