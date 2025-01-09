package gameworld;

import entity.Entity;
import graphics.InputHandler;
import utils.EntityCreator;
import utils.EntityArray;
import utils.MovableEntity;

public class PieceManager {
    private EntityArray pieces;
    private EntityCreator creator;
    private InputHandler input;

    public PieceManager(EntityArray pieces, EntityCreator creator, InputHandler inputHandler) {
        this.pieces = pieces;
        this.creator = creator;
        this.input = inputHandler;
        createBeginningPieces();
    }

    public void movePiece(Entity entity) {
        if (entity instanceof MovableEntity && movePieceHelper((MovableEntity) entity)) {
            pieces.addEntity(entity);
        }
    }

    private boolean movePieceHelper(MovableEntity entityToMove) {
        int[][] theoreticalMoves = entityToMove.getTheoreticalMoves();
        if (theoreticalMoves == null) {
            return false;
        }

        int postX = input.getSelectedCol();
        int postY = input.getSelectedRow();

        for (int[] xPosition : theoreticalMoves) {
            if (postX == xPosition[0] && postY == xPosition[1]) {
                entityToMove.setX(postX);
                entityToMove.setY(postY);
                entityToMove.update();
                pieces.removeEntity(input.getFirstXPos(), input.getFirstYPos());
                return true;
            }
        }
        return false;
    }

    private void createBeginningPieces() {
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
                pieces.addEntity(creator.createDarkPiece(x, y));
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
                pieces.addEntity(creator.createLightPiece(x, y));
                x += 2;
            }
        }
    }

    public Entity getPiece(int x, int y) {
        return pieces.getEntity(x, y);
    }

    public void printNumPieces() {
        System.out.println("The number of pieces: " + pieces.getNumEntities());
    }

    public void printAllPiecesInPlay() {
        pieces.printEntities();
    }
}
