package entity;

import entity.movement.MovementHandler;
import utils.MovableEntity;

import java.awt.image.BufferedImage;

public class DarkPiece extends Entity implements MovableEntity {
    private final MovementHandler movementHandler;

    public DarkPiece(int x, int y, BufferedImage image) {
        super("DarkPiece", x, y, image);
        this.movementHandler = new MovementHandler();
        generateLegalMoves();
    }

    public void update() {
        movementHandler.clearList();
        generateLegalMoves();
    }

    @Override
    public int[][] getTheoreticalMoves() {
        return movementHandler.getTheoreticalMoves();
    }

    @Override
    public void setX(int x) {
        super.setX(x);
    }

    @Override
    public void setY(int y) {
        super.setY(y);
    }

    @Override
    public int getX() {
        return super.getX();
    }

    @Override
    public int getY() {
        return super.getY();
    }

    private void generateLegalMoves() {
        if (getY() - 1 >= 0) {
            if (getX() + 1 <= 7) {
                movementHandler.addMovement(getX() + 1, getY() - 1);
            }
            if (getX() - 1 >= 0) {
                movementHandler.addMovement(getX() - 1, getY() - 1);
            }
        }
    }
}
