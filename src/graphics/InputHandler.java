package graphics;

import java.awt.event.MouseEvent;

public class InputHandler {
    private GraphicsHandler gHandler;
    private int selectedRow;
    private int selectedCol;
    private int yPosCell;
    private int xPosCell;
    private boolean cellChosen;

    public InputHandler(GraphicsHandler gHandler) {
        this.gHandler = gHandler;
        this.selectedCol = -1;
        this.selectedRow = -1;
        this.xPosCell = -1;
        this.yPosCell = -1;
        this.cellChosen = false;
    }

    public void update() {
        if (selectedRow != -1 && selectedCol != -1) {
            xPosCell = selectedCol * (gHandler.getWidth() / 8);
            yPosCell = selectedRow * (gHandler.getHeight() / 8);
        }
    }

    public void handleMouseClick(MouseEvent e) {
        selectedRow = e.getY() / (gHandler.getHeight() / 8);
        selectedCol = e.getX() / (gHandler.getWidth() / 8);
        cellChosen = true;
    }

    public void setCellChosen(boolean isCellChosen) {
        cellChosen = isCellChosen;
    }

    public boolean cellIsChosen() {
        return cellChosen;
    }

    public int getYPosCell() {
        return yPosCell;
    }

    public int getXPosCell() {
        return xPosCell;
    }
}
