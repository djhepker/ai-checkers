package main.java.game.ai;

import main.java.game.entity.movement.ActionNode;
import main.java.game.gameworld.PieceManager;

class AgentDecisionHandler {
    private PieceManager pMgr;
    private AgentTools toolbox;
    private ActionNode[] decisionArray;

    public AgentDecisionHandler(PieceManager pMgr, AgentTools toolbox) {
        this.pMgr = pMgr;
        this.toolbox = toolbox;
    }

    public void updateDecisionArray() {
        decisionArray = toolbox.getDecisionArray(pMgr);
    }

    public int getNumDecisions() {
        return decisionArray.length;
    }

    public void fulfillDecision(int moveChoice) {
        pMgr.movePiece(decisionArray[moveChoice]);
        pMgr.updateAllPieces();
    }
}
