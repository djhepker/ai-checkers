package main.java.game.entity.movement;

public class ActionNode {
    private final short[] data;
    private CapturedNode capturedEnemies;
    private ActionNode next;
    private int reward;

    public ActionNode(short postX, short postY) {
        this.data = new short[] {postX, postY};
        this.next = null;
        this.capturedEnemies = null;
        this.reward = 0;
    }

    public ActionNode(int postX, int postY) {
        this.data = new short[]{(short) postX, (short) postY};
        this.next = null;
        this.capturedEnemies = null;
        this.reward = 0;
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
        reward += pointValue;
        addCapturedNode(new CapturedNode((short) x, (short) y, (short) pointValue));
    }

    public void addCapturedNode(CapturedNode capturedNode) {
        if (capturedEnemies != null) {
            capturedNode.setNext(capturedEnemies);
        }
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
}
