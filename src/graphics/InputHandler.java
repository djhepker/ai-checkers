package graphics;

import java.awt.event.MouseEvent;

public class InputHandler {
    private GraphicsHandler gHandler;
    private int selectedRow;
    private int selectedCol;
    private int firstXPos;
    private int firstYPos;
    private int secondXPos;
    private int secondYPos;
    private InputState inputState;

    private enum InputState {
        NO_CLICK,
        FIRST_CLICK,
        SECOND_CLICK;
    }

    public InputHandler(GraphicsHandler gHandler) {
        this.gHandler = gHandler;
        this.selectedCol = -1;
        this.selectedRow = -1;
        this.firstXPos = -1;
        this.firstYPos = -1;
        this.secondXPos = -1;
        this.secondYPos = -1;
        inputState = InputState.NO_CLICK;
    }

    public void update() {
        switch (inputState) {
            case NO_CLICK:
                return;
            case FIRST_CLICK:
                firstXPos = selectedCol * (gHandler.getWidth() / 8);
                firstYPos = selectedRow * (gHandler.getHeight() / 8);
                return;
            case SECOND_CLICK:
                secondXPos = selectedCol * (gHandler.getWidth() / 8);
                secondYPos = selectedRow * (gHandler.getHeight() / 8);
                return;
            default:
                System.out.println("Invalid input state");
        }
    }

    public void handleMouseClick(MouseEvent e) {
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
        selectedCol = e.getX() / (gHandler.getWidth() / 8);
        selectedRow = e.getY() / (gHandler.getHeight() / 8);
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

    public int getSecondXPos() {
        return secondXPos;
    }

    public int getSecondYPos() {
        return secondYPos;
    }
}
