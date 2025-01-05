package gameworld;

import entity.DarkPiece;
import entity.Entity;
import entity.LightPiece;
import utils.EntityList;
import utils.GameWindow;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
/*
* Handles draw logic for our checkersboard
* */
public class BoardRenderer extends JPanel {
    private EntityList cells;
    private EntityList pieces;
    private GameWindow gameWindow;

    public BoardRenderer(EntityList cells, EntityList pieces) {
        this.cells = cells;
        this.pieces = pieces;
        this.gameWindow = new GameWindow(this);
        Border blackLine = BorderFactory.createLineBorder(Color.BLACK, 10); // 5px black border
        setBorder(blackLine);
    }

    public boolean windowOpen() {
        return gameWindow.isOpen();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        drawPieces(g);
    }

    private void drawBoard(Graphics g) {
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int cellWidth = panelWidth / 8;   // 8 columns on the board
        int cellHeight = panelHeight / 8; // 8 rows on the board

        for (Entity entity : cells) {
            Cell cell = (Cell) entity;
            int xPos = cell.getX() * cellWidth;
            int yPos = cell.getY() * cellHeight;
            g.drawImage(cell.getSprite(), xPos, yPos, cellWidth, cellHeight, null); // Resize the cell
        }
    }


    private void drawPieces(Graphics g) {
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int pieceWidth = panelWidth / 8;   // 8 columns on the board
        int pieceHeight = panelHeight / 8; // 8 rows on the board

        for (Entity entity : pieces) {
            if (entity.getName() == "DarkPiece") {
                DarkPiece dark = (DarkPiece) entity;
                int xPos = dark.getX() * pieceWidth;
                int yPos = dark.getY() * pieceHeight;
                g.drawImage(dark.getSprite(), xPos, yPos, pieceWidth, pieceHeight, null);
            } else if (entity.getName() == "LightPiece") {
                LightPiece light = (LightPiece) entity;
                int xPos = light.getX() * pieceWidth;
                int yPos = light.getY() * pieceHeight;
                g.drawImage(light.getSprite(), xPos, yPos, pieceWidth, pieceHeight, null);
            }
        }
    }
}
