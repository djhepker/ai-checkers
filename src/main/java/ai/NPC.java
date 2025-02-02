package main.java.ai;

import main.java.ai.environment.AgentDecisionHandler;
import main.java.ai.utils.AgentTools;
import main.java.game.gameworld.PieceManager;

abstract class NPC {

    public void update(PieceManager pMgr, AgentTools toolbox) {
        AgentDecisionHandler decisionHandler = new AgentDecisionHandler(pMgr, toolbox);
        decisionHandler.updateDecisionArray();
        int moveChoice = getMoveChoice(decisionHandler);
        decisionHandler.movePiece(moveChoice);
    }

    public int getMoveChoice(AgentDecisionHandler handler) {
        return 0;
    }
}
