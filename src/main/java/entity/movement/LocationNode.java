package main.java.entity.movement;

public class LocationNode {
    private final short[] data;
    private LocationNode capturedEnemies;
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

    public LocationNode getCapturedEnemyNodes() {
        return capturedEnemies;
    }

    public void addCapturedEnemyNode(LocationNode capturedNode) {
        if (capturedEnemies == null) {
            this.capturedEnemies = capturedNode;
        } else {
          capturedNode.setNext(capturedEnemies);
          this.capturedEnemies = capturedNode;
        }
    }

    public void addCapturedEnemyNode(int x, int y) {
        LocationNode capturedNode = new LocationNode((short) x, (short) y);
        if (capturedEnemies == null) {
            this.capturedEnemies = capturedNode;
        } else {
            LocationNode cursor = capturedEnemies;
            while (cursor.getNext() != null) {
                cursor = cursor.getNext();
            }
            cursor.setNext(capturedNode);
        }
    }

    public LocationNode getNext() {
        return next;
    }

    public void setNext(LocationNode next) {
        this.next = next;
    }
}
