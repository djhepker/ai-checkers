package main.java.ai;

import main.java.game.entity.movement.ActionNode;
import main.java.game.utils.GameBoardPiece;

import java.util.PriorityQueue;

/*
* STATE: Will be identified as pieces[][]; Each state represents a board configuration
* ACTION: Moving pieces when it is Agent's turn
* REWARD: Positive & Negative
* EPISODE: GameEngine calls updateGame()
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
    private GameBoardPiece[][] pieces;
    private AgentTools actions;

    /*
    * Discounting Factor for Future Rewards. Future rewards are less valuable
    * than current rewards so they must be discounted. Since Q-value is an
    * estimation of expected rewards from a state, discounting rule applies here as well
    * */
    private final double GAMMA = 0.80;
    // instantaneous reward the agent received for taking action "a" from state "S"
    private double rInstant;
    /*
    * Q(s,a) Q-value for action a given state
    * TODO: map states s for the above
    * */
    private double qValue;
    // max(a') [ Q(s',a') ], maximum qValue for the next state, s', representing the best possible future outcome
    private double maxQ;
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
    private double sig = 0.0;

    public Agent (GameBoardPiece[][] pieces, boolean playerLight) {
        this.isDusky = playerLight;
        this.pieces = pieces;
    }

    public void update() {
        this.actions = new AgentTools(pieces, isDusky);
        PriorityQueue<ActionNode> queue = actions.getQueueOfActions();

    }

    private AgentTools chooseAction() {
        if (Math.random() < EPSELON) {
            return explore();
        } else {
            return exploit();
        }
    }

    // random moves
    private AgentTools explore() {
        return null;
    }

    // choosing which move is the most appropriate based on past experiences
    private AgentTools exploit() {
        return null;
    }

    /*
    * After an action is taken, the reward is calculated based on the change in game state
    * Positive reward for a better position
    * Negative reward for a worse position
    * A large positive reward should be awarded if the Agent wins the game
    * */
    double calculateReward() { return 0.0; }

    /*
    * Updates our Q value to its newest value
    * Q = Q(s,a) + alpha*( R(s,a) + gamma*Q(s',a') - Q(s,a) )
    * */
    private void updateQValue() {
        qValue = qValue + ALPHA * (rInstant + GAMMA * maxQ - qValue);
    }

    private void getAvailableMoves() {}

    private void decayEpsilon() {}

    double getQValue() {
        return 0.0;
    }

    double getMaxQ() {
        // calculates maximum Q value of all possible actions in next state, a''
        return 0.0;
    }

    double calculateReward(GameBoardPiece piece) {
        return 0.0;
    }

    public void printQueue() {
        actions = new AgentTools(pieces, isDusky);
        actions.printQueue();
    }
}
