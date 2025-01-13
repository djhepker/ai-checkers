package main.java.entity.movement;

//  Package-private scope
class MovementList {
    private MovementNode head;
    private short size;

    public MovementList() {
        this.head = null;
        this.size = 0;
    }

    public void addNode(MovementNode nodeToAdd) {
        if (head != null) {
            nodeToAdd.setNext(head);
        }
        head = nodeToAdd;
        size++;
    }

    public int getSize() {
        return size;
    }

    public void clearList() {
        head = null;
        size = 0;
    }

    public MovementNode getHead() {
        return head;
    }
}
