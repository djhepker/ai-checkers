package hepker.engine.agentintegration;

import hepker.ai.utils.AITools;
import hepker.game.gameworld.PieceManager;

public final class Environment {

    private Environment() {

    }

    public static String getEncodedGameState(PieceManager inputPMgr) {
        return AITools.intArrTo64Encoding(getStateArray(inputPMgr));
    }

    private static int[] getStateArray(PieceManager inputPMgr) {
        int[] gameState = new int[64];
        for (int j = 0; j < 8; ++j) {
            for (int i = 0; i < 8; ++i) {
                gameState[j * 8 + i] = AITools.pieceToInt(inputPMgr.getPiece(i, j));
            }
        }
        return gameState;
    }
}
