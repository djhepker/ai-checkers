package main.java.ai;

import main.java.game.utils.GameBoardPiece;

public class Agent {
    private GameBoardPiece[][] pieces;

    /*
    * Discounting Factor for Future Rewards. Future rewards are less valuable
    * than current rewards so they must be discounted. Since Q-value is an
    * estimation of expected rewards from a state, discounting rule applies here as well
    * */
    private double gamma;
    // instantaneous reward the agent received for taking action a from state s
    private double rInstant;
    // Q(s,a) Q-value for action a given state s
    // TODO: map states s for the above
    private double qValue;
    // max(a') [ Q(s',a') ], maximum qValue for the next state, s', representing the best possible future outcome
    private double maxQ;
    // learning rate
    private final double alpha = 0.84;
    // greed policy
    private final double epselon = 0.0;

    public Agent (GameBoardPiece[][] pieces) {
        this.pieces = pieces;
    }

    private void chooseAction() {}

    // random moves
    private void explore() {}

    // choosing which move is the most appropriate based on past experiences
    private void exploit() {}

    /*
    * After an action is taken, the reward is calculated based on the change in game state
    * Positive reward for a better position
    * Negative reward for a worse position
    * A large positive reward should be awarded if the Agent wins the game
    * */
    double calculateReward() { return 0.0; }

    private void updateQValue() {}

    private void getAvailableMoves() {}

    private void decayEpsilon() {}

    double getQValue() { return 0.0; }

    double calculateReward(GameBoardPiece piece) { return 0.0; }
}
