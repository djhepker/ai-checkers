package entity;

import entity.movement.MovementHandler;
import utils.MovableEntity;

import java.awt.image.BufferedImage;

public class DarkPiece extends Entity implements MovableEntity {
    private final MovementHandler movementHandler;

    public DarkPiece(int x, int y, BufferedImage image) {
        super("DarkPiece", x, y, image);
        this.movementHandler = new MovementHandler();
        generateTheoreticalMoves();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void update() {
        movementHandler.clearListOfMoves();
        generateTheoreticalMoves();
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

    @Override
    public int[][] getTheoreticalMoves() {
        return movementHandler.getTheoreticalMoves();
    }

    @Override
    public void printData() {
        System.out.print("Piece name: " + getName() + "; ");
        System.out.print("Piece coordinates: (" + getX() + ", " + getY() + "); ");
        System.out.print("Theoretical move choices: ");
        printTheoreticalMoves();
    }

    public void clearMovementList() {
        movementHandler.clearListOfMoves();
    }

    private void generateTheoreticalMoves() {
        if (getY() - 1 >= 0) {
            if (getX() + 1 <= 7) {
                movementHandler.addMovement(getX() + 1, getY() - 1);
            }
            if (getX() - 1 >= 0) {
                movementHandler.addMovement(getX() - 1, getY() - 1);
            }
        }
    }

    private void printTheoreticalMoves() {
        int[][] moves = getTheoreticalMoves();
        if (moves == null) {
            return;
        }
        for (int row = 0; row < moves[0].length; row++) {
            System.out.print("Row: " + row + " (" + moves[row][0] + ", " + moves[row][1] + "); ");
        }
        System.out.println();
    }
}
