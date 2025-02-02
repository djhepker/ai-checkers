package main.java.ai;

import main.java.ai.environment.AgentDecisionHandler;
import main.java.ai.environment.Environment;
import main.java.ai.utils.AgentTools;
import main.java.ai.utils.QTableManager;
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

public class Agent extends NPC {
    private final boolean isDusky;

    private AgentTools toolbox;
    private QTableManager qTableMgr;
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
        this.currentQ = 0.0;
        this.maxQPrime = 0.0;
        this.RHO = 0.0;
    }

    public void update() {
        AgentDecisionHandler decisionHandler = new AgentDecisionHandler(pMgr, toolbox);
        decisionHandler.updateDecisionArray(); // loads movelist into decisionhandler *
        int moveChoice = getMoveChoice(decisionHandler);   // random move generated *

        Environment environment = new Environment(toolbox, pMgr); // not needed
        this.stateKey = environment.getEncodedGameState(pMgr); // not needed


        this.currentQ = getQValue(stateKey, moveChoice);    // not needed; requires movechoice
        decisionHandler.calculateDecisionReward(environment, moveChoice); // not needed; requires movechoice

        decisionHandler.movePiece(moveChoice);  // piece moved *


        environment.updateStatePrime(); // not needed; requires piece be moved
        updateRho(environment, decisionHandler);
        calculateMaxQPrime(environment);
        updateQValue(moveChoice);
    }

    private int getMoveChoice(AgentDecisionHandler handler) {
        if (Math.random() < EPSILON) {
            return explore(handler);
        } else {
            return exploit();
        }
    }

    private double getQValue(String stateKey, int moveChoice) {
        if (qTableMgr.isWithinSize(stateKey, moveChoice)) {
            return qTableMgr.getQValue(stateKey, moveChoice);
        } else {
            return 0.0;
        }
    }

    private int exploit() {
        return qTableMgr.getMaxQIndex(stateKey);
    }

    private int explore(AgentDecisionHandler handler) {
        return new Random().nextInt(handler.getNumDecisions());
    }

    public void updateRho(Environment env, AgentDecisionHandler handler) {
        this.RHO = handler.getDecisionReward(env);
    }

    private void calculateMaxQPrime(Environment env) {
        String statePrimeKey = env.getEncodedGameState(pMgr);
        int maxQPrimeIndex = qTableMgr.getMaxQIndex(statePrimeKey);
        this.maxQPrime = getQValue(statePrimeKey, maxQPrimeIndex);
    }

    private void updateQValue(int moveChoice) {
        double updatedQ = 2 * currentQ + ALPHA * (RHO + GAMMA * maxQPrime - currentQ);
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
