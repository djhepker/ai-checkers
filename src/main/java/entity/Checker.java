package main.java.entity;

import main.java.entity.movement.MovementHandler;
import main.java.utils.GameBoardPiece;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Set;

public class Checker extends Entity implements GameBoardPiece {
    private final MovementHandler movementHandler;
    private final PieceColor color;
    private GameBoardPiece[][] pieces;
    private final int movementSign;

    public Checker(String name, int x, int y, BufferedImage image, GameBoardPiece[][] pieces) {
        super(name, x, y, image);
        this.movementHandler = new MovementHandler();
        this.color = PieceColor.valueOf(name.substring(0, 5));
        this.movementSign = color == PieceColor.LIGHT ? 1 : -1;
        this.pieces = pieces;
        generateLegalMoves();
    }

    @Override
    public BufferedImage getSprite() {
        return super.getSprite();
    }

    @Override
    public void printLegalMoves() {
        Set<Point> moves = getLegalMoves();
        if (moves == null || moves.isEmpty()) {
            System.out.println();
            return;
        }
        int row = 0;
        for (Point move : moves) {
            System.out.print("Option " + row + ": (" + move.getX() + ", " + move.getY() + "); ");
            row++;
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
    //TODO: HANDLE BEING ABLE TO MOVE (0, -2) IF ENEMY PIECE IS (+1, -2)
    private void generateMoveHelper(int xCell, int yCell, int numRecursions) {
        int nextYPosition = yCell - 1 * movementSign;
        if (nextYPosition >= 0) {
            int leftX = xCell - 1;
            if (leftX >= 0) {
                if (pieces[leftX][nextYPosition] == null) {
                    movementHandler.addMovement(leftX, nextYPosition);
                    //System.out.println("Boundary check: [" + (leftX >= 0) + "] leftX movement: [" + (pieces[leftX][nextYPosition] == null) + "] numRecursions is even: [" + (numRecursions % 2 == 0) + "]");
                    if (numRecursions % 2 != 0) {
                        generateMoveHelper(leftX, nextYPosition, numRecursions + 1);
                    }
                } else if (numRecursions % 2 == 0) {
                    generateMoveHelper(leftX, nextYPosition, numRecursions + 1);
                } else if (false) {
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
    public Set<Point> getLegalMoves() {
        return movementHandler.getLegalMoves();
    }

    @Override
    public void printData() {
        System.out.print("Piece name: " + getName() + "; ");
        System.out.print("Piece coordinates: (" + getX() + ", " + getY() + "); ");
        System.out.print("Legal move choices: ");
        printLegalMoves();
    }
}
