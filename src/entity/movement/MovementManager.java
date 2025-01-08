package entity.movement;

import entity.Entity;
import graphics.InputHandler;
import utils.MovableEntity;

public class MovementManager {
    private InputHandler input;

    public MovementManager(InputHandler inHndlr) {
        this.input = inHndlr;
    }

    public void movePiece(Entity entity) {
        if (entity instanceof MovableEntity) {
            movePieceHelper((MovableEntity) entity);
        }
    }

    private void movePieceHelper(MovableEntity entityToMove) {
        int[][] theoreticalMoves = entityToMove.getTheoreticalMoves();
        int postX = input.getSecondXPos();
        int postY = input.getSecondYPos();

        for (int[] xPosition : theoreticalMoves) {
            if (postX == xPosition[0] && postY == xPosition[1]) {
                entityToMove.setX(postX);
                entityToMove.setY(postY);
            }
        }
    }
}
