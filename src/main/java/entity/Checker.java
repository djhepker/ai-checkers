package main.java.entity;

import main.java.entity.movement.MovementHandler;
import main.java.utils.GameBoardPiece;

import java.awt.image.BufferedImage;

public class Checker extends Entity implements GameBoardPiece {
    private final MovementHandler movementHandler;
    private final PieceColor color;
    private Entity[][] pieces;
    private final int movementSign;

    public Checker(String name, int x, int y, BufferedImage image, Entity[][] pieces) {
        super(name, x, y, image);
        this.movementHandler = new MovementHandler();
        this.color = PieceColor.valueOf(name.substring(0, 5));
        this.movementSign = color == PieceColor.LIGHT ? 1 : -1;
        this.pieces = pieces;
        generateLegalMoves();
    }

    @Override
    public void clearMovementList() {
        movementHandler.clearListOfMoves();
    }

    @Override
    public void printTheoreticalMoves() {
        int[][] moves = getLegalMoves();
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
        generateLegalMoves();
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
    public void generateLegalMoves() {
        generateMoveHelper(getX(), getY(), 0);
    }

    //TODO: Logic implementation for king status of checker
    //TODO: decide if we want to mark pieces that can optionally be taken
    private void generateMoveHelper(int xCell, int yCell, int numRecursions) {
        int nextYPosition = yCell - 1 * movementSign;
        if (nextYPosition >= 0) {
            int leftX = xCell - 1;
            if (leftX >= 0) {
                if (pieces[leftX][nextYPosition] == null) {
                    movementHandler.addMovement(leftX, nextYPosition);
                    if (numRecursions % 2 != 0) {
                        generateMoveHelper(leftX, nextYPosition, numRecursions + 1);
                    }
                } else if (numRecursions % 2 == 0) {
                    generateMoveHelper(leftX, nextYPosition, numRecursions + 1);
                } else {
                    return;
                }
            } else if (numRecursions % 2 != 0) {
                return;
            }
            int rightX = xCell + 1;
            if (rightX <= 7) {
                if (pieces[rightX][nextYPosition] == null) {
                    movementHandler.addMovement(rightX, nextYPosition);
                    if (numRecursions % 2 != 0) {
                        generateMoveHelper(rightX, nextYPosition, numRecursions + 1);
                    }
                } else if (numRecursions % 2 == 0) {
                    generateMoveHelper(rightX, nextYPosition, numRecursions + 1);
                }
            }
        }
    }

    @Override
    public int[][] getLegalMoves() {
        return movementHandler.getLegalMoves();
    }

    @Override
    public void printData() {
        System.out.print("Piece name: " + getName() + "; ");
        System.out.print("Piece coordinates: (" + getX() + ", " + getY() + "); ");
        System.out.print("Theoretical move choices: ");
        printTheoreticalMoves();
    }
}
