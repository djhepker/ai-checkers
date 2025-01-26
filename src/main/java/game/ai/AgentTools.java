package main.java.game.ai;

import main.java.game.entity.movement.ActionNode;
import main.java.game.gameworld.PieceManager;
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
    private final GameBoardPiece.PieceColor pieceColor;

    public AgentTools(boolean isDusky) {
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
                .flatMap(Arrays::stream)
                .filter(piece -> piece != null && piece.getColor() == pieceColor)
                .flatMap(GameBoardPiece::getMoveListAsStream)
                .sorted(Comparator.comparingInt(ActionNode::getoDataX).thenComparing(ActionNode::getoDataY))
                .toArray(ActionNode[]::new);
    }

    public String getHexadecimalEncodingOfArr(int[] gameState) {
        String encodedState = Arrays.stream(gameState)
                .mapToObj(Integer::toHexString)
                .collect(Collectors.joining());
        return encodedState;
    }

    public int getMaximumOpponentReward(PieceManager pMgr) {
        return Arrays.stream(pMgr.getPieces())
                .flatMap(Arrays::stream)
                .filter(piece -> piece != null && piece.getColor() != pieceColor)
                .flatMap(GameBoardPiece::getMoveListAsStream)
                .mapToInt(ActionNode::getReward)
                .max()
                .orElse(0);
    }

    public int getNumOpponentOptions(PieceManager pMgr) {
        return Arrays.stream(pMgr.getPieces())
                .flatMap(Arrays::stream)
                .filter(piece -> piece != null && piece.getColor() != pieceColor)
                .flatMap(GameBoardPiece::getMoveListAsStream)
                .toArray()
                .length;
    }

    /*
    * TODO: logic for kingchecker mapping
    *
    * TODO: dynamic color logic
    * */
    public int pieceToInt(GameBoardPiece piece) {
        if (piece == null) {
            return 0;
        }
        int colorSign = pieceColor == DUSKY ? 1 : -1;
        try {
            return switch (piece.getName()) {
                case "LIGHTChecker" -> -1 * colorSign;
                case "DUSKYChecker" -> colorSign;
                default -> throw new IllegalArgumentException("Invalid input: " + piece.getName());
            };
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.exit(0);
            return 0;
        }
    }
}
