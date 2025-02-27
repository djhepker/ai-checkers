package hepker.ai.utils;

import hepker.engine.agentintegration.Environment;
import hepker.game.entity.movement.ActionNode;
import hepker.game.gameworld.PieceManager;

public class DecisionCalculator {
    private AITools toolbox;
    private PieceManager pMgr;

    private int numOptionsNaught;
    private int numEnemyOptionsNaught;

    private int pointsFromDecision;
    private int numEnemiesNaught;

    public DecisionCalculator(AITools toolbox, PieceManager pMgr) {
        this.toolbox = toolbox;
        this.pMgr = pMgr;
    }

    public void calculateDecisionReward(Environment env, ActionNode[] decisionArray, int moveChoice) {
        this.numEnemiesNaught = env.getNumEnemyPieces();
        this.numOptionsNaught = decisionArray.length;
        this.numEnemyOptionsNaught = toolbox.getNumOpponentOptions(pMgr);
        pointsFromDecision = decisionArray[moveChoice].getReward();
    }

    // TODO: fix reward calculation to improve the rate of positive numbers
    public double getReward(Environment env, ActionNode[] decisionArray) {
        double ratioOptions = (double) decisionArray.length / toolbox.getNumOpponentOptions(pMgr) -
                (double) numOptionsNaught / numEnemyOptionsNaught;
        int numAlliedPieces = env.getNumAlliedPieces();
        double ratioPieces = (double) numAlliedPieces / env.getNumEnemyPieces() -
                (double) numAlliedPieces / numEnemiesNaught;
        int pointsEarned = pointsFromDecision - toolbox.getMaximumOpponentReward(pMgr);
        return ratioOptions + ratioPieces + pointsEarned;
    }
}
