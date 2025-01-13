package main.java.entity.movement;

public class MovementManager {
    private LocationNode cursor;
    private LocationList locList;

    public MovementManager() {
        this.locList = new LocationList();
        this.cursor = null;
    }

    public void clearListOfMoves() {
        locList.clearList();
    }

    public LocationNode getPointerToMoveList() {
        return locList.getHead();
    }

    public void addMovement(int postX, int postY) {
        locList.addNode(new LocationNode((short) postX, (short) postY));
    }

    public void addMovement(int postX, int postY, LocationList capturedList) {
        locList.addNode(new LocationNode((short) postX, (short) postY, capturedList));
    }
}
