package main.java.ai.environment;

import main.java.ai.utils.AITools;
import main.java.ai.utils.DecisionCalculator;
import main.java.game.entity.movement.ActionNode;
import main.java.game.gameworld.PieceManager;

public class AIDecisionHandler {
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
        updateDecisionArray();
    }

    public void movePiece(int moveChoice) {
        pMgr.machineMovePiece(decisionArray[moveChoice]);
        pMgr.updateAllPieces();
    }

    public void updateDecisionArray() {
        decisionArray = toolbox.getDecisionArray(pMgr);
    }

    public int getNumDecisions() {
        return decisionArray.length;
    }

    public void calculateDecisionReward(int moveChoice) {
        calculator.calculateDecisionReward(env, decisionArray, moveChoice);
    }

    public double getDecisionReward() {
        return calculator.getReward(env, decisionArray);
    }
}
