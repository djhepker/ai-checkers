package hepker.ai.ai;

import hepker.ai.table.DataManager;
import lombok.Setter;

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

    // All agents same the same database as to not create multiple or wear memory
    private static DataManager qTableMgr;

    static {
        qTableMgr = new DataManager();
    }

    private final double gamma = 0.9; // value of knowledge
    private final double alpha = 0.82; // learning rate
    private double epsilon = 0.7; // exploration rate
    private double rho;
    private double currentQ;
    private double maxQPrime;

    @Setter
    private String stateKey;

    public Agent() {
        this.currentQ = 0.0;
        this.maxQPrime = 0.0;
        this.rho = 0.0;
    }

    public void update(String stateKeyPrime, int actionChoiceInt) {
        calculateMaxQPrime(stateKeyPrime);
        updateQValue(actionChoiceInt);
    }

    public int getActionInt(int numDecisions) {
        if (Math.random() < epsilon) {
            return explore(numDecisions);
        } else {
            return exploit();
        }
    }

    public void updateCurrentQ(String inputStateKey, int actionChoiceInt) {
        this.currentQ = getQValue(inputStateKey, actionChoiceInt);
    }

    private int exploit() {
        return qTableMgr.getMaxQIndex(stateKey);
    }

    private int explore(int numDecisions) {
        return new Random().nextInt(numDecisions);
    }

    private double getQValue(String inputStateKey, int moveChoice) {
        if (qTableMgr.isWithinSize(inputStateKey, moveChoice)) {
            return qTableMgr.queryQTableForValue(inputStateKey, moveChoice);
        } else {
            return 0.0;
        }
    }

    public void updateRho(double updatedReward) {
        this.rho = updatedReward;
    }

    /**
     * Updates maxQPrime
     * @param stateKeyPrimeString String representation of the gamestate, used to query sqlite table
     */
    public void calculateMaxQPrime(String stateKeyPrimeString) {
        if (epsilon == 0.0) {
            return;
        }
        this.maxQPrime = qTableMgr.getMaxQValue(stateKeyPrimeString);
    }

    public void finalizeQTableUpdate() {
        if (epsilon != 1.0) {
            qTableMgr.updateData();
        }
    }

    /**
     * Mutator for Epsilon. Epsilon is the probability that Agent will select a random action. The probability that
     * Agent will select the learned optimal decision is 1 - Epsilon
     * @param inputEpsilon The new value we will set Epsilon to [0, 1]. Epsilon will be 1 if argument greater
     *                     than one, and 0 if argument less than zero.
     */
    public void setEpsilon(double inputEpsilon) {
        this.epsilon = inputEpsilon < 0.0 ? 0.0 : Math.min(inputEpsilon, 1.0);
    }

    private void updateQValue(int moveChoice) {
        double updatedQ = currentQ + alpha * (rho + gamma * maxQPrime - currentQ);
        if (Math.abs(updatedQ - currentQ) > 0.01) {
            qTableMgr.putUpdatedValue(stateKey, moveChoice, updatedQ);
        }
    }
}
