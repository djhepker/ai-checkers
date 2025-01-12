package main.java.entity;

import main.java.entity.movement.MovementManager;
import main.java.utils.GameBoardPiece;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Set;
import java.util.Stack;

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

    //TODO: Logic implementation for king status of checker
    //TODO: decide if we want to mark pieces that can optionally be taken
    @Override
    public void generateLegalMoves() {
        Stack<MoveState> taskStack = new Stack<>();
        taskStack.push(new MoveState(getX(), getY(), 0));

        while (!taskStack.empty()) {
            MoveState currState = taskStack.pop();
            int [] xOffsets;
            if (currState.jumpState == 0) {
                xOffsets = new int[] {-1, 1};
            } else {
                xOffsets = new int[] {currState.jumpState};
            }
            int yNext = currState.yCell - movementSign;
            if (0 <= yNext && yNext < 8) {
                for (int xOffset : xOffsets) {
                    int xNext = currState.xCell + xOffset;
                    if (0 <= xNext && xNext < 8) {
                        if (currState.jumpState == 0) { // stationary
                            if (pieces[xNext][yNext] == null) { // next is clear
                                moveMgr.addMovement(xNext, yNext);
                            } else {    // check jump case
                                taskStack.push(new MoveState(xNext, yNext, xOffset));
                            }
                        } else if (pieces[xNext][yNext] == null) {  // jumping; next is clear;
                            moveMgr.addMovement(xNext, yNext);
                            taskStack.push(new MoveState(xNext, yNext, 0));
                        }
                    }
                }
            }
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

    private class MoveState {
        int xCell;
        int yCell;
        int jumpState;

        MoveState(int xCell, int yCell, int jumpState) {
            this.xCell = xCell;
            this.yCell = yCell;
            this.jumpState = jumpState;
        }
    }
}
