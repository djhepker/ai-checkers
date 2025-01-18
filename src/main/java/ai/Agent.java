package main.java.ai;

import main.java.game.utils.GameBoardPiece;

public class Agent {
    private GameBoardPiece[][] pieces;

    public Agent (GameBoardPiece[][] pieces) {
        this.pieces = pieces;
    }

    private void chooseAction() {}

    // random moves
    private void explore() {}

    // choosing which move is the most appropriate based on past experiences
    private void exploit() {}

    // after an action is taken, the reward is calculated based on the change in game state
    double calculateReward() { return 0.0; }

    private void updateQValue() {}

    private void getAvailableMoves() {}

    private void decayEpsilon() {}

    double getQValue() { return 0.0; }

    double calculateReward(GameBoardPiece piece) { return 0.0; }
}
