package main.java.game.entity.movement;

class LocationList {
    private LocationNode head;

    public LocationList() {
        this.head = null;
    }

    public void addNode(LocationNode nodeToAdd) {
        if (head != null) {
            nodeToAdd.setNext(head);
        }
        head = nodeToAdd;
    }

    public void clearList() {
        head = null;
    }

    public LocationNode getHead() {
        return head;
    }
}
