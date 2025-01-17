package main.java.entity.movement;

public class LocationNode {
    private final short[] data;
    private CapturedNode capturedEnemies;
    private LocationNode next;

    public LocationNode(short postX, short postY) {
        this.data = new short[] {postX, postY};
        this.next = null;
        this.capturedEnemies = null;
    }

    public short getDataX() {
        return data[0];
    }

    public short getDataY() {
        return data[1];
    }

    public CapturedNode getCapturedNodes() {
        return capturedEnemies;
    }

    public void addCapturedNode(int x, int y, int pointValue) {
        addCapturedNode(new CapturedNode((short) x, (short) y, (short) pointValue));
    }

    public void addCapturedNode(CapturedNode capturedNode) {
        if (capturedEnemies != null) {
            capturedNode.setNext(capturedEnemies);
        }
        this.capturedEnemies = capturedNode;
    }

    public LocationNode getNext() {
        return next;
    }

    public void setNext(LocationNode next) {
        this.next = next;
    }
}
