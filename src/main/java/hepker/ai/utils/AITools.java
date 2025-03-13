package hepker.ai.utils;

import hepker.game.entity.movement.ActionNode;
import hepker.game.gameworld.PieceManager;
import hepker.game.entity.GameBoardPiece;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public static int pieceToInt(GameBoardPiece piece) {
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

    /**
     * Processes and returns a String representing the checkerboard's gamestate
     * @param inputPMgr Necessary for evaluating pieces
     * @return String encoding of the gamestate
     */
    public static String getByteEncryptedStateString(PieceManager inputPMgr) {
        return intArrTo64Encoding(getStateArray(inputPMgr));
    }

    /**
     * Encodes the game state into a Base64 string for storage.
     * For LIGHT, reverses the array and swaps pieces; for DUSKY, uses as-is.
     * Each square (0-4) is 3 bits, packed into 96 bits.
     * @param gameState 32-element array (0=empty, 1=LIGHTChecker, etc.)
     * @return Base64-encoded string
     */
    private static String intArrTo64Encoding(int[] gameState) {
        if (gameState == null) {
            LOGGER.error("Null gameState");
            throw new IllegalArgumentException("Null gameState");
        } else if (gameState.length != 64) {
            LOGGER.error(String.format("Invalid array length: %d", gameState.length));
            throw new IllegalArgumentException();
        }
        byte[] bytes = new byte[24];
        int byteIndex = 0;
        long accumulator = 0;
        int bitCount = 0;
        for (int i = 0; i < 64; i++) {
            int value = gameState[i];
            if (value < 0 || value > 4) {
                throw new IllegalArgumentException("Invalid value at index " + i + ": " + value);
            }
            accumulator = (accumulator << 3) | (value & 0x07); // Mask to 3 bits
            bitCount += 3;
            if (bitCount >= 8) {
                int shift = bitCount - 8;
                bytes[byteIndex] = (byte) ((accumulator >> shift) & 0xFF);
                byteIndex++;
                bitCount -= 8;
                accumulator = accumulator & ((1L << bitCount) - 1); // Keep remaining bits
            }
        }
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Helper function for encoding the board's pieces into an int[]
     * @param inputPMgr pMgr required for evaluating pieces on the board
     * @return gameState An int[] representing every piece in play
     */
    private static int[] getStateArray(PieceManager inputPMgr) {
        int[] gameState = new int[64];
        for (int j = 0; j < 8; ++j) {
            for (int i = 0; i < 8; ++i) {
                gameState[j * 8 + i] = AITools.pieceToInt(inputPMgr.getPiece(i, j));
            }
        }
        return gameState;
    }
}
