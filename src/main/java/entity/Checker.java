package main.java.entity;

import main.java.entity.movement.MovementHandler;
import main.java.utils.GameBoardPiece;

import java.awt.image.BufferedImage;

public class Checker extends Entity implements GameBoardPiece {
    private final MovementHandler movementHandler;
    private final PieceColor color;
    private final int movementSign;

    public Checker(String name, int x, int y, BufferedImage image) {
        super(name, x, y, image);
        this.movementHandler = new MovementHandler();
        try {
            this.color = PieceColor.valueOf(name.substring(0, 5));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        this.movementSign = color == PieceColor.LIGHT ? 1 : -1;
        generateTheoreticalMoves();
    }

    @Override
    public void clearMovementList() {
        movementHandler.clearListOfMoves();
    }

    @Override
    public void printTheoreticalMoves() {
        int[][] moves = getTheoreticalMoves();
        if (moves == null) {
            return;
        }
        for (int row = 0; row < moves[0].length; row++) {
            System.out.print("Row: " + row + " (" + moves[row][0] + ", " + moves[row][1] + "); ");
        }
        System.out.println();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void update() {
        movementHandler.clearListOfMoves();
        printTheoreticalMoves();
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
    public void generateTheoreticalMoves() {
        if (getY() - 1 * movementSign >= 0) {
            if (getX() + 1 <= 7) {
                movementHandler.addMovement(getX() + 1, getY() - 1 * movementSign);
            }
            if (getX() - 1 >= 0) {
                movementHandler.addMovement(getX() - 1, getY() - 1 * movementSign);
            }
        }
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
}
