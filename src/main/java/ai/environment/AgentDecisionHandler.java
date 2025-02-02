package main.java.ai.environment;

import main.java.ai.utils.AgentTools;
import main.java.ai.utils.DecisionCalculator;
import main.java.game.entity.movement.ActionNode;
import main.java.game.gameworld.PieceManager;

public class AgentDecisionHandler {
    private PieceManager pMgr;
    private final AgentTools toolbox;
    private final DecisionCalculator calculator;

    private ActionNode[] decisionArray;

    public AgentDecisionHandler(PieceManager pMgr, AgentTools toolbox) {
        this.pMgr = pMgr;
        this.toolbox = toolbox;
        this.calculator = new DecisionCalculator(toolbox, pMgr);
    }

    public void movePiece(int moveChoice) {
        pMgr.machineMovePiece(decisionArray[moveChoice]);
        pMgr.updateAllPieces();
        updateDecisionArray();
    }

    public void updateDecisionArray() {
        decisionArray = toolbox.getDecisionArray(pMgr);
    }

    public int getNumDecisions() {
        return decisionArray.length;
    }

    public void calculateDecisionReward(Environment env, int moveChoice) {
        calculator.calculateDecisionReward(env, decisionArray, moveChoice);
    }

    public double getDecisionReward(Environment env) {
        return calculator.getReward(env, decisionArray);
    }
}
