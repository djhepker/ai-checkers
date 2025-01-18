package main.java.game.entity.movement;

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

    public CapturedNode cloneCapturedNode(CapturedNode inputNode) {
        if (inputNode == null) {
            System.out.println("Input node is null in getCloneOfCapturedNodeList()");
            return null;
        }
        CapturedNode cursor = inputNode;
        if (cursor == null) {
            System.out.println("inputNode.getCapturedNodes() is null in getCloneOfCapturedNodeList()");
            return null;
        }
        CapturedNode listHead = new CapturedNode(cursor.getDataX(), cursor.getDataY(), cursor.getPointValue());
        CapturedNode capturedNode = listHead;
        while (cursor.getNext() != null) {
            cursor = cursor.getNext();
            capturedNode.setNext(new CapturedNode(cursor.getDataX(), cursor.getDataY(), cursor.getPointValue()));
            capturedNode = capturedNode.getNext();
        }
        return listHead;
    }

    public void addLocationNode(int postX, int postY) {
        locList.addNode(new LocationNode((short) postX, (short) postY));
    }
}
