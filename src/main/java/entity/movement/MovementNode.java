package main.java.entity.movement;

//  Package-private scope
class MovementNode {
    private final Movement data;
    private MovementNode next;

    public MovementNode(short postX, short postY) {
        data = new Movement(postX, postY);
        this.next = null;
    }

    public int getDataX() {
        return data.getPostX();
    }

    public int getDataY() {
        return data.getPostY();
    }

    public MovementNode getNext() {
        return next;
    }

    public void setNext(MovementNode next) {
        this.next = next;
    }
}
