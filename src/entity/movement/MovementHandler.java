package entity.movement;

import utils.MovementList;

public class MovementHandler {
    private MovementNode cursor;
    private MovementList mList;

    public MovementHandler() {
        this.mList = new MovementList();
        this.cursor = null;
    }

    public void clearList() {
        mList.clearList();
    }

    public int[][] getTheoreticalMoves() {
        cursor = mList.getHead();
        int[][] moves = new int[mList.getSize()][2];
        int i = 0;
        while(cursor != null) {
            moves[i] = cursor.getData();
            cursor = cursor.getNext();
            i++;
        }
        return moves;
    }

    public void addMovement(int postX, int postY) {
        mList.addNode( new MovementNode(postX, postY));
    }
}
