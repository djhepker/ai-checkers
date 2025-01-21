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
    private double SIGMA = 0.0;
    // instantaneous reward the agent received for taking action "a" from state "S"
    private double RHO;
    private double currentQ;
    private double maxQPrime;

    private String stateKey;

    public Agent (PieceManager pMgr, boolean playerLight) {
        this.isDusky = playerLight;
        this.pMgr = pMgr;
        this.toolbox = new AgentTools(isDusky);
        this.qTableMgr = new QTableManager(toolbox);
        this.decisionHandler = new AgentDecisionHandler(pMgr, toolbox);
        this.currentQ = 0.0;
        this.maxQPrime = 0.0;
        this.RHO = 0.0;
    }

    public void update() {
        this.environment = new Environment(toolbox, pMgr);
        this.stateKey = environment.getEncodedGameState(pMgr);
        qTableMgr.updateQValues(stateKey);
        decisionHandler.updateDecisionArray();
        int moveChoice = getMoveChoice();
        this.currentQ = qTableMgr.getQValue(moveChoice);
        decisionHandler.fulfillDecision(environment, moveChoice);
        environment.generateStatePrime();
        updateRho();
        calculateMaxQPrime();
        updateQValue(moveChoice); // progress flag * *
        // store result
    }

    private int getMoveChoice() {
        if (Math.random() < EPSILON) {
            return explore();
        } else {
            return exploit();
        }
    }

    private int exploit() {
        return qTableMgr.getMaxQIndex();
    }

    // random moves
    private int explore() {
        return new Random().nextInt(decisionHandler.getNumDecisions());
    }

    /*
     * After an action is taken, the reward is calculated based on the change in game state
     * Positive reward for a better position
     * Negative reward for a worse position
     * A large positive reward should be awarded if the Agent wins the game
     * */
    public void updateRho() {
        // should base it off of the number of possible actions the AI has vs player has
        this.RHO = decisionHandler.getReward(environment);
    }

    public double getQValue() {
        return 0.0;
    }

    private void calculateMaxQPrime() {
        String statePrime = environment.getEncodedGameState(pMgr);

        maxQPrime = qTableMgr.getQValue(qTableMgr.getMaxQIndex());
    }

    private void updateQValue(int moveChoice) {
        double nextQMax = 0.0;
        // this will grow over time; may need to research;
        //qValues[moveChoice] += ALPHA * (RHO + GAMMA * nextQMax - qValues[moveChoice]);

    }

    private void getAvailableMoves() {

    }

    private void decayEpsilon() {

    }

    public void printQueue() {
        toolbox.printQueue(pMgr);
    }
}
