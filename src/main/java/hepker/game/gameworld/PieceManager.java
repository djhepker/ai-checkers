package hepker.game.gameworld;

import hepker.game.entity.movement.CapturedNode;
import hepker.game.entity.movement.ActionNode;
import hepker.game.graphics.InputHandler;
import hepker.engine.EntityCreator;
import hepker.game.entity.GameBoardPiece;
import lombok.Getter;

public final class PieceManager {
    @Getter
    private GameBoardPiece[] piecesLightPerspective;
    @Getter
    private GameBoardPiece[] piecesDuskyPerspective;
    private final EntityCreator creator;
    private final InputHandler input;
    @Getter
    private int numDusky;
    @Getter
    private int numLight;
    private boolean gameOver;

    public PieceManager(EntityCreator inputCreator, InputHandler inputInputHandler) {
        this.creator = inputCreator;
        this.piecesLightPerspective = generateBeginningCheckers();
        this.input = inputInputHandler;
        generateBeginningCheckers();
        updateAllPieces();
        this.piecesDuskyPerspective = getInverseBoard(piecesLightPerspective);
        this.gameOver = false;
    }

    public void updateAllPieces() {
        for (GameBoardPiece piece : piecesLightPerspective) {
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
        return piecesLightPerspective[y * 8 + x];
    }

    public boolean movePiece(GameBoardPiece piece) {
        int postX = input.getSelectedCol();
        int postY = input.getSelectedRow();
        if (spaceIsNull(postX, postY)) {
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

    public boolean spaceIsNull(int inputX, int inputY) {
        return piecesLightPerspective[inputY * 8 + inputX] == null;
    }

    public boolean insertPieceToBoard(GameBoardPiece piece) {
        if (piece == null) {
            return false;
        }
        piecesLightPerspective[piece.getY() * 8 + piece.getX()] = piece;
        return true;
    }

    public void nullifyPiece(int x, int y) {
        piecesLightPerspective[y * 8 + x] = null;
    }

    public int getNumPiecesInPlay() {
        return piecesLightPerspective.length;
    }

    /**
     * Reverses all pieces in the game board, as if the board were rotated pi
     */
    private GameBoardPiece[] getInverseBoard(GameBoardPiece[] inputPieces) {
        GameBoardPiece[] inverseBuilder = new GameBoardPiece[inputPieces.length];
        int left = 0;
        int right = inverseBuilder.length - 1;
        while (left <= right) {
            inverseBuilder[left] = inputPieces[right];
            inverseBuilder[right--] = inputPieces[left++];
        }
        return inverseBuilder;
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
