package main.java.entity.movement;

public class LocationNode {
    private final short[] data;
    private LocationList capturedList;
    private LocationNode next;

    public LocationNode(short postX, short postY) {
        data = new short[] {postX, postY};
        this.next = null;
    }

    public LocationNode(short postX, short postY, LocationList capturedList) {
        data = new short[] {postX, postY};
        this.next = null;
        this.capturedList = capturedList;
    }

    public short getDataX() {
        return data[0];
    }

    public short getDataY() {
        return data[1];
    }
    // TODO: Implement logic to utilize our newly created capturedlist parameter
    // TODO: ensure we don't have irrelevant logic or scope in our movement package
    // TODO: create logic in checker movement such that capturedList is filled accordingly
    public LocationNode getCapturedListPointer() {
        return capturedList.getHead();
    }

    public LocationNode getNext() {
        return next;
    }

    public void setNext(LocationNode next) {
        this.next = next;
    }
}
