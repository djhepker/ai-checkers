package main.java.game.gameworld;

import main.java.game.entity.movement.CapturedNode;
import main.java.game.entity.movement.ActionNode;
import main.java.game.graphics.InputHandler;
import main.java.engine.EntityCreator;
import main.java.game.utils.GameBoardPiece;

public class PieceManager {
    private GameBoardPiece[][] pieces;
    private EntityCreator creator;
    private InputHandler input;

    public PieceManager(EntityCreator creator, InputHandler inputHandler) {
        this.creator = creator;
        this.pieces = generateBeginningCheckers();
        this.input = inputHandler;
        generateBeginningCheckers();
        updateAllPieces();
    }

    public void updateAllPieces() {
        for (GameBoardPiece[] row : pieces) {
            for (GameBoardPiece piece : row) {
                if (piece != null) {
                    piece.update(pieces);
                }
            }
        }
    }

    public GameBoardPiece[][] getPieces() {
        return pieces;
    }

    public GameBoardPiece getPiece(int x, int y) {
        return pieces[x][y];
    }

    public boolean movePiece(GameBoardPiece piece) {
        int postX = input.getSelectedCol();
        int postY = input.getSelectedRow();

        if (spaceIsNull(postX, postY)) {
            ActionNode cursor = piece.getMoveListPointer();
            while (cursor != null) {
                if (cursor.getfDataX() == postX && cursor.getfDataY() == postY) {
                    CapturedNode capturedPiece = cursor.getCapturedNodes();
                    while (capturedPiece != null) {
                        pieces[capturedPiece.getDataX()][capturedPiece.getDataY()] = null;
                        capturedPiece = capturedPiece.getNext();
                    }
                    piece.setX(postX);
                    piece.setY(postY);
                    pieces[input.getFirstXPos()][input.getFirstYPos()] = null;
                    pieces[piece.getX()][piece.getY()] = piece;
                    return true;
                }
                cursor = cursor.getNext();
            }
        }
        return false;
    }

    public boolean spaceIsNull(int inputX, int inputY) {
        return pieces[inputX][inputY] == null;
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

    private GameBoardPiece[][] generateBeginningCheckers() {
        GameBoardPiece[][] pieces = new GameBoardPiece[8][8];
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
        return pieces;
    }
}
