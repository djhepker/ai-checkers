package main.java.game.entity.movement;

public class MovementManager {
    private ActionList locList;

    public MovementManager() {
        this.locList = new ActionList();
    }

    public void addLocationNode(int preX, int preY, int postX, int postY) {
        locList.addNode(new ActionNode((short) preX, (short) preY, (short) postX, (short) postY));
    }

    public void addLocationNode(ActionNode node) {
        locList.addNode(node);
    }

    public void clearListOfMoves() {
        locList.clearList();
    }

    public ActionNode getPointerToListHead() {
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
}
