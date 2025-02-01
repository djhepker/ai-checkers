package main.java.game.entity.piece;

import main.java.game.entity.Entity;
import main.java.game.entity.movement.MovementManager;
import main.java.game.entity.movement.ActionNode;
import main.java.game.gameworld.PieceManager;
import main.java.game.entity.GameBoardPiece;

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
        this.pieceValue = 1;
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

    /*
     *   STATECODE:
     *   Stationary: 4
     *   Post-jump stationary: 3
     *   Left-jumping: -1
     *   Right-jumping: 1
     * */
    @Override
    public void generateLegalMoves(PieceManager pMgr) {
        Deque<MoveState> taskQueue = new ArrayDeque<>();
        taskQueue.push(new MoveState(getX(), getY(), 4));
        while (!taskQueue.isEmpty()) {
            MoveState currState = taskQueue.pop();
            int stateCode = currState.getStateCode();
            int[] xDirectionArray;
            if (stateCode > 1) {
                xDirectionArray = new int[] {-1, 1};
            } else {
                xDirectionArray = new int[] {currState.getStateCode()};
            }
            int yNext = currState.getY() - movementSign;
            if (0 <= yNext && yNext < 8) {
                for (int xDirection : xDirectionArray) {
                    int xNext = currState.getX() + xDirection;
                    if (0 <= xNext && xNext < 8) {
                        GameBoardPiece target = pMgr.getPiece(xNext, yNext);
                        if (target == null) {   // target open case
                            if (stateCode == 4) { // target open; stationary;
                                moveMgr.addLocationNode(getX(), getY(), xNext, yNext);
                            } else if (stateCode < 3) {    //  target open; mid-jump; direction acknowledged;
                                ActionNode nextSpace = getCaptureAction(currState, xNext, yNext, pMgr);
                                taskQueue.push(new MoveState(
                                        xNext, yNext, 3, nextSpace.getCapturedNodes()));
                                moveMgr.addLocationNode(nextSpace);
                            }
                        } else if (stateCode > 2 && target.getColor() != this.color) {  // target not open;
                            if (stateCode == 3) { // post-jump; target !null; stationary;
                                taskQueue.push(new MoveState(xNext, yNext, xDirection, currState.getCapture()));
                            } else {    // beginning position
                                taskQueue.push(new MoveState(xNext, yNext, xDirection));
                            }
                        }
                    }
                }
            }
        }
    }

    ActionNode getCaptureAction(MoveState currState, int xNext, int yNext, PieceManager pMgr) {
        int jumpedX = currState.getX();
        int jumpedY = currState.getY();
        short captureValue = pMgr.getPiece(jumpedX, jumpedY).getPieceValue();
        ActionNode nextSpace = new ActionNode(getX(), getY(), xNext, yNext);
        if (currState.getCapture() != null) { // handle prior captures this move
            nextSpace.addCapturedNode(moveMgr.cloneCapturedNode(currState.getCapture()));
        }
        nextSpace.addCapturedNode(jumpedX, jumpedY, captureValue);
        return nextSpace;
    }

    public MovementManager getMoveMgr() {
        return moveMgr;
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
    public void update(PieceManager pMgr) {
        moveMgr.clearListOfMoves();
        generateLegalMoves(pMgr);
    }

    @Override
    public boolean isReadyForPromotion() {
        if (movementSign == 1 && getY() == 0) {
            return true;
        } else if (movementSign == -1 && getY() == 7) {
            return true;
        }
        return false;
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
    public void printData() {
        System.out.print("Piece name: " + getName() + "; ");
        System.out.print("Piece coordinates: (" + getX() + ", " + getY() + "); ");
        System.out.print("Legal move choices: ");
        printLegalMoves();
    }
}
