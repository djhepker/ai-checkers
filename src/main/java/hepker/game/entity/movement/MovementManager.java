package hepker.game.entity.movement;

public class MovementManager {
    private ActionList actList;

    public MovementManager() {
        this.actList = new ActionList();
    }

    public void addLocationNode(int preX, int preY, int postX, int postY) {
        actList.addNode(new ActionNode((short) preX, (short) preY, (short) postX, (short) postY));
    }

    public void addLocationNode(ActionNode node) {
        actList.addNode(node);
    }

    public void clearListOfMoves() {
        actList.clearList();
    }

    public ActionNode getPointerToListHead() {
        return actList.getHead();
    }

    public CapturedNode cloneCapturedNode(CapturedNode inputNode) {
        if (inputNode == null) {
            return null;
        }
        CapturedNode cursor = inputNode;
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
