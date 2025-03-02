package hepker.game.entity.movement;

import lombok.Getter;
import lombok.Setter;

public final class CapturedNode {

    private final short[] data;

    @Getter
    private final short pointValue;

    @Setter
    @Getter
    private CapturedNode next;

    public CapturedNode(short postX, short postY, short inputRewardValue) {
        this.data = new short[] {postX, postY};
        this.next = null;
        this.pointValue = inputRewardValue;
    }

    public short getDataX() {
        return data[0];
    }

    public short getDataY() {
        return data[1];
    }
}
