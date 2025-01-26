package main.java.game.entity.piece;

import main.java.game.entity.movement.ActionNode;
import main.java.game.entity.movement.MovementManager;
import main.java.game.gameworld.PieceManager;
import main.java.game.utils.GameBoardPiece;

import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Deque;

/*
* TODO: create movement logic for KingChecker, likely just need to Override generateLegalMoves()
* */
public class KingChecker extends Checker implements GameBoardPiece {
    MovementManager moveMgr;

    public KingChecker(String name, int x, int y, BufferedImage image) {
        super(name, x, y, image);
        this.moveMgr = super.getMoveMgr();
    }

    /*
     *   STATECODE:
     *   Stationary: 4
     *   Post-jump stationary: 3
     *   Northwest: -1
     *   Northeast: 1
     *   Southwest: -2
     *   SouthEast: 2
     * */
    @Override
    public void generateLegalMoves(PieceManager pMgr) {
        Deque<MoveState> taskQueue = new ArrayDeque<>();
        taskQueue.push(new MoveState(getX(), getY(), 3));

        while (!taskQueue.isEmpty()) {

            MoveState currState = taskQueue.pop();
            int stateCode = currState.getStateCode();
            int[] xDirectionArray;
            int[] yDirectionArray;

            if (stateCode > 2) {    // stationary case
                xDirectionArray = new int[] {-1, 1};
                yDirectionArray = new int[] {1, -1};
            } else if (stateCode < 0) {    // jumping case
                if (stateCode == -1) {
                    yDirectionArray = new int[] {-1};
                } else {
                    yDirectionArray = new int[] {1};
                }
                xDirectionArray = new int[] {-1};
            } else {
                if (stateCode == 1) {
                    yDirectionArray = new int[] {-1};
                } else {
                    yDirectionArray = new int[] {1};
                }
                xDirectionArray = new int[] {1};
            }

            // set ynext, xnext
            for (int xDirection : xDirectionArray) {
                int xNext = currState.getX() + xDirection;
                if (0 <= xNext && xNext < 8) {
                    for (int yDirection : yDirectionArray) {
                        int yNext = currState.getY() + yDirection;
                        if (0 <= yNext && yNext < 8) {
                            GameBoardPiece target = pMgr.getPiece(xNext, yNext);
                            if (target == null) {   // target open case
                                if (stateCode == 4) {   // target open; stationary
                                    moveMgr.addLocationNode(getX(), getY(), xNext, yNext);
                                } else if (stateCode < 3) { //  target open; mid-jump; direction acknowledged;
                                    ActionNode nextSpace = getJumpLandingNode(currState, xNext, yNext, pMgr);
                                    taskQueue.push(new MoveState(
                                            xNext, yNext, 3, nextSpace.getCapturedNodes()));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isReadyForPromotion() {
        return false;
    }
}
