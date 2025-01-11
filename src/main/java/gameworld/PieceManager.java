package main.java.gameworld;

import main.java.entity.Entity;
import main.java.graphics.InputHandler;
import main.java.engine.EntityCreator;
import main.java.utils.GameBoardPiece;

public class PieceManager {
    private Entity[][] pieces;
    private EntityCreator creator;
    private InputHandler input;

    public PieceManager(Entity[][] pieces, EntityCreator creator, InputHandler inputHandler) {
        this.pieces = pieces;
        this.creator = creator;
        this.input = inputHandler;
        createBeginningCheckers();
    }

    public void movePiece(Entity entity) {
        if (entity instanceof GameBoardPiece && movePieceHelper((GameBoardPiece) entity)) {
            pieces[entity.getX()][entity.getY()] = entity;
        }
    }

    private boolean movePieceHelper(GameBoardPiece entityToMove) {
        int[][] theoreticalMoves = entityToMove.getTheoreticalMoves();
        if (theoreticalMoves == null) {
            return false;
        }

        int postX = input.getSelectedCol();
        int postY = input.getSelectedRow();

        for (int[] xPosition : theoreticalMoves) {
            if (postX == xPosition[0] && postY == xPosition[1] && spaceIsNull(postX, postY)) {
                entityToMove.setX(postX);
                entityToMove.setY(postY);
                entityToMove.update();
                pieces[input.getFirstXPos()][input.getFirstYPos()] = null;
                return true;
            }
        }
        return false;
    }

    private boolean spaceIsNull(int postX, int postY) {
        return pieces[postX][postY] == null;
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

    public Entity getPiece(int x, int y) {
        return pieces[x][y];
    }

    public void printNumPieces() {
        System.out.println("The number of pieces: " + pieces.length);
    }

    public void printAllPiecesInPlay() {
        for (Entity[] row : pieces) {
            for (Entity col : row) {
                if (col instanceof GameBoardPiece) {
                    GameBoardPiece piece = (GameBoardPiece) col;
                    piece.printData();
                }
            }
        }
    }
}
