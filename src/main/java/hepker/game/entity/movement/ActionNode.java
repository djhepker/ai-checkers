package hepker.game.entity.movement;

import lombok.Getter;
import lombok.Setter;

public final class ActionNode {

    private final short[] data;

    @Setter
    @Getter
    private ActionNode next;
    private CapturedNode capturedEnemies;

    @Getter
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

    public String toString() {
        return new StringBuilder().append("ActionNode: [ ")
                .append("Start: (").append(getoDataX()).append(", ").append(getoDataY()).append("), ")
                .append("End: (").append(getfDataX()).append(", ").append(getfDataY()).append("), ")
                .append("Reward: ").append(reward)
                .append(" ]\n")
                .toString();
    }
}
