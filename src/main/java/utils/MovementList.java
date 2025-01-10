package main.java.utils;

import main.java.entity.movement.MovementNode;

public class MovementList {
    private MovementNode head;
    private int size;

    public MovementList() {
        this.head = null;
        this.size = 0;
    }

    public void addNode(MovementNode nodeToAdd) {
        if (head == null) {
            head = nodeToAdd;
            size++;
        } else {
            nodeToAdd.setNext(head);
            head = nodeToAdd;
            size++;
        }
    }

    public int getSize() {
        return size;
    }

    public void clearList() {
        head = null;
    }

    public MovementNode getHead() {
        return head;
    }
}
