package hepker.game.entity.piece;

import hepker.game.entity.movement.CapturedNode;

class MoveState {

    private final int xCell;
    private final int yCell;
    private final int stateCode;
    private final CapturedNode capturedNode;

    MoveState(int xCoordinate, int yCoordinate, int stateIntRepresentation) {
        this.xCell = xCoordinate;
        this.yCell = yCoordinate;
        this.stateCode = stateIntRepresentation;
        capturedNode = null;
    }

    MoveState(int xCoordinate, int yCoordinate, int stateIntRepresentation, CapturedNode inputCapturedNode) {
        this.xCell = xCoordinate;
        this.yCell = yCoordinate;
        this.stateCode = stateIntRepresentation;
        this.capturedNode = inputCapturedNode;
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