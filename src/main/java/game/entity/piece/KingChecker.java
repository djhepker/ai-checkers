package main.java.game.entity.piece;

import main.java.game.entity.movement.ActionNode;
import main.java.game.entity.movement.MovementManager;
import main.java.game.gameworld.PieceManager;
import main.java.game.entity.GameBoardPiece;

import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;

public class KingChecker extends Checker {
    private MovementManager moveMgr;

    private int DEBUG_TIMER = 0;

    public KingChecker(String name, int x, int y, BufferedImage image) {
        super(name, x, y, image);
        this.moveMgr = super.getMoveMgr();
    }

    /*
    * TODO: check if the fix worked
    * */

    /*
     *   STATECODE:
     *   Stationary: 4
     *   Post-jump stationary: 3
     *   Northeast: -2
     *   Southeast: -1
     *   Northwest: 1
     *   Southwest: 2
     * */
    @Override
    public void generateLegalMoves(PieceManager pMgr) {
        Deque<MoveState> taskQueue = new ArrayDeque<>();
        taskQueue.push(new MoveState(getX(), getY(), 4));
        boolean[] isChecked = new boolean[64];
        while (!taskQueue.isEmpty()) {
            MoveState currState = taskQueue.pop();
            int stateCode = currState.getStateCode();
            int[] directionArr = getDirectionArr(stateCode);
            try {
                for (int i = 0; i < directionArr.length; ++i) {
                    if (++DEBUG_TIMER > 1000) {
                        throw new IllegalStateException();
                    }
                    int xNext = currState.getX() + directionArr[i++];
                    if (0 <= xNext && xNext < 8) {
                        int yNext = currState.getY() + directionArr[i];
                        if (0 <= yNext && yNext < 8) {
                            GameBoardPiece target = pMgr.getPiece(xNext, yNext);
                            if (target == null) { // cell open
                                if (stateCode == 4) { // cell open; stationary
                                    moveMgr.addLocationNode(getX(), getY(), xNext, yNext);
                                } else if (stateCode < 3) { // cell open; mid-jump
                                    isChecked[yNext * 8 + xNext] = true;
                                    ActionNode nextSpace = getJumpLandingNode(currState, xNext, yNext, pMgr);
                                    taskQueue.push(new MoveState(
                                            xNext, yNext, 3, nextSpace.getCapturedNodes()));
                                }
                            } else if (stateCode > 2 &&
                                    target.getColor() != super.getColor() &&
                                    !isChecked[yNext * 8 + xNext]) { // enemy occupied;
                                int dx = xNext - currState.getX();
                                int dy = yNext - currState.getY();
                                int updatedState = getStateCode(dx, dy);
                                if (stateCode == 3) { // post-jump; enemy located
                                    taskQueue.push(new MoveState(
                                            xNext, yNext, updatedState, currState.getCapture()));
                                } else { // starting position; enemy located;
                                    taskQueue.push(new MoveState(xNext, yNext, updatedState));
                                }
                            }
                        }
                    }
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
                taskQueue.clear();
            }
        }
        DEBUG_TIMER = 0;
    }

    /*
     *   STATECODE:
     *   Stationary: 4
     *   Post-jump stationary: 3
     *   Northeast: -2
     *   Southeast: -1
     *   Northwest: 1
     *   Southwest: 2
     * */
    private int getStateCode(int xNext, int yNext) {
        if (xNext == 1) { // east case
            if (yNext == 1) { // south case
                return -1;
            }
            return -2;
        } else if (xNext == -1) { // west case
            if (yNext == 1) { // south case
                return 2;
            }
            return 1;
        } else {
            throw new IllegalArgumentException("xNext: " + xNext + ", yNext: " + yNext);
        }
    }

    private int[] getDirectionArr(int stateCode) {
        return switch (stateCode) {
            case -2 -> new int[]{1, -1}; // Northeast
            case -1 -> new int[]{1, 1};  // Southeast
            case 1 -> new int[]{-1, -1}; // Northwest
            case 2 -> new int[]{-1, 1};  // Southwest
            default -> new int[]{ 1,-1, 1,1, -1,-1, -1,1 }; // For stationary and other cases
        };
    }


    @Override
    public boolean isReadyForPromotion() {
        return false;
    }
}
