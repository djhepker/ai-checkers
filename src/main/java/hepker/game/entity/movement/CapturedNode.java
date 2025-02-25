package hepker.game.entity.movement;

public class CapturedNode {
    private final short[] data;
    private final short pointValue;
    private CapturedNode next;

    public CapturedNode(short postX, short postY, short pointValue) {
        this.data = new short[] {postX, postY};
        this.next = null;
        this.pointValue = pointValue;
    }

    public short getPointValue() {
        return pointValue;
    }

    public short getDataX() {
        return data[0];
    }

    public short getDataY() {
        return data[1];
    }

    public CapturedNode getNext() {
        return next;
    }

    public void setNext(CapturedNode next) {
        this.next = next;
    }

    public void printData() {
        System.out.printf("CapturedNode (%d,%d)\n", data[0], data[1]);
    }
}
