package main.java.game.ai;

import main.java.game.entity.movement.ActionNode;
import main.java.game.gameworld.PieceManager;

class AgentDecisionHandler {
    private PieceManager pMgr;
    private AgentTools toolbox;
    private ActionNode[] decisionArray;

    private int numOptionsNaught;
    private int numEnemyOptionsNaught;

    private int pointsFromDecision;
    private int numEnemiesNaught;

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

    public void fulfillDecision(Environment env, int moveChoice) {
        this.numEnemiesNaught = env.getNumEnemyPieces();
        this.numOptionsNaught = decisionArray.length;
        this.numEnemyOptionsNaught = toolbox.getNumOpponentOptions(pMgr);
        pointsFromDecision = decisionArray[moveChoice].getReward();
        pMgr.machineMovePiece(decisionArray[moveChoice]);
        pMgr.updateAllPieces();
        updateDecisionArray();
    }

    public double getReward(Environment env) {
        double ratioOptions = (double) decisionArray.length / toolbox.getNumOpponentOptions(pMgr) -
                             (double) numOptionsNaught / numEnemyOptionsNaught;
        int numAlliedPieces = env.getNumAlliedPieces();
        double ratioPieces = (double) numAlliedPieces / env.getNumEnemyPieces() -
                             (double) numAlliedPieces / numEnemiesNaught;
        int pointsEarned = pointsFromDecision - toolbox.getMaximumOpponentReward(pMgr);
        return ratioOptions + ratioPieces + pointsEarned;
    }
}
