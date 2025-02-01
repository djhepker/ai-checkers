package main.java.game.entity.movement;

public class ActionNode {
    private final short[] data;
    private CapturedNode capturedEnemies;
    private ActionNode next;
    private int reward;

    public ActionNode(short preX, short preY, short postX, short postY) {
        this.data = new short[]{preX, preY, postX, postY};
        this.next = null;
        this.capturedEnemies = null;
        this.reward = 0;
    }

    public ActionNode(int preX, int preY, int postX, int postY) {
        this.data = new short[]{(short) preX, (short) preY, (short) postX, (short) postY};
        this.next = null;
        this.capturedEnemies = null;
        this.reward = 0;
    }

    public short getoDataX() {
        return data[0];
    }

    public short getoDataY() {
        return data[1];
    }

    public short getfDataX() {
        return data[2];
    }

    public short getfDataY() {
        return data[3];
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
        this.reward += capturedNode.getPointValue();
        this.capturedEnemies = capturedNode;
    }

    public ActionNode getNext() {
        return next;
    }

    public void setNext(ActionNode next) {
        this.next = next;
    }

    public int getReward() {
        return reward;
    }

    public void printData() {
        System.out.printf("Node (%d,%d)\n", data[0], data[1]);
    }

    public void printCapturedEnemies() {
        CapturedNode capturedCursor = capturedEnemies;
        printData();
        while (capturedCursor != null) {
            capturedCursor.printData();
            capturedCursor = capturedCursor.getNext();
        }
        System.out.println();
    }
}
