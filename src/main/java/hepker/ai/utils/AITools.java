package hepker.ai.utils;

import hepker.game.entity.movement.ActionNode;
import hepker.game.gameworld.PieceManager;
import hepker.game.entity.GameBoardPiece;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;

import static hepker.game.entity.GameBoardPiece.PieceColor.DUSKY;
import static hepker.game.entity.GameBoardPiece.PieceColor.LIGHT;

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

    private final GameBoardPiece.PieceColor pieceColor;

    public AITools(boolean isDusky) {
        this.pieceColor = isDusky ? DUSKY : LIGHT;
    }

    public boolean isDusky() {
        return pieceColor == DUSKY;
    }

    // TODO: just flip the pmgr board before updating the pieces?
    public ActionNode[] getDecisionArray(PieceManager pMgr) { // verified as optimal
        return Arrays.stream(pMgr.getPieces())
                .filter(piece -> piece != null && piece.getColor() == pieceColor)
                .flatMap(GameBoardPiece::getMoveListAsStream)
                .sorted(Comparator.comparingInt(ActionNode::getoDataX).thenComparing(ActionNode::getoDataY)) // sorted for consistent action selection
                .toArray(ActionNode[]::new);
    }

    public int getNumOpponentOptions(PieceManager pMgr) {
        return Arrays.stream(pMgr.getPieces())
                .filter(piece -> piece != null && piece.getColor() != pieceColor)
                .flatMap(GameBoardPiece::getMoveListAsStream)
                .toArray()
                .length;
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

    /**
     * Encodes the game state into a Base64 string for storage.
     * For LIGHT, reverses the array and swaps pieces; for DUSKY, uses as-is.
     * Each square (0-4) is 3 bits, packed into 96 bits.
     * @param gameState 32-element array (0=empty, 1=LIGHTChecker, etc.)
     * @return Base64-encoded string
     */
    public String intArrTo64Encoding(int[] gameState) {
        if (gameState == null) {
            LOGGER.error("Null gameState");
            throw new IllegalArgumentException("Null gameState");
        } else if (gameState.length != 64) {
            LOGGER.error(String.format("Invalid array length: %d", gameState.length));
            throw new IllegalArgumentException();
        }
        // Pack into 96 bits using a byte array (12 bytes)
        byte[] bytes = new byte[12]; // 96 bits = 12 bytes
        for (int i = 0; i < 32; i++) {
            int value = gameState[i];
            if (value < 0 || value > 4) {
                throw new IllegalArgumentException("Invalid value at index " + i + ": " + value);
            }
            // Pack 3 bits per value into bytes
            int byteIndex = i * 3 / 8; // Which byte to write to
            int bitOffset = (i * 3) % 8; // Bit position within the byte
            if (bitOffset <= 5) {
                // Fits within one byte
                bytes[byteIndex] |= (byte) (value << (5 - bitOffset));
            } else {
                // Spans two bytes
                bytes[byteIndex] |= (byte) (value >> (bitOffset - 5));
                bytes[byteIndex + 1] |= (byte) (value << (13 - bitOffset));
            }
        }
        return Base64.getEncoder().encodeToString(bytes);
    }

    // TODO may use this in pmgr
    private void reverseArray(int[] array) {
        int left = 0;
        int right = array.length - 1;
        while (left < right) {
            int temp = array[left];
            array[left] = array[right];
            array[right] = temp;
            left++;
            right--;
        }
    }
}
