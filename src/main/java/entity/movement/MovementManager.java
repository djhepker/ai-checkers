package main.java.entity.movement;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class MovementManager {
    private MovementNode cursor;
    private MovementList mList;

    public MovementManager() {
        this.mList = new MovementList();
        this.cursor = null;
    }

    public void clearListOfMoves() {
        mList.clearList();
    }

    public Set<Point> getLegalMoves() {
        cursor = mList.getHead();
        if (cursor == null) {
            return null;
        }
        Set<Point> moves = new HashSet<>();
        while (cursor != null) {
            moves.add(new Point(cursor.getDataX(), cursor.getDataY()));
            cursor = cursor.getNext();
        }
        return moves;
    }

    public void addMovement(int postX, int postY) {
        mList.addNode(new MovementNode((short) postX, (short) postY));
    }
}
