package hepker.game.entity.piece;

import hepker.game.entity.movement.ActionNode;
import hepker.game.entity.movement.MovementManager;
import hepker.game.gameworld.PieceManager;
import hepker.game.entity.GameBoardPiece;

import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Deque;

public final class KingChecker extends Checker {

    private final MovementManager moveMgr;

    public KingChecker(String name, int x, int y, BufferedImage image) {
        super(name, x, y, image, (short) 10);
        this.moveMgr = super.getMoveMgr();
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
                    int xNext = currState.getX() + directionArr[i++];
                    if (0 <= xNext && xNext < 8) {
                        int yNext = currState.getY() + directionArr[i];
                        if (0 <= yNext && yNext < 8) {
                            int nxtTile = yNext * 8 + xNext;
                            GameBoardPiece target = pMgr.getPiece(xNext, yNext);
                            if (target == null) { // cell open
                                if (stateCode == 4) { // cell open; stationary
                                    moveMgr.addLocationNode(getX(), getY(), xNext, yNext);
                                } else if (stateCode < 3) { // cell open; mid-jump
                                    isChecked[nxtTile] = true;
                                    ActionNode nextSpace = getCaptureAction(currState, xNext, yNext, pMgr);
                                    taskQueue.push(new MoveState(
                                            xNext, yNext, 3, nextSpace.getCapturedNodes()));
                                    moveMgr.addLocationNode(nextSpace);
                                }
                            } else if (stateCode > 2 && target.getColor() != super.getColor()) { // enemy occupied;
                                if (!isChecked[nxtTile]) {
                                    int dx = xNext - currState.getX();
                                    int dy = yNext - currState.getY();
                                    int updatedState = getStateCode(dx, dy);
                                    if (stateCode == 3) { // post-jump; enemy located
                                        taskQueue.push(new MoveState(
                                                xNext, yNext, updatedState, currState.getCapture()));
                                    } else { // starting position; enemy located;
                                        taskQueue.push(new MoveState(xNext, yNext, updatedState));
                                    }
                                    isChecked[nxtTile] = true;
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
            default -> new int[] {1, -1, 1, 1, -1, -1, -1, 1}; // For stationary and other cases
        };
    }

    @Override
    public boolean isReadyForPromotion() {
        return false;
    }
}
