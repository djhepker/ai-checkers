package hepker.ai.agentintegration;

import hepker.game.entity.movement.ActionNode;
import hepker.game.gameworld.PieceManager;
import hepker.game.entity.GameBoardPiece;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
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
     * Sigmoid rounding function which returns a result (0.0,max). Makes data
     * more readable when working with large datasets
     *
     * @param x The value we will smooth on a scale of (0.0,max)
     * @param xNaught The value which will be the center of the graph. Return value will be 0.5
     *                when x = xNaught
     * @param max The upper asymptote of the return value
     * @param k Scalar rate of change of the slope. A higher steepness (k) makes the rate of change slower,
     *          and the resulting graph appears wider (stretched). As (k) becomes smaller, function becomes
     *          more sensitive to lower negative inputs
     * @return A value (0.0,max) that accurately represents the input
     */
    public static double getSigmoid(double x, double xNaught, double max, double k) {
        return max / (1 + Math.exp(-k * (x - xNaught)));
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
    public static String getEncryptedGameStateString(PieceManager inputPMgr, boolean isDusky) {
        return encodeIntArrToString(getStateArray(inputPMgr, isDusky));
    }

    /**
     * Encodes an int[] to a String representation
     *
     * @param gameState 65-element int[] representing the gamestate
     * @return String of gameboard pieces where empty spaces are shifted into their ASCII counterparts
     */
    private static String encodeIntArrToString(int[] gameState) {
        if (gameState == null) {
            LOGGER.error("Null gameState");
            throw new IllegalArgumentException("Null gameState");
        } else if (gameState.length != 65) {
            LOGGER.error(String.format("Invalid array length: %d", gameState.length));
            throw new IllegalArgumentException();
        }
        StringBuilder stateKeyBuilder = new StringBuilder();
        int numberOfOpenSpaces = 0;
        for (int element : gameState) {
            if (element == 0) {
                ++numberOfOpenSpaces;
            } else if (numberOfOpenSpaces > 0) {
                char compressedNullSpaces = (char) (31 + numberOfOpenSpaces); // Board is never larger than ascii
                stateKeyBuilder.append(compressedNullSpaces)
                        .append(element);
                numberOfOpenSpaces = 0;
            } else {
                stateKeyBuilder.append(element);
            }
        }
        if (numberOfOpenSpaces > 0) {
            char compressedNullSpaces = (char) (31 + numberOfOpenSpaces);
            stateKeyBuilder.append(compressedNullSpaces);
        }
        return stateKeyBuilder.toString();
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
     * @param piece The given piece we are converting
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
