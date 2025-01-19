package main.java.game.entity.movement;

class ActionList {
    private ActionNode head;

    public ActionList() {
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
