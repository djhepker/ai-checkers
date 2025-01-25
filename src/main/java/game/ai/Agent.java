package main.java.game.ai;

import main.java.game.gameworld.PieceManager;

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

public class Agent {
    private final boolean isDusky;

    private AgentTools toolbox;
    private QTableManager qTableMgr;
    private AgentDecisionHandler decisionHandler;
    private Environment environment;
    private PieceManager pMgr;

    private final double GAMMA = 0.75;
    private final double ALPHA = 0.84;
    private final double EPSILON = 0.92;
    private double RHO;
    private double currentQ;
    private double maxQPrime;

    private String stateKey;

    public Agent (PieceManager pMgr, boolean playerLight) {
        this.isDusky = playerLight;
        this.pMgr = pMgr;
        this.toolbox = new AgentTools(isDusky);
        this.qTableMgr = new QTableManager();
        this.decisionHandler = new AgentDecisionHandler(pMgr, toolbox);
        this.currentQ = 0.0;
        this.maxQPrime = 0.0;
        this.RHO = 0.0;
    }

    public void update() {
        this.environment = new Environment(toolbox, pMgr);
        this.stateKey = environment.getEncodedGameState(pMgr);
        decisionHandler.updateDecisionArray();
        if (!decisionHandler.hasOptions()) {

        }
        int moveChoice = getMoveChoice();
        this.currentQ = getQValue(stateKey, moveChoice);
        decisionHandler.fulfillDecision(environment, moveChoice);
        environment.generateStatePrime();
        updateRho();
        calculateMaxQPrime();
        updateQValue(moveChoice);
    }

    private double getQValue(String stateKey, int moveChoice) {
        if (qTableMgr.isWithinSize(stateKey, moveChoice)) {
            return qTableMgr.getQValue(stateKey, moveChoice);
        } else {
            return 0.0;
        }
    }

    private int getMoveChoice() {
        if (Math.random() < EPSILON) {
            return explore();
        } else {
            return exploit();
        }
    }

    private int exploit() {
        return qTableMgr.getMaxQIndex(stateKey);
    }

    private int explore() {
        return new Random().nextInt(decisionHandler.getNumDecisions());
    }

    public void updateRho() {
        this.RHO = decisionHandler.getReward(environment);
    }

    private void calculateMaxQPrime() {
        String statePrimeKey = environment.getEncodedGameState(pMgr);
        int maxQPrimeIndex = qTableMgr.getMaxQIndex(statePrimeKey);
        this.maxQPrime = getQValue(statePrimeKey, maxQPrimeIndex);
    }

    private void updateQValue(int moveChoice) {
        double updatedQ = 2 * currentQ + ALPHA * (RHO + GAMMA * maxQPrime - currentQ);
        System.out.println("Q inserted back into hashmap: " + updatedQ);
        qTableMgr.setQValue(stateKey, moveChoice, updatedQ);
    }

    public void finalizeQTableUpdate() {
        qTableMgr.updateQData();
    }

    private void decayEpsilon() {
        // logic for increasing the exploitation rate over time
    }

    public void printQueue() {
        toolbox.printQueue(pMgr);
    }
}
