package hepker.game.entity.piece;

import hepker.game.entity.Entity;
import hepker.game.entity.movement.MovementManager;
import hepker.game.entity.movement.ActionNode;
import hepker.game.gameworld.PieceManager;
import hepker.game.entity.GameBoardPiece;
import lombok.Getter;

import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.Stream;

public class Checker extends Entity implements GameBoardPiece {

    @Getter
    private final MovementManager moveMgr;
    private final int movementSign;
    private final short pieceValue;

    public Checker(String name, int x, int y, BufferedImage image) {
        super(name, x, y, image, name.startsWith("LIGHT"));
        this.pieceValue = 1;
        this.moveMgr = new MovementManager();
        this.movementSign = super.isLight() ? 1 : -1;
    }

    /**
     * Generates a stream of legal moves the checker can make for the AI
     * @return Stream of ActionNode
     */
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

    /**
     * Getter for checking the color of this checker
     * @return true if the checker is light, false otherwise
     */
    @Override
    public boolean isLight() {
        return super.isLight();
    }

    /**
     * Generates and contributes to this checker's movemanager, a list of legal actions it can take
     * @param pMgr Required for computer vision
     */
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
                        } else if (stateCode > 2 && target.getColor() != super.getColor()) {  // target not open;
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

    /**
     * Helper function for generating an ActionNode for the corresponding movement
     * @param currState The current state we are evaluating from
     * @param xNext Hypothetical next x coordinate
     * @param yNext Hypothetical next y coordinate
     * @param pMgr Required for accurate computer vision
     * @return nextSpace ActionNode containing all of this movement's information
     */
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

    /**
     * Getter for sprite
     * @return Sprite of this checker
     */
    @Override
    public BufferedImage getSprite() {
        return super.getSprite();
    }

    /**
     * Getter for this piece's color
     * @return The color light or dark
     */
    @Override
    public PieceColor getColor() {
        return super.getColor();
    }

    /**
     * Getter function for obtaining the linkedlist of legal moves
     * @return A pointer to the head of the linkedlist of legal moves
     */
    @Override
    public ActionNode getMoveListPointer() {
        return moveMgr.getPointerToListHead();
    }

    /**
     * Getter for checker's name
     * @return Light or Dusky checker
     */
    @Override
    public String getName() {
        return super.getName();
    }

    /**'
     * Getter for this piece's value
     * @return The value of capturing this piece
     */
    @Override
    public short getPieceValue() {
        return pieceValue;
    }

    /**
     * Commands this piece to check for legal moves
     * @param pMgr Required for computer vision
     */
    @Override
    public void update(PieceManager pMgr) {
        moveMgr.clearListOfMoves();
        generateLegalMoves(pMgr);
    }

    /**
     * Returns values to see if this piece can be promoted
     * @return true if yes false if no
     */
    @Override
    public boolean isReadyForPromotion() {
        if (movementSign == 1 && getY() == 0) {
            return true;
        } else {
            return movementSign == -1 && getY() == 7;
        }
    }
}
