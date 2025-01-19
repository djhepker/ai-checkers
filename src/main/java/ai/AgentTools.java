package main.java.ai;

import main.java.game.entity.movement.ActionNode;
import main.java.game.utils.GameBoardPiece;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import static main.java.game.utils.GameBoardPiece.PieceColor.DUSKY;
import static main.java.game.utils.GameBoardPiece.PieceColor.LIGHT;

/*
* STATE REPRESENTATION: int[][]
* maybe more compact like a feature vector or reduction
* ACTION REPRESENTATION: Organize the priority queue by
* (x,y); prioritizing x and then y
* Q-TABLE: Must export Q-values for each state-action pair
* WIN-LOSS-DRAW: Result of the game
* AVERGAE REWARD PER-GAME: Average points
* */

class AgentTools {
    private final GameBoardPiece[][] pieces;
    private final GameBoardPiece.PieceColor pieceColor;
    private PriorityQueue<ActionNode> actionQueue;

    public AgentTools(GameBoardPiece[][] pieces, boolean isDusky) {
        this.pieces = pieces;
        this.pieceColor = isDusky ? DUSKY : LIGHT;
        this.actionQueue = getQueueOfActions();
    }

    public void printQueue() {
        ActionNode[] printable = getQueueOfActions().toArray(new ActionNode[0]);
        for (ActionNode node : printable) {
            node.printData();
        }
    }

    public PriorityQueue<ActionNode> getQueueOfActions() {
        return Arrays.stream(pieces)
                .flatMap(Arrays::stream)
                .filter(piece -> piece != null && piece.getColor() == pieceColor)
                .flatMap(GameBoardPiece::getMoveListAsStream)
                .collect(Collectors.toCollection(() -> new PriorityQueue<>(
                        (a, b) -> Integer.compare(b.getReward(), a.getReward()))));
    }

    public int[] getIntArrState() {
        int[] gameState = new int[64];
        for (int j = 0; j < 8; ++j) {
            for (int i = 0; i < 8; ++i) {
                gameState[j * 8 + i] = pieceToInt(pieces[j][i]);
            }
        }
        return gameState;
    }

    public long encodedLong

    private int pieceToInt(GameBoardPiece piece) {
        if (piece == null) {
            return 0;
        }
        try {
            switch (piece.getName()) {
                case "LIGHTChecker":
                    return 1;
                case "DUSKYChecker":
                    return -1;
                default:
                    throw new IllegalArgumentException("Invalid input: " + piece.getName());
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.exit(0);
            return 0;
        }
    }
}
