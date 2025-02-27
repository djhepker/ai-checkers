package hepker.ai.ai;

import hepker.engine.agentintegration.AIDecisionHandler;
import hepker.engine.agentintegration.Environment;
import hepker.ai.utils.AITools;
import hepker.ai.table.DataManager;
import hepker.game.gameworld.PieceManager;

import java.util.Random;

/*
* STATE: pieces[][] converted to a hexadecimal String
* ACTION: Moving pieces when it is Agent's turn
* REWARD: Positive, Negative, & Neutral
* EPISODE: GameEngine calls updateGame() in-between episodes
* Q-VALUE: Metrics used to evaluate actions at specific states
* MODEL: Q(S,a,S') ─► Model "Q" is action "a" given state "S" results in "S'"
* └► P(S'|S,a) = Probability of reaching a state "S'" if action "a" is taken in state "S"
* "A" is the set of all possible actions
* "A(s)" defines the set of actions that can be taken while in state "S"
* POLICY: A mapping from "S" to "a"; a solution to the Markov decision process. Indicates
* action "a" is to be taken while in state "S"
* */

public final class Agent implements AI {

    private final boolean isDusky;

    // All agents same the same database as to not create multiple or wear memory
    private static DataManager qTableMgr;

    private final AITools toolbox;

    private PieceManager pMgr;

    private double GAMMA = 0.9; // value of knowledge
    private double ALPHA = 0.82; // learning rate
    private double EPSILON = 0.8; // exploration rate
    private double RHO;
    private double currentQ;
    private double maxQPrime;

    private String stateKey;

    public Agent (PieceManager pMgr, AITools toolbox, boolean vsLight) {
        this.pMgr = pMgr;
        this.isDusky = vsLight;
        this.toolbox = toolbox;
        this.qTableMgr = new DataManager();
        this.currentQ = 0.0;
        this.maxQPrime = 0.0;
        this.RHO = 0.0;
        System.out.println("Agent boolean isDusky is: " + isDusky);
    }

    public void update(String stateKeyPrime, int actionChoiceInt) {
        calculateMaxQPrime(stateKeyPrime); // VALID
        updateQValue(actionChoiceInt); // VALID
    }

    public void setStateKey(String stateKey) {
        this.stateKey = stateKey;
    }

    public int getActionInt(int numDecisions) {
        if (Math.random() < EPSILON) {
            return explore(numDecisions);
        } else {
            return exploit();
        }
    }

    public void updateCurrentQ(String stateKey, int actionChoiceInt) {
        this.currentQ = getQValue(stateKey, actionChoiceInt);
    }

    private int exploit() {
        return qTableMgr.getMaxQIndex(stateKey);
    }

    private int explore(int numDecisions) {
        return new Random().nextInt(numDecisions);
    }

    private double getQValue(String stateKey, int moveChoice) {
        if (qTableMgr.isWithinSize(stateKey, moveChoice)) {
            return qTableMgr.queryQTableForValue(stateKey, moveChoice);
        } else {
            return 0.0;
        }
    }

    public void updateRho(double updatedReward) {
        this.RHO = updatedReward;
    }

    /**
     * Updates maxQPrime
     * @param stateKeyPrimeString String representation of the gamestate, used to query sqlite table
     */
    public void calculateMaxQPrime(String stateKeyPrimeString) {
        this.maxQPrime = qTableMgr.getMaxQValue(stateKeyPrimeString);
    }

    private void updateQValue(int moveChoice) {
        double updatedQ = currentQ + ALPHA * (RHO + GAMMA * maxQPrime - currentQ);
        if (Math.abs(updatedQ - currentQ) > 0.01) {
            qTableMgr.putUpdatedValue(stateKey, moveChoice, updatedQ);
        }
    }

    public void finalizeQTableUpdate(boolean gameWon) {
        qTableMgr.updateData(gameWon);
    }
}
