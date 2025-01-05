package entity.movement;

public class MovementNode {
    private final Movement data;
    private MovementNode next;

    public MovementNode(int postX, int postY) {
        data = new Movement(postX, postY);
        this.next = null;
    }

    public int[] getData() {
        return data.getMoveCoordinates();
    }

    public MovementNode getNext() {
        return next;
    }

    public void setNext(MovementNode next) {
        this.next = next;
    }
}
