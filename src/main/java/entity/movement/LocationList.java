package main.java.entity.movement;

//  Package-private scope
public class LocationList {
    private LocationNode head;
    private short size;

    public LocationList() {
        this.head = null;
        this.size = 0;
    }

    public void addNode(LocationNode nodeToAdd) {
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

    public LocationNode getHead() {
        return head;
    }
}
