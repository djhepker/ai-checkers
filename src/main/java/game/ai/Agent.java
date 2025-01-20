package main.java.game.ai;

import main.java.game.gameworld.PieceManager;
import main.java.game.utils.GameBoardPiece;

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
    private AgentChoiceHandler choiceHandler;
    private PieceManager pMgr;

    /*
    * Discounting Factor for Future Rewards. Future rewards are less valuable
    * than current rewards so they must be discounted. Since Q-value is an
    * estimation of expected rewards from a state, discounting rule applies here as well
    * */
    private final double GAMMA = 0.75;
    // learning rate
    private final double ALPHA = 0.84;
    /*
    * greed policy, exploitation P(max) = 1 - epselon -> optimal decision
    * In this instance of exploitation, the agent chooses
    * the course of action that, given its current understanding, it feels is optimal
    * epselon = exploration chance
    */
    private final double EPSELON = 0.92;
    // may or may not need or use sig
    private double SIGMA = 0.0;
    // instantaneous reward the agent received for taking action "a" from state "S"
    private double RHO;
    /*
     * Q(s,a) Q-value for action a given state
     * TODO: map states s for the above
     * */
    private double currentQ;
    // max(a') [ Q(s',a') ], maximum qValue for the next state, s', representing the best possible future outcome
    private double maxQ;
    private double maxQPrime;

    private double[] qValues;
    private String stateKey;

    public Agent (PieceManager pMgr, boolean playerLight) {
        this.isDusky = playerLight;
        this.pMgr = pMgr;
        this.toolbox = new AgentTools(isDusky);
        this.qTableMgr = new QTableManager(toolbox);
        this.choiceHandler = new AgentChoiceHandler(pMgr, toolbox);
        this.currentQ = 0.0;
        this.maxQ = 0.0;
        this.maxQPrime = 0.0;
    }

    public void update() {
        this.stateKey = toolbox.getEncodedGameState(pMgr);
        this.qValues = qTableMgr.getQValuesOfState(stateKey);
        updateMaxQ(stateKey);
        int moveChoice = getMoveChoice();
        /*
        * TODO: calculate maxQPrime after AI makes a move decision
        *  need to create a controller class for the AI to manipulate
        *  game world
        * */
        calculateMaxQPrime();
        updateQValue(moveChoice);
    }

    private int getMoveChoice() {
        if (Math.random() < EPSELON) {
            return explore();
        } else {
            return exploit();
        }
    }

    /*
     * After an action is taken, the reward is calculated based on the change in game state
     * Positive reward for a better position
     * Negative reward for a worse position
     * A large positive reward should be awarded if the Agent wins the game
     * */
    double calculateReward() {
        return 0.0;
    }

    public double getQValue() {
        return 0.0;
    }

    public void updateMaxQ(String stringKey) {
        this.maxQ = qTableMgr.getMaxQOfState(stringKey);
    }

    double calculateReward(GameBoardPiece piece) {
        return 0.0;
    }

    // random moves
    private int explore() {
        return new Random().nextInt(qTableMgr.getTableSize());
    }

    // choosing which move is the most appropriate based on past experience
    private int exploit() {
        for (int i = 0; i < qValues.length; ++i) {
            if (qValues[i] == maxQ) {
                return i;
            }
        }
        System.out.println("Max not found, random default is being returned");
        return explore();
    }

    private void calculateMaxQPrime() {

    }

    private void updateQValue(int moveChoice) {
        double nextQMax = 0.0;
        qValues[moveChoice]+= ALPHA * (RHO + GAMMA * nextQMax - qValues[moveChoice]);
    }

    private void getAvailableMoves() {}

    private void decayEpsilon() {}

    public void printQueue() {
        toolbox.printQueue(pMgr);
    }
}
