package hepker.engine.agentintegration;

import hepker.ai.utils.DecisionHandler;
import hepker.ai.utils.AITools;
import hepker.ai.utils.DecisionCalculator;
import hepker.game.entity.movement.ActionNode;
import hepker.game.gameworld.PieceManager;

/**
 * AI Utility for calculations
 * */
public class AIDecisionHandler implements DecisionHandler {

    private PieceManager pMgr;
    private final AITools toolbox;
    private final DecisionCalculator calculator;
    private Environment env;

    private ActionNode[] decisionArray;

    public AIDecisionHandler(PieceManager pMgr, AITools toolbox, Environment env) {
        this.pMgr = pMgr;
        this.toolbox = toolbox;
        this.env = env;
        this.calculator = new DecisionCalculator(toolbox, pMgr);
        updateDecisionContainer();
    }

    public void updateDecisionContainer() {
        decisionArray = toolbox.getDecisionArray(pMgr);
    }

    public int getNumDecisions() {
        return decisionArray.length;
    }

    public void calculateDecisionReward(int moveChoice) {
        calculator.calculateDecisionReward(env, decisionArray, moveChoice);
    }

    public void movePiece(int moveChoice) {
        pMgr.machineMovePiece(decisionArray[moveChoice]);
        pMgr.updateAllPieces();
    }

    public double getDecisionReward() {
        return calculator.getReward(env, decisionArray);
    }
}
