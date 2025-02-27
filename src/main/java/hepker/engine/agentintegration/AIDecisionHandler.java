package hepker.engine.agentintegration;

import hepker.ai.utils.DecisionHandler;
import hepker.ai.utils.AITools;
import hepker.game.entity.movement.ActionNode;
import hepker.game.gameworld.PieceManager;

/**
 * AI Utility for calculations
 * */
public final class AIDecisionHandler implements DecisionHandler {

    private PieceManager pMgr;
    private final AITools toolbox;
    private Environment env;

    private int numOptionsNaught;
    private int numEnemyOptionsNaught;

    private int pointsFromDecision;
    private int numEnemiesNaught;

    private ActionNode[] decisionArray;

    public AIDecisionHandler(PieceManager pMgr, AITools toolbox, Environment env) {
        this.pMgr = pMgr;
        this.toolbox = toolbox;
        this.env = env;
        updateDecisionContainer();
    }

    public void updateDecisionContainer() {
        decisionArray = toolbox.getDecisionArray(pMgr);
    }

    public int getNumDecisions() {
        return decisionArray.length;
    }

    public void movePiece(int moveChoice) {
        pMgr.machineMovePiece(decisionArray[moveChoice]);
        pMgr.updateAllPieces();
    }

    public void setPreDecisionRewardParameters(int moveChoice) {
        setPreDecisionRewardParameters(env, decisionArray, moveChoice);
    }

    public double getDecisionReward() {
        double ratioOptions = (double) decisionArray.length / toolbox.getNumOpponentOptions(pMgr) -
                (double) numOptionsNaught / numEnemyOptionsNaught;
        int numAlliedPieces = env.getNumAlliedPieces();
        double ratioPieces = (double) numAlliedPieces / env.getNumEnemyPieces() -
                (double) numAlliedPieces / numEnemiesNaught;
        int pointsEarned = pointsFromDecision - toolbox.getMaximumOpponentReward(pMgr);
        return ratioOptions + ratioPieces + pointsEarned;
    }

    public void setPreDecisionRewardParameters(Environment env, ActionNode[] decisionArray, int moveChoice) {
        this.numEnemiesNaught = env.getNumEnemyPieces();
        this.numOptionsNaught = decisionArray.length;
        this.numEnemyOptionsNaught = toolbox.getNumOpponentOptions(pMgr);
        pointsFromDecision = decisionArray[moveChoice].getReward();
    }
}
