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
        StringBuilder stateBuilder = new StringBuilder();
        if (pieceColor == LIGHT) {
            for (int i = gameState.length - 1; i >= 0; --i) {
                int swapped = getSwapped(gameState[i]);
                stateBuilder.append(swapped);
            }
        } else {
            for (int val : gameState) {
                stateBuilder.append(val);
            }
        }
        return new BigInteger(stateBuilder.toString()).toString(16);
    }

    private int getSwapped(int gameState) {
        int val = gameState;
        int swapped = switch (val) {
            case 0 -> 0;         // empty stays empty
            case 1 -> 3;         // LIGHTChecker → DUSKYChecker
            case 2 -> 4;         // LIGHTCheckerKing → DUSKYCheckerKing
            case 3 -> 1;         // DUSKYChecker → LIGHTChecker
            case 4 -> 2;         // DUSKYCheckerKing → LIGHTCheckerKing
            default -> throw new IllegalArgumentException("Invalid gameState value: " + val);
        };
        return swapped;
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
                case "LIGHTCheckerKing" -> 2;
                case "DUSKYChecker" -> 3;
                case "DUSKYCheckerKing" -> 4;
                default -> throw new IllegalArgumentException("Invalid piece name: " + piece.getName());
            };
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
