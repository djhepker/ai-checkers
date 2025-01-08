package entity.movement;

public class Movement {
    private final int postX;
    private final int postY;

    protected Movement(int postX, int postY) {
        this.postX = postX;
        this.postY = postY;
    }

    public int[] getMoveCoordinates() {
        return new int[] {postX, postY};
    }
}
