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
    private boolean firstClick;
    private boolean secondClick;

    public InputHandler(GraphicsHandler gHandler) {
        this.gHandler = gHandler;
        this.selectedCol = -1;
        this.selectedRow = -1;
        this.firstXPos = -1;
        this.firstYPos = -1;
        this.secondXPos = -1;
        this.secondYPos = -1;
        this.firstClick = false;
        this.secondClick = false;
    }

    public void update() {
        if (firstClick) {
            firstXPos = selectedCol * (gHandler.getWidth() / 8);
            firstYPos = selectedRow * (gHandler.getHeight() / 8);
        } else if (secondClick) {
            System.out.println("Second action detected");
            secondXPos = selectedCol * (gHandler.getWidth() / 8);
            secondYPos = selectedRow * (gHandler.getHeight() / 8);
        }
    }

    public void handleMouseClick(MouseEvent e) {
        if (!firstClick) {
            firstClick = true;
        } else {
            secondClick = true;
        }
        selectedRow = e.getY() / (gHandler.getHeight() / 8);
        selectedCol = e.getX() / (gHandler.getWidth() / 8);
    }

    public void resetClicks() {
        firstClick = false;
        secondClick = false;
    }

    public boolean movementChosen() {
        return secondClick;
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
