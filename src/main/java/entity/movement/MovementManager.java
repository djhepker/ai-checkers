package main.java.entity.movement;

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

    public MovementNode getPointerToMoveList() {
        return mList.getHead();
    }

    public void addMovement(int postX, int postY) {
        mList.addNode(new MovementNode((short) postX, (short) postY));
    }
}
