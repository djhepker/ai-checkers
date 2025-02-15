package main.java.ai.utils;

import main.java.game.entity.movement.ActionNode;
import main.java.game.gameworld.PieceManager;
import main.java.game.entity.GameBoardPiece;

import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;

import static main.java.game.entity.GameBoardPiece.PieceColor.DUSKY;
import static main.java.game.entity.GameBoardPiece.PieceColor.LIGHT;

/*
* STATE REPRESENTATION: Hexadecimal String
* ACTION REPRESENTATION: Organize the priority queue by
* (x,y); prioritizing x and then y
* Q-TABLE: Must export Q-values for each state-action pair
* WIN-LOSS-DRAW: Result of the game
* AVERAGE REWARD PER-GAME: Average points
* */

public final class AITools {
    private final GameBoardPiece.PieceColor pieceColor;

    public AITools(boolean isDusky) {
        this.pieceColor = isDusky ? DUSKY : LIGHT;
    }

    public void printQueue(PieceManager pMgr) {
        ActionNode[] printable = getDecisionArray(pMgr);
        for (ActionNode node : printable) {
            node.printData();
        }
    }

    public ActionNode[] getDecisionArray(PieceManager pMgr) {
        return Arrays.stream(pMgr.getPieces())
                .filter(piece -> piece != null && piece.getColor() == pieceColor)
                .flatMap(GameBoardPiece::getMoveListAsStream)
                .sorted(Comparator.comparingInt(ActionNode::getoDataX).thenComparing(ActionNode::getoDataY))
                .toArray(ActionNode[]::new);
    }

    public int getNumOpponentOptions(PieceManager pMgr) {
        return Arrays.stream(pMgr.getPieces())
                .filter(piece -> piece != null && piece.getColor() != pieceColor)
                .flatMap(GameBoardPiece::getMoveListAsStream)
                .toArray()
                .length;
    }

    public String getBase64EncodingOfArr(int[] gameState) {
        byte[] byteArray = new byte[gameState.length * Integer.BYTES];
        for (int i = 0; i < gameState.length; i++) {
            // Convert each int to a 4-byte array and copy it to the byte array
            byteArray[i * 4] = (byte) (gameState[i] >> 24);
            byteArray[i * 4 + 1] = (byte) (gameState[i] >> 16);
            byteArray[i * 4 + 2] = (byte) (gameState[i] >> 8);
            byteArray[i * 4 + 3] = (byte) gameState[i];
        }
        return Base64.getEncoder().encodeToString(byteArray);
    }

    public int getMaximumOpponentReward(PieceManager pMgr) {
        return Arrays.stream(pMgr.getPieces())
                .filter(piece -> piece != null && piece.getColor() != pieceColor)
                .flatMap(GameBoardPiece::getMoveListAsStream)
                .mapToInt(ActionNode::getReward)
                .max()
                .orElse(0);
    }

    public int pieceToInt(GameBoardPiece piece) {
        if (piece == null) {
            return 0;
        }
        int colorSign = pieceColor == DUSKY ? 1 : -1;
        try {
            return switch (piece.getName()) {
                case "LIGHTChecker" -> -1 * colorSign;
                case "DUSKYChecker" -> colorSign;
                case "LIGHTCheckerKing" -> -2 * colorSign;
                case "DUSKYCheckerKing" -> 2 * colorSign;
                default -> throw new IllegalArgumentException("Invalid piece name: " + piece.getName());
            };
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
