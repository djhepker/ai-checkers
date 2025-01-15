package main.java.entity.movement;

public class MovementManager {
    private LocationList locList;

    public MovementManager() {
        this.locList = new LocationList();
    }

    public void clearListOfMoves() {
        locList.clearList();
    }

    public LocationNode getPointerToListHead() {
        return locList.getHead();
    }

    public void addLocationNode(int postX, int postY) {
        locList.addNode(new LocationNode((short) postX, (short) postY));
    }

    public LocationNode cloneNode(LocationNode originalNode) {
        LocationNode nodeClone = new LocationNode(originalNode.getDataX(), originalNode.getDataY());
        CapturedNode cursor = originalNode.getCapturedNodes();
        while (cursor != null) {
            nodeClone.addCapturedNode(new CapturedNode(cursor.getDataX(), cursor.getDataY(), cursor.getPointValue()));
            cursor = cursor.getNext();
        }
        return nodeClone;
    }
}
