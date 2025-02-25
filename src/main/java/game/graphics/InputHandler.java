package main.java.game.graphics;

import java.awt.event.MouseEvent;

public class InputHandler {

    private int selectedRow;
    private int selectedCol;
    private int firstXPos;
    private int firstYPos;

    private InputState inputState;

    private enum InputState {
        NO_CLICK,
        FIRST_CLICK,
        SECOND_CLICK
    }

    public InputHandler() {
        this.selectedCol = -1;
        this.selectedRow = -1;
        this.firstXPos = -1;
        this.firstYPos = -1;
        inputState = InputState.NO_CLICK;
    }

    public void update() {
        switch (inputState) {
            case NO_CLICK:
                return;
            case FIRST_CLICK:
                firstXPos = selectedCol;
                firstYPos = selectedRow;
                return;
            case SECOND_CLICK:
                return;
            default:
                System.out.println("Invalid input state");
        }
    }

    public void handleMouseClick(MouseEvent e, int width, int height) {
        switch (inputState) {
            case NO_CLICK:
                inputState = InputState.FIRST_CLICK;
                break;
            case FIRST_CLICK:
                inputState = InputState.SECOND_CLICK;
                break;
            case SECOND_CLICK:
                System.out.println("Slow down there, partner. We have two second clicks.");
                return;
            default:
                System.out.println("Invalid input state");
                return;
        }
        selectedCol = e.getX() / (width / 8);
        selectedRow = e.getY() / (height / 8);
    }

    public void resetClicks() {
        inputState = InputState.NO_CLICK;
    }

    public boolean hasSelectedPiece() {
        return inputState == InputState.FIRST_CLICK;
    }

    public boolean movementChosen() {
        return inputState == InputState.SECOND_CLICK;
    }

    public int getFirstXPos() {
        return firstXPos;
    }

    public int getFirstYPos() {
        return firstYPos;
    }

    public int getSelectedRow() {
        return selectedRow;
    }

    public int getSelectedCol() {
        return selectedCol;
    }
}
