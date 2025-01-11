package main.java.entity;

import main.java.entity.movement.MovementManager;
import main.java.utils.GameBoardPiece;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Set;

public class Checker extends Entity implements GameBoardPiece {
    private final MovementManager moveMgr;
    private final PieceColor color;
    private GameBoardPiece[][] pieces;
    private final int movementSign;

    public Checker(String name, int x, int y, BufferedImage image, GameBoardPiece[][] pieces) {
        super(name, x, y, image);
        this.moveMgr = new MovementManager();
        this.color = PieceColor.valueOf(name.substring(0, 5));
        this.movementSign = color == PieceColor.LIGHT ? 1 : -1;
        this.pieces = pieces;
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
        moveMgr.clearListOfMoves();
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
        int nextY = yCell - 1 * movementSign;

        if (nextY >= 0 && nextY < 8) {

            int leftX = xCell - 1;

            if (leftX < 0) {
                return;
            }

            if (pieces[leftX][nextY] == null) {
                generateMoveHelper(leftX, nextY, numRecursions + 1);
                moveMgr.addMovement(leftX, nextY);
            } else if (numRecursions % 2 == 0) {
                generateMoveHelper(leftX, nextY, numRecursions + 1);
            }
//
//            int rightX = xCell + 1;
//
//            if (rightX <= 7) {
//
//            }
        }
    }

    @Override
    public Set<Point> getLegalMoves() {
        return moveMgr.getLegalMoves();
    }

    @Override
    public void printData() {
        System.out.print("Piece name: " + getName() + "; ");
        System.out.print("Piece coordinates: (" + getX() + ", " + getY() + "); ");
        System.out.print("Legal move choices: ");
        printLegalMoves();
    }
}
