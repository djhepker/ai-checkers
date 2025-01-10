package com.hepker.ai_checkers.entity.movement;

public class Movement {
    private final int postX;
    private final int postY;

    protected Movement(int postX, int postY) {
        this.postX = postX;
        this.postY = postY;
    }

    public int getpostX() {
        return postX;
    }

    public int getpostY() {
        return postY;
    }
}
