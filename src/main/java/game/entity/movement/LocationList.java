package main.java.game.entity.movement;

class LocationList {
    private ActionNode head;

    public LocationList() {
        this.head = null;
    }

    public void addNode(ActionNode nodeToAdd) {
        if (head != null) {
            nodeToAdd.setNext(head);
        }
        head = nodeToAdd;
    }

    public void clearList() {
        head = null;
    }

    public ActionNode getHead() {
        return head;
    }
}
