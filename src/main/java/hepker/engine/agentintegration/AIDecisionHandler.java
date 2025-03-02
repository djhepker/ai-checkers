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
    private int reasonableTurnCount;

    private double decayingScalar;

    private ActionNode[] decisionArray;

    public AIDecisionHandler(PieceManager inputPMgr, AITools tools, Environment inputEnv) {
        this.pMgr = inputPMgr;
        this.toolbox = tools;
        this.env = inputEnv;
        this.reasonableTurnCount = 30;
        this.decayingScalar = 1.0;
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
        setPreDecisionRewardParameters(env, decisionArray[moveChoice]);
    }

    public double getDecisionReward() {
        double ratioOptions = (double) decisionArray.length / toolbox.getNumOpponentOptions(pMgr)
                - (double) numOptionsNaught / numEnemyOptionsNaught;
        double alliedPieces = env.getNumAlliedPieces();
        double ratioPieces = alliedPieces / env.getNumEnemyPieces()
                - alliedPieces / numEnemiesNaught;
        int pointsEarned = pointsFromDecision - toolbox.getMaximumOpponentReward(pMgr);

        double summation = ratioOptions + ratioPieces + pointsEarned;

        if (reasonableTurnCount < 0) {
            decayingScalar -= 0.09;
            return decayingScalar * Math.abs(summation);
        } else {
            --reasonableTurnCount;
            return summation;
        }
    }

    public void setPreDecisionRewardParameters(Environment inputEnv, ActionNode actionChosen) {
        this.numEnemiesNaught = inputEnv.getNumEnemyPieces();
        this.numOptionsNaught = decisionArray.length;
        this.numEnemyOptionsNaught = toolbox.getNumOpponentOptions(pMgr);
        pointsFromDecision = actionChosen.getReward();
    }
}
