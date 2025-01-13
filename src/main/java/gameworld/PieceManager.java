package main.java.gameworld;

import main.java.entity.Entity;
import main.java.graphics.InputHandler;
import main.java.engine.EntityCreator;
import main.java.utils.GameBoardPiece;

import java.awt.Point;
import java.util.Set;

public class PieceManager {
    private GameBoardPiece[][] pieces;
    private EntityCreator creator;
    private InputHandler input;

    public PieceManager(GameBoardPiece[][] pieces, EntityCreator creator, InputHandler inputHandler) {
        this.pieces = pieces;
        this.creator = creator;
        this.input = inputHandler;
        createBeginningCheckers();
        updateAllPieces();
    }

    public void updateAllPieces() {
        for (GameBoardPiece[] row : pieces) {
            for (GameBoardPiece piece : row) {
                if (piece != null) {
                    piece.update();
                }
            }
        }
    }

    public boolean movePiece(GameBoardPiece piece) {
        if (movePieceHelper(piece)) {
            pieces[piece.getX()][piece.getY()] = piece;
        } else {
            return false;
        }
        return true;
    }

    private boolean movePieceHelper(GameBoardPiece entityToMove) {
        Set<Point> legalMoves = entityToMove.getLegalMoves();
        if (legalMoves == null || legalMoves.isEmpty()) {
            return false;
        }
        int postX = input.getSelectedCol();
        int postY = input.getSelectedRow();
        Point targetMove = new Point(postX, postY); // move logic into mvmgr
        if (legalMoves.contains(targetMove) && spaceIsNull(postX, postY)) {
            entityToMove.setX(postX);
            entityToMove.setY(postY);
            pieces[input.getFirstXPos()][input.getFirstYPos()] = null;
            return true;
        }
        return false;
    }


    private boolean spaceIsNull(int postX, int postY) {
        return pieces[postX][postY] == null;
    }

    public GameBoardPiece getPiece(int x, int y) {
        return pieces[x][y];
    }

    public void printNumPieces() {
        System.out.println("The number of pieces: " + pieces.length);
    }

    public void printAllPiecesInPlay() {
        for (GameBoardPiece[] row : pieces) {
            for (GameBoardPiece piece : row) {
                piece.printData();
            }
        }
    }

    private void createBeginningCheckers() {
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
                pieces[x][y] = creator.createChecker("DUSKYChecker", x, y);
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
                pieces[x][y] = creator.createChecker("LIGHTChecker", x, y);
                x += 2;
            }
        }
    }
}
