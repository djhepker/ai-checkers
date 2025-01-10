package main.java.gameworld;

import main.java.entity.Entity;
import main.java.graphics.InputHandler;
import main.java.utils.EntityArray;
import main.java.engine.EntityCreator;
import main.java.utils.GameBoardPiece;

public class PieceManager {
    private EntityArray pieces;
    private EntityCreator creator;
    private InputHandler input;

    public PieceManager(EntityArray pieces, EntityCreator creator, InputHandler inputHandler) {
        this.pieces = pieces;
        this.creator = creator;
        this.input = inputHandler;
        createBeginningCheckers();
    }

    public void movePiece(Entity entity) {
        if (entity instanceof GameBoardPiece && movePieceHelper((GameBoardPiece) entity)) {
            pieces.addEntity(entity);
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
            if (postX == xPosition[0] && postY == xPosition[1] && isLegalMove(postX, postY)) {
              entityToMove.setX(postX);
              entityToMove.setY(postY);
                entityToMove.update();
                pieces.removeEntity(input.getFirstXPos(), input.getFirstYPos());
                return true;
            }
        }
        return false;
    }

    private boolean isLegalMove(int postX, int postY) {
        return pieces.getEntity(postX, postY) == null;
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
                pieces.addEntity(creator.createChecker("DUSKYChecker", x, y));
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
                pieces.addEntity(creator.createChecker("LIGHTChecker", x, y));
                x += 2;
            }
        }
    }

    public Entity getPiece(int x, int y) {
        return pieces.getEntity(x, y);
    }

    public void printNumPieces() {
        System.out.println("The number of pieces: " + pieces.getLength());
    }

    public void printAllPiecesInPlay() {
        pieces.printEntities();
    }
}
