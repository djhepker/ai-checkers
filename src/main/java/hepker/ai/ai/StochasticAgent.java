package hepker.ai.ai;

import hepker.ai.utils.AITools;
import hepker.game.entity.movement.ActionNode;
import hepker.game.gameworld.PieceManager;

import java.util.Random;

public class StochasticAgent {
    private final boolean isDusky;

    private PieceManager pMgr;
    private AITools toolbox;

    public StochasticAgent(PieceManager pMgr, AITools toolbox, boolean vsLight) {
        this.isDusky = vsLight;
        this.pMgr = pMgr;
        this.toolbox = toolbox;
    }

    public void update() {
        ActionNode[] decisionArr = toolbox.getDecisionArray(pMgr);
        if (decisionArr.length == 0) {
            pMgr.flagGameOver();
            return;
        }
        int moveChoice = new Random().nextInt(decisionArr.length);
        if (!pMgr.machineMovePiece(decisionArr[moveChoice])) {
            throw new RuntimeException("StochasticNPC update failed");
        }
        pMgr.updateAllPieces();
    }
}
