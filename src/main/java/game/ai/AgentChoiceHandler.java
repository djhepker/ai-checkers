package main.java.game.ai;

import main.java.game.entity.movement.ActionNode;
import main.java.game.gameworld.PieceManager;

class AgentChoiceHandler {
    private PieceManager pMgr;
    private AgentTools toolbox;

    public AgentChoiceHandler(PieceManager pMgr, AgentTools toolbox) {
        this.pMgr = pMgr;
        this.toolbox = toolbox;
    }

    public void fulfillDecision(int moveChoice) {
        pMgr.movePiece(toolbox.getActionsArray(pMgr)[moveChoice])
        pMgr.updateAllPieces();
    }
}
