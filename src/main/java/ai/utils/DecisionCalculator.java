package main.java.ai.utils;

import main.java.ai.environment.Environment;
import main.java.game.entity.movement.ActionNode;
import main.java.game.gameworld.PieceManager;

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
