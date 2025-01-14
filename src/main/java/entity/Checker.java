package main.java.entity;

import main.java.entity.movement.LocationList;
import main.java.entity.movement.MovementManager;
import main.java.entity.movement.LocationNode;
import main.java.utils.GameBoardPiece;

import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Deque;

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
    public LocationNode getMoveListPointer() {
        return moveMgr.getPointerToMoveList();
    }

    @Override
    public void printLegalMoves() {
        LocationNode cursor = moveMgr.getPointerToMoveList();
        int row = 0;
        while (cursor != null) {
            System.out.print("Option " + row + ": (" + cursor.getDataX() + ", " + cursor.getDataY() + "); ");
            cursor = cursor.getNext();
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
    /*
     *   STATECODE:
     *   Stationary: 3
     *   Post-jump stationary: 2
     *   Left-jumping: -1
     *   Right-jumping: 1
     *
     * */
    @Override
    public void generateLegalMoves() {
        Deque<MoveState> taskQueue = new ArrayDeque<>();
        taskQueue.push(new MoveState(getX(), getY(), 3));
        while (!taskQueue.isEmpty()) {
            MoveState currState = taskQueue.pop();
            int stateCode = currState.stateCode;
            int [] xDirrectionArray;    // possible directions to move
            if (stateCode > 1) {
                xDirrectionArray = new int[] {-1, 1};
            } else {
                xDirrectionArray = new int[] {currState.stateCode};
            }
            int yNext = currState.yCell - movementSign;
            if (0 <= yNext && yNext < 8) {
                for (int xDirection : xDirrectionArray) {
                    int xNext = currState.xCell + xDirection;
                    if (0 <= xNext && xNext < 8) {
                        GameBoardPiece target = pieces[xNext][yNext];
                        if (target == null) {   // target open case
                            if (stateCode == 3) { // target open; stationary;
                                moveMgr.addLocationNode(xNext, yNext);
                            } else if (stateCode < 2) {    //  target open; mid-jump;
                                moveMgr.addLocationNode(xNext, yNext);
                                taskQueue.push(new MoveState(xNext, yNext, 2));
                            }
                        } else if (stateCode > 1) {  // target not open; stationary;
                            taskQueue.push(new MoveState(xNext, yNext, xDirection));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void printData() {
        System.out.print("Piece name: " + getName() + "; ");
        System.out.print("Piece coordinates: (" + getX() + ", " + getY() + "); ");
        System.out.print("Legal move choices: ");
        printLegalMoves();
    }

    private class MoveState {
        private final int xCell;
        private final int yCell;
        private final int stateCode;
        private LocationList capturedList;

        MoveState(int xCell, int yCell, int stateCode) {
            this.xCell = xCell;
            this.yCell = yCell;
            this.stateCode = stateCode;
        }

        MoveState(int xCell, int yCell, LocationNode enemyNode, int stateCode) {
            this.xCell = xCell;
            this.yCell = yCell;
            this.stateCode = stateCode;
            this.capturedList = new LocationList(enemyNode);
        }
    }
}
