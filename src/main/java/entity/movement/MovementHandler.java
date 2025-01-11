package main.java.entity.movement;

import main.java.utils.MovementList;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

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
        mList.addNode(new MovementNode(postX, postY));
    }
}
