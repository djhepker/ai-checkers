package main.java.game.gameworld;

import main.java.game.entity.movement.CapturedNode;
import main.java.game.entity.movement.ActionNode;
import main.java.game.graphics.InputHandler;
import main.java.engine.EntityCreator;
import main.java.game.entity.GameBoardPiece;
/*
* TODO: scale down [][] to []
* */
public class PieceManager {
    private GameBoardPiece[][] pieces;
    private EntityCreator creator;
    private InputHandler input;
    private int numDusky;
    private int numLight;

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
                    piece.update(this);
                }
            }
        }
    }

    public void promotePiece(GameBoardPiece piece) {
        int x = piece.getX();
        int y = piece.getY();
        pieces[x][y] = creator.createPiece(piece.getName() + "King", x, y);
    }

    public boolean sideDefeated() {
        return numDusky * numLight == 0;
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

    public void movePiece(ActionNode actionNode) {
        int xNaught = actionNode.getoDataX();
        int yNaught = actionNode.getoDataY();
        CapturedNode capturedPiece = actionNode.getCapturedNodes();
        while (capturedPiece != null) {
            if (pieces[capturedPiece.getDataX()][capturedPiece.getDataY()].isLight()) {
                --numLight;
            } else {
                --numDusky;
            }
            pieces[capturedPiece.getDataX()][capturedPiece.getDataY()] = null;
            capturedPiece = capturedPiece.getNext();
        }
        GameBoardPiece piece = pieces[xNaught][yNaught];
        piece.setX(actionNode.getfDataX());
        piece.setY(actionNode.getfDataY());
        pieces[xNaught][yNaught] = null;
        pieces[piece.getX()][piece.getY()] = piece;
    }

    public boolean spaceIsNull(int inputX, int inputY) {
        return pieces[inputX][inputY] == null;
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
                pieces[x][y] = creator.createPiece("DUSKYChecker", x, y);
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
                pieces[x][y] = creator.createPiece("LIGHTChecker", x, y);
                x += 2;
            }
        }
        numDusky = 12;
        numLight = 12;
        return pieces;
    }
}
