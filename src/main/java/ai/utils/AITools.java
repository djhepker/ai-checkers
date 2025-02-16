package main.java.ai.utils;

import main.java.game.entity.movement.ActionNode;
import main.java.game.gameworld.PieceManager;
import main.java.game.entity.GameBoardPiece;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

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

    public boolean isDusky() {
        return pieceColor == DUSKY;
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

    public String getHexadecimalEncodingOfArr(int[] gameState) {
        return new BigInteger(Arrays.stream(gameState)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining()))
                .toString(16);
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
        try {
            return switch (piece.getName()) {
                case "LIGHTChecker" -> 1;
                case "DUSKYChecker" -> 3;
                case "LIGHTCheckerKing" -> 2;
                case "DUSKYCheckerKing" -> 4;
                default -> throw new IllegalArgumentException("Invalid piece name: " + piece.getName());
            };
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
