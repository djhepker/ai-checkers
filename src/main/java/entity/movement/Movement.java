package main.java.entity.movement;

//  Package-private scope
class Movement {
    private final short postX;
    private final short postY;

    protected Movement(short postX, short postY) {
        this.postX = postX;
        this.postY = postY;
    }

    protected short getPostX() {
        return postX;
    }

    protected short getPostY() {
        return postY;
    }
}
