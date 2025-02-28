package hepker.ai.ai;

import hepker.ai.table.DataManager;

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

    private double GAMMA = 0.9; // value of knowledge
    private double ALPHA = 0.82; // learning rate
    private double EPSILON = 0.8; // exploration rate
    private double RHO;
    private double currentQ;
    private double maxQPrime;

    private String stateKey;

    public Agent () {
        this.currentQ = 0.0;
        this.maxQPrime = 0.0;
        this.RHO = 0.0;
    }

    public void update(String stateKeyPrime, int actionChoiceInt) {
        calculateMaxQPrime(stateKeyPrime);
        updateQValue(actionChoiceInt);
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
        if (EPSILON == 0.0) {
            return;
        }
        this.maxQPrime = qTableMgr.getMaxQValue(stateKeyPrimeString);
    }

    public void finalizeQTableUpdate() {
        if (EPSILON != 1.0) {
            qTableMgr.updateData();
        }
    }

    /**
     * Mutator for Epsilon. Epsilon is the probability that Agent will select a random action. The probability that
     * Agent will select the learned optimal decision is 1 - Epsilon
     * @param epsilon The new value we will set Epsilon to [0, 1]. Epsilon will be 1 if argument greater than one, and
     *                0 if argument less than zero.
     */
    public void setEpsilon(double epsilon) {
        if (epsilon < 0.0) {
            this.EPSILON = 0.0;
        } else this.EPSILON = Math.min(epsilon, 1.0);
    }

    private void updateQValue(int moveChoice) {
        double updatedQ = currentQ + ALPHA * (RHO + GAMMA * maxQPrime - currentQ);
        if (Math.abs(updatedQ - currentQ) > 0.01) {
            qTableMgr.putUpdatedValue(stateKey, moveChoice, updatedQ);
        }
    }
}
