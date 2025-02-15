package main.java.ai.ai;

import main.java.ai.utils.AITools;
import main.java.game.entity.movement.ActionNode;
import main.java.game.gameworld.PieceManager;

import java.util.Random;

public class StochasticNPC {
    private final boolean isDusky;

    private PieceManager pMgr;
    private AITools toolbox;

    public StochasticNPC(PieceManager pMgr, AITools toolbox, boolean vsLight) {
        this.isDusky = vsLight;
        this.pMgr = pMgr;
        this.toolbox = toolbox;
    }

    public void update() {
        ActionNode[] decisionArr = toolbox.getDecisionArray(pMgr);
        int moveChoice = new Random().nextInt(decisionArr.length);
        if (!pMgr.machineMovePiece(decisionArr[moveChoice])) {
            throw new RuntimeException("StochasticNPC update failed");
        }
        pMgr.updateAllPieces();
    }
}
