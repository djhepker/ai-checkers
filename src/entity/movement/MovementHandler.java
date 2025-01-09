package entity.movement;

import utils.MovementList;

public class MovementHandler {
    private MovementNode cursor;
    private MovementList mList;

    public MovementHandler() {
        this.mList = new MovementList();
        this.cursor = null;
    }

    public void clearListOfMoves() {
        mList.clearList();
    }

    // creates Nx2 matrix
    public int[][] getTheoreticalMoves() {
        cursor = mList.getHead();
        if (cursor == null) {
            return null;
        }
        int[][] moves = new int[mList.getSize()][2];
        int row = 0;
        while (cursor != null) {
            moves[row][0] = cursor.getDataX();
            moves[row][1] = cursor.getDataY();
            cursor = cursor.getNext();
            row++;
        }
        return moves;
    }

    public void addMovement(int postX, int postY) {
        mList.addNode(new MovementNode(postX, postY));
    }
}
