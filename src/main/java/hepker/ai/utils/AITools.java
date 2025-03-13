package hepker.ai.utils;

import hepker.game.entity.movement.ActionNode;
import hepker.game.gameworld.PieceManager;
import hepker.game.entity.GameBoardPiece;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;

/**
 * STATE REPRESENTATION: Hexadecimal String
 * ACTION REPRESENTATION: Organize the priority queue by
 * (x,y); prioritizing x and then y
 * Q-TABLE: Must export Q-values for each state-action pair
 * WIN-LOSS-DRAW: Result of the game
 * AVERAGE REWARD PER-GAME: Average points
 */
public final class AITools {
    private static final Logger LOGGER = LoggerFactory.getLogger(AITools.class);

    private AITools() {

    }

    /**
     * Streams an ActionNode[]. Array is sorted first by x then by y for consistent decision making
     * @param pMgr Required for getPieces
     * @param inputColor Verifying the piece is movable by this AI
     * @return ActionNode[] for use by AI
     */
    public static ActionNode[] getDecisionArray(PieceManager pMgr, GameBoardPiece.PieceColor inputColor) {
        return Arrays.stream(pMgr.getPiecesContainer())
                .filter(piece -> piece != null && piece.getColor() == inputColor)
                .flatMap(GameBoardPiece::getMoveListAsStream)
                .sorted(Comparator.comparingInt(ActionNode::getoDataX).thenComparing(ActionNode::getoDataY))
                .toArray(ActionNode[]::new);
    }

    public static int getNumOpponentOptions(PieceManager pMgr, GameBoardPiece.PieceColor inputColor) {
        return Arrays.stream(pMgr.getPiecesContainer())
                .filter(piece -> piece != null && piece.getColor() != inputColor)
                .flatMap(GameBoardPiece::getMoveListAsStream)
                .toArray()
                .length;
    }

    public static int getMaximumOpponentReward(PieceManager pMgr, GameBoardPiece.PieceColor inputColor) {
        return Arrays.stream(pMgr.getPiecesContainer())
                .filter(piece -> piece != null && piece.getColor() != inputColor)
                .flatMap(GameBoardPiece::getMoveListAsStream)
                .mapToInt(ActionNode::getReward)
                .max()
                .orElse(0);
    }

    /**
     * Processes and returns a String representing the checkerboard's gamestate
     * @param inputPMgr Necessary for evaluating pieces
     * @return String encoding of the gamestate
     */
    public static String getByteEncryptedStateString(PieceManager inputPMgr, boolean isDusky) {
        return intArrTo64Encoding(getStateArray(inputPMgr, isDusky));
    }

    /**
     * Base 64 encodes an int[] to a String representation
     *
     * @param gameState 65-element int[] representing the gamestate
     * @return Base64-encoded string
     */
    private static String intArrTo64Encoding(int[] gameState) {
        if (gameState == null) {
            LOGGER.error("Null gameState");
            throw new IllegalArgumentException("Null gameState");
        } else if (gameState.length != 65) {
            LOGGER.error(String.format("Invalid array length: %d", gameState.length));
            throw new IllegalArgumentException();
        }
        ByteBuffer buffer = ByteBuffer.allocate(gameState.length * 4);
        for (int element : gameState) {
            buffer.putInt(element);
        }
        return Base64.getEncoder().encodeToString(buffer.array());
    }

    /**
     * Helper function for encoding the board's pieces into an int[]
     *
     * @param inputPMgr pMgr required for evaluating pieces on the board
     * @return gameState An int[] representing every piece in play
     */
    private static int[] getStateArray(PieceManager inputPMgr, boolean isDusky) {
        int[] gameState = new int[65];
        gameState[gameState.length - 1] = isDusky ? 2 : 1;
        for (int j = 0; j < 8; ++j) {
            for (int i = 0; i < 8; ++i) {
                gameState[j * 8 + i] = AITools.pieceToInt(inputPMgr.getPiece(i, j));
            }
        }
        return gameState;
    }

    /**
     * Helper that changes Pieces into int
     *
     * @param piece The given piece we are sconverting
     * @return an int representing the piece
     */
    private static int pieceToInt(GameBoardPiece piece) {
        if (piece == null) {
            return 0;
        }
        try {
            return switch (piece.getName()) {
                case "LIGHTChecker" -> 3;
                case "DUSKYChecker" -> 4;
                case "LIGHTCheckerKing" -> 5;
                case "DUSKYCheckerKing" -> 6;
                default -> throw new IllegalArgumentException("Invalid piece name: " + piece.getName());
            };
        } catch (IllegalArgumentException e) {
            LOGGER.error("Illegal argument", e);
            return 0;
        }
    }
}
