package main.java.ai;

import main.java.game.entity.movement.ActionNode;
import main.java.game.utils.GameBoardPiece;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

import static main.java.game.utils.GameBoardPiece.PieceColor.DUSKY;
import static main.java.game.utils.GameBoardPiece.PieceColor.LIGHT;

/*
* STATE REPRESENTATION: Hexadecimal String
* ACTION REPRESENTATION: Organize the priority queue by
* (x,y); prioritizing x and then y
* Q-TABLE: Must export Q-values for each state-action pair
* WIN-LOSS-DRAW: Result of the game
* AVERAGE REWARD PER-GAME: Average points
* */

class AgentTools {
    private final GameBoardPiece[][] pieces;
    private final GameBoardPiece.PieceColor pieceColor;

    public AgentTools(GameBoardPiece[][] pieces, boolean isDusky) {
        this.pieces = pieces;
        this.pieceColor = isDusky ? DUSKY : LIGHT;
    }

    public void printQueue() {
        ActionNode[] printable = getActionsArray();
        for (ActionNode node : printable) {
            node.printData();
        }
    }

    public ActionNode[] getActionsArray() {
        return Arrays.stream(pieces)
                .flatMap(Arrays::stream)
                .filter(piece -> piece != null && piece.getColor() == pieceColor)
                .flatMap(GameBoardPiece::getMoveListAsStream)
                .sorted(Comparator.comparingInt(ActionNode::getoDataX).thenComparing(ActionNode::getoDataY))
                .toArray(ActionNode[]::new);
    }

    public String getEncodedGameState() {
        int[] arrState = getIntArrState();
        String encodedState = getHexadecimalEncodingOfArr(arrState);
        return encodedState;
    }

    private int[] getIntArrState() {
        int[] gameState = new int[64];
        for (int j = 0; j < 8; ++j) {
            for (int i = 0; i < 8; ++i) {
                gameState[j * 8 + i] = pieceToInt(pieces[j][i]);
            }
        }
        return gameState;
    }

    private String getHexadecimalEncodingOfArr(int[] gameState) {
        String encodedState = Arrays.stream(gameState)
                .mapToObj(Integer::toHexString)
                .collect(Collectors.joining());
        return encodedState;
    }

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
