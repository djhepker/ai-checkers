package hepker.ai.ai;

import hepker.ai.environment.AIDecisionHandler;
import hepker.ai.environment.Environment;
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

public class Agent implements AI {
    private final boolean isDusky;

    private AITools toolbox;
    private DataManager qTableMgr;
    private PieceManager pMgr;

    private final double GAMMA = 0.9; // value of knowledge
    private final double ALPHA = 0.82; // learning rate
    private final double EPSILON = 0.8; // exploration rate
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
    }

    public void update() {
        Environment environment = new Environment(toolbox, pMgr);
        AIDecisionHandler decisionHandler = new AIDecisionHandler(pMgr, toolbox, environment);
        this.stateKey = environment.getEncodedGameState(pMgr);
        decisionHandler.updateDecisionArray();
        int numDecisions = decisionHandler.getNumDecisions();
        if (numDecisions == 0) {
            pMgr.flagGameOver();
            return;
        }
        int moveChoice = getActionInt(numDecisions);
        this.currentQ = getQValue(stateKey, moveChoice);
        decisionHandler.calculateDecisionReward(moveChoice);
        decisionHandler.movePiece(moveChoice);
        decisionHandler.updateDecisionArray();
        environment.generateStatePrime();
        updateRho(decisionHandler);
        calculateMaxQPrime(environment);
        updateQValue(moveChoice);
    }

    public int getActionInt(int numDecisions) {
        if (Math.random() < EPSILON) {
            return explore(numDecisions);
        } else {
            return exploit();
        }
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

    private void updateRho(AIDecisionHandler handler) {
        this.RHO = handler.getDecisionReward();
    }

    private void calculateMaxQPrime(Environment env) {
        this.maxQPrime = qTableMgr.getMaxQValue(env.getEncodedGameState(pMgr));
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
