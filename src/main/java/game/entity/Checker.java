package main.java.game.entity;

import main.java.game.entity.movement.CapturedNode;
import main.java.game.entity.movement.MovementManager;
import main.java.game.entity.movement.ActionNode;
import main.java.game.utils.GameBoardPiece;

import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.Stream;

public class Checker extends Entity implements GameBoardPiece {
    private final MovementManager moveMgr;
    private final PieceColor color;
    private final int movementSign;
    private final short pieceValue;

    public Checker(String name, int x, int y, BufferedImage image) {
        super(name, x, y, image, name.startsWith("LIGHT"));
        this.pieceValue = 10;
        this.moveMgr = new MovementManager();
        this.color = super.isLight() ? PieceColor.LIGHT : PieceColor.DUSKY;
        this.movementSign = super.isLight() ? 1 : -1;
    }

    @Override
    public Stream<ActionNode> getMoveListAsStream() {
        Stream.Builder<ActionNode> streamBuilder = Stream.builder();
        ActionNode cursor = getMoveListPointer();
        while (cursor != null) {
            streamBuilder.accept(cursor);
            cursor = cursor.getNext();
        }
        return streamBuilder.build();
    }

    @Override
    public boolean isLight() {
        return super.isLight();
    }

    @Override
    public BufferedImage getSprite() {
        return super.getSprite();
    }

    @Override
    public PieceColor getColor() {
        return color;
    }

    @Override
    public ActionNode getMoveListPointer() {
        return moveMgr.getPointerToListHead();
    }

    @Override
    public void printLegalMoves() {
        ActionNode cursor = moveMgr.getPointerToListHead();
        int row = 0;
        while (cursor != null) {
            System.out.print("Option " + row + ": (" + cursor.getfDataX() + ", " + cursor.getfDataY() + "); ");
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
    public short getPieceValue() {
        return pieceValue;
    }

    @Override
    public void update(GameBoardPiece[][] pieces) {
        moveMgr.clearListOfMoves();
        generateLegalMoves(pieces);
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
     * */
    @Override
    public void generateLegalMoves(GameBoardPiece[][] pieces) {
        Deque<MoveState> taskQueue = new ArrayDeque<>();
        taskQueue.push(new MoveState(getX(), getY(), 3));
        while (!taskQueue.isEmpty()) {
            MoveState currState = taskQueue.pop();
            int stateCode = currState.stateCode;
            int[] xDirectionArray;    // possible directions to move
            if (stateCode > 1) {
                xDirectionArray = new int[] {-1, 1};
            } else {
                xDirectionArray = new int[] {currState.stateCode};
            }
            int yNext = currState.yCell - movementSign;
            if (0 <= yNext && yNext < 8) {
                for (int xDirection : xDirectionArray) {
                    int xNext = currState.xCell + xDirection;
                    if (0 <= xNext && xNext < 8) {
                        GameBoardPiece target = pieces[xNext][yNext];
                        if (target == null) {   // target open case
                            if (stateCode == 3) { // target open; stationary;
                                moveMgr.addLocationNode(getX(), getY(), xNext, yNext);
                            } else if (stateCode < 2) {    //  target open; mid-jump; direction acknowledged;
                                short captureValue = pieces[currState.xCell][currState.yCell].getPieceValue();
                                ActionNode nextSpace = new ActionNode(getX(), getY(), xNext, yNext);
                                nextSpace.addCapturedNode(currState.xCell, currState.yCell, captureValue);
                                if (currState.capturedNode != null) { // handle prior captures this move
                                    nextSpace.addCapturedNode(moveMgr.cloneCapturedNode(currState.capturedNode));
                                }
                                moveMgr.addLocationNode(nextSpace);
                                taskQueue.push(new MoveState(xNext, yNext, 2, nextSpace.getCapturedNodes()));
                            }
                        } else if (stateCode > 1 && target.getColor() != this.color) {  // target not open;
                            if (stateCode == 2) { // post-jump; target !null; stationary;
                                taskQueue.push(new MoveState(xNext, yNext, xDirection, currState.capturedNode));
                            } else {    // beginning position
                                taskQueue.push(new MoveState(xNext, yNext, xDirection));
                            }
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
        private CapturedNode capturedNode;

        MoveState(int xCell, int yCell, int stateCode) {
            this.xCell = xCell;
            this.yCell = yCell;
            this.stateCode = stateCode;
            capturedNode = null;
        }

        MoveState(int xCell, int yCell, int stateCode, CapturedNode capturedNode) {
            this.xCell = xCell;
            this.yCell = yCell;
            this.stateCode = stateCode;
            this.capturedNode = capturedNode;
        }
    }
}
