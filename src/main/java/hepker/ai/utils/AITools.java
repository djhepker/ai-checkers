package hepker.ai.utils;

import hepker.game.entity.movement.ActionNode;
import hepker.game.gameworld.PieceManager;
import hepker.game.entity.GameBoardPiece;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;

import static hepker.game.entity.GameBoardPiece.PieceColor.DUSKY;
import static hepker.game.entity.GameBoardPiece.PieceColor.LIGHT;

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
        StringBuilder sb = new StringBuilder();
        for (int i : gameState) {
            sb.append(i);
        }
        return new BigInteger(sb.toString()).toString(16);
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
