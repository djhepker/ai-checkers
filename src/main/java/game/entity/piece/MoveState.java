package main.java.game.entity.piece;

import main.java.game.entity.movement.CapturedNode;

class MoveState {
    private final int xCell;
    private final int yCell;
    private final int stateCode;
    private CapturedNode capturedNode;

    MoveState(int xCell, int yCell, int stateCode) {
        this.xCell = xCell;
        this.yCell = yCell;
        this.stateCode = stateCode;
        capturedNode = null;
    }

    MoveState(int xCell, int yCell, int stateCode, CapturedNode capturedNode) {
        this.xCell = xCell;
        this.yCell = yCell;
        this.stateCode = stateCode;
        this.capturedNode = capturedNode;
    }

    public int getX() {
        return xCell;
    }

    public int getY() {
        return yCell;
    }

    public int getStateCode() {
        return stateCode;
    }

    public CapturedNode getCapture() {
        return capturedNode;
    }
}