package hepker.game.gameworld;

import hepker.game.entity.movement.CapturedNode;
import hepker.game.entity.movement.ActionNode;
import hepker.game.graphics.InputHandler;
import hepker.engine.EntityCreator;
import hepker.game.entity.GameBoardPiece;
import lombok.Getter;

import java.util.Arrays;

public final class PieceManager {
    private final EntityCreator creator;
    @Getter
    private GameBoardPiece[] piecesContainer;
    @Getter
    private GameBoardPiece[] displayPieces;
    @Getter
    private int numDusky;
    @Getter
    private int numLight;
    private boolean gameOver;

    public PieceManager(EntityCreator inputCreator) {
        this.creator = inputCreator;
        this.piecesContainer = generateBeginningCheckers();
        updateAllPieces();
        this.displayPieces = piecesContainer;
        this.gameOver = false;
    }

    /**
     * Loads all legal moves for each piece in play
     */
    public void updateAllPieces() {
        for (GameBoardPiece piece : piecesContainer) {
            if (piece != null) {
                piece.update(this);
            }
        }
    }

    public void promotePiece(GameBoardPiece piece) {
        if (piece != null) {
            insertPieceToBoard(creator.createPiece(piece.getName() + "King", piece.getX(), piece.getY()));
        }
    }

    public boolean sideDefeated() {
        return numDusky * numLight == 0 || gameOver;
    }

    public void flagGameOver() {
        this.gameOver = true;
    }

    public GameBoardPiece getPiece(int x, int y) {
        return piecesContainer[y * 8 + x];
    }

    public boolean movePiece(GameBoardPiece piece, InputHandler input) {
        int postX = input.getSelectedCol();
        int postY = input.getSelectedRow();
        if (getPiece(postX, postY) == null) {
            ActionNode cursor = piece.getMoveListPointer();
            while (cursor != null) {
                if (cursor.getfDataX() == postX && cursor.getfDataY() == postY) {
                    processCapturedPieces(cursor);
                    piece.setX(postX);
                    piece.setY(postY);
                    nullifyPiece(input.getFirstXPos(), input.getFirstYPos());
                    insertPieceToBoard(piece);
                    return true;
                }
                cursor = cursor.getNext();
            }
        }
        return false;
    }

    public boolean machineMovePiece(ActionNode actionNode) {
        int xNaught = actionNode.getoDataX();
        int yNaught = actionNode.getoDataY();
        processCapturedPieces(actionNode);
        GameBoardPiece piece = getPiece(xNaught, yNaught);
        piece.setX(actionNode.getfDataX());
        piece.setY(actionNode.getfDataY());
        nullifyPiece(xNaught, yNaught);
        boolean result = insertPieceToBoard(piece);
        if (piece.isReadyForPromotion()) {
            promotePiece(piece);
        }
        return result;
    }

    public boolean insertPieceToBoard(GameBoardPiece piece) {
        if (piece == null) {
            return false;
        }
        if (Arrays.equals(piecesContainer, displayPieces)) {
            piecesContainer[piece.getY() * 8 + piece.getX()] = piece;
            displayPieces[piece.getY() * 8 + piece.getX()] = piece;
        } else {
            piecesContainer[piece.getY() * 8 + piece.getX()] = piece;
            displayPieces[(7 - piece.getY()) * 8 + (7 - piece.getX())] = piece;
        }
        return true;
    }

    public void nullifyPiece(int x, int y) {
        if (Arrays.equals(piecesContainer, displayPieces)) {
            piecesContainer[y * 8 + x] = null;
            displayPieces[y * 8 + x] = null;
        } else {
            piecesContainer[y * 8 + x] = null;
            displayPieces[(7 - y) * 8 + (7 - x)] = null;
        }
    }

    public int getNumPiecesInPlay() {
        int count = 0;
        for (GameBoardPiece piece : piecesContainer) {
            if (piece != null) {
                ++count;
            }
        }
        return count;
    }

    /**
     * Reverses all pieces in the game board, as if the board were rotated pi
     */
    public void reverseBoard() {
        int n = piecesContainer.length;
        for (int i = 0; i < n; i++) {
            GameBoardPiece piece = piecesContainer[i];
            if (piece != null) {
                piece.setX(7 - piece.getX());
                piece.setY(7 - piece.getY());
            }
        }
        for (int left = 0, right = n - 1; left < right; left++, right--) {
            GameBoardPiece temp = piecesContainer[left];
            piecesContainer[left] = piecesContainer[right];
            piecesContainer[right] = temp;
        }
    }


    private void processCapturedPieces(ActionNode actionNode) {
        CapturedNode capturedPiece = actionNode.getCapturedNodes();
        while (capturedPiece != null) {
            if (getPiece(capturedPiece.getDataX(), capturedPiece.getDataY()).isLight()) {
                --numLight;
            } else {
                --numDusky;
            }
            nullifyPiece(capturedPiece.getDataX(), capturedPiece.getDataY());
            capturedPiece = capturedPiece.getNext();
        }
    }

    private GameBoardPiece[] generateBeginningCheckers() {
        GameBoardPiece[] boardBuilder = new GameBoardPiece[64];
        int x = 1;
        int y = 0;
        while (y < 3) {
            if (x > 7) {
                x = 0;
                y++;
                if (y % 2 == 0) {
                    x += 1;
                }
            } else {
                boardBuilder[y * 8 + x] = creator.createPiece("DUSKYChecker", x, y);
                x += 2;
            }
        }
        y += 2;
        while (y < 8) {
            if (x > 7) {
                x = 0;
                y++;
                if (y % 2 == 0) {
                    x += 1;
                }
            } else {
                boardBuilder[y * 8 + x] = creator.createPiece("LIGHTChecker", x, y);
                x += 2;
            }
        }
        numDusky = 12;
        numLight = 12;
        return boardBuilder;
    }
}
