package entity;

import entity.movement.MovementHandler;

import java.awt.image.BufferedImage;

public class LightPiece extends Entity {
    private final MovementHandler movementHandler;

    public LightPiece(int x, int y, BufferedImage image) {
        super("LightPiece", x, y, image);
        this.movementHandler = new MovementHandler();
        generateLegalMoves();
    }

    public void update() {
        movementHandler.clearList();
        generateLegalMoves();
    }

    public int[][] getTheoreticalMoves() {
        return movementHandler.getTheoreticalMoves();
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
