package main.java.game.entity.piece;

import main.java.game.entity.movement.ActionNode;
import main.java.game.entity.movement.MovementManager;
import main.java.game.gameworld.PieceManager;
import main.java.game.entity.GameBoardPiece;

import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Deque;

public class KingChecker extends Checker {
    private MovementManager moveMgr;

    private int DEBUG_TIMER = 0;

    public KingChecker(String name, int x, int y, BufferedImage image) {
        super(name, x, y, image);
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
        while (!taskQueue.isEmpty()) {
            MoveState currState = taskQueue.pop();
            int stateCode = currState.getStateCode();
            int[] directionArr = getDirectionArr(stateCode);
            for (int i = 0; i < directionArr.length; ++i) {
                int xNext = directionArr[i++];
                if (0 <= xNext && xNext < 8) {
                    int yNext = directionArr[i];
                    if (0 <= yNext && yNext < 8) {
                        GameBoardPiece target = pMgr.getPiece(xNext, yNext);
                        if (target == null) { // cell open
                            if (stateCode == 4) {
                                moveMgr.addLocationNode(getX(), getY(), xNext, yNext);
                            } else if (stateCode < 3) { // cell open; mid-jump
                                ActionNode nextSpace = getJumpLandingNode(currState, xNext, yNext, pMgr);
                                taskQueue.push(new MoveState(
                                        xNext, yNext, 3, nextSpace.getCapturedNodes()));
                            }
                        } else if (stateCode > 2 && target.getColor() != super.getColor()) { // enemy occupied;
                            if (stateCode == 3) { // post-jump; enemy located
                                taskQueue.push(new MoveState(
                                        xNext, yNext, getStateCode(xNext, yNext), currState.getCapture()));
                            } else { // starting position; enemy located;
                                taskQueue.push(new MoveState(xNext, yNext, getStateCode(xNext, yNext)));
                            }
                        }
                    }
                }
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
        if (xNext == 1) {
            if (yNext == 1) {
                return -1;
            }
            return -2;
        } else if (xNext == -1) {
            if (yNext == 1) {
                return 2;
            }
            return 1;
        }
        return 100000;
    }

    private int[] getDirectionArr(int stateCode) {
        int[] directionArr = switch (stateCode) {
            case -2 -> {
                yield new int[]{1,-1};
            }
            case -1 -> {
                yield new int[]{1,1};
            }
            case 1 -> {
                yield new int[]{-1,-1};
            }
            case 2 -> {
                yield new int[]{-1,1};
            }
            default -> {
                yield new int[]{ 1,-1,
                                 1,1,
                                 -1,-1,
                                 -1,1 };
            }
        };
        return directionArr;
    }

    @Override
    public boolean isReadyForPromotion() {
        return false;
    }
}
