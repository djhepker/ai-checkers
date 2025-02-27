package hepker.ai.ai;

import hepker.ai.utils.AITools;
import hepker.game.entity.movement.ActionNode;
import hepker.game.gameworld.PieceManager;

import java.util.Random;

public class StochasticAgent implements AI {
    private final boolean isDusky;

    private PieceManager pMgr;
    private AITools toolbox;

    public StochasticAgent(PieceManager pMgr, AITools toolbox, boolean vsLight) {
        this.isDusky = vsLight;
        this.pMgr = pMgr;
        this.toolbox = toolbox;
        System.out.println("StochasticAgent boolean isDusky is: " + isDusky);
    }

    public int getActionInt(int numDecisions) {
        return new Random().nextInt(numDecisions);
    }

    public void update() {
        ActionNode[] decisionArr = toolbox.getDecisionArray(pMgr);
        if (decisionArr.length == 0) {
            pMgr.flagGameOver();
            return;
        }
        int moveChoice = getActionInt(decisionArr.length);
        if (!pMgr.machineMovePiece(decisionArr[moveChoice])) {
            throw new RuntimeException("StochasticNPC update failed");
        }
        pMgr.updateAllPieces();
    }
}
