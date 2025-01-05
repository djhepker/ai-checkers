package graphics;

import entity.DarkPiece;
import entity.Entity;
import entity.LightPiece;
import gameworld.Cell;
import utils.EntityList;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardRenderer extends JPanel {
    private EntityList cells;
    private EntityList pieces;
    private GameWindow gameWindow;

    private int selectedRow;
    private int selectedCol;

    public BoardRenderer(EntityList cells, EntityList pieces) {
        this.cells = cells;
        this.pieces = pieces;
        this.gameWindow = new GameWindow(this);
        Border blackLine = BorderFactory.createLineBorder(Color.BLACK, 10); // 5px black border
        setBorder(blackLine);
        this.selectedCol = -1;
        this.selectedRow = -1;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });
    }

    public boolean windowOpen() {
        return gameWindow.isOpen();
    }

    private void handleMouseClick(int mouseX, int mouseY) {
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int cellWidth = panelWidth / 8;
        int cellHeight = panelHeight / 8;
        int col = mouseX / cellWidth;
        int row = mouseY / cellHeight;

        selectedRow = row;
        selectedCol = col;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        drawPieces(g);

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int cellWidth = panelWidth / 8;
        int cellHeight = panelHeight / 8;

        if (selectedRow != -1 && selectedCol != -1) {
            // Calculate the top-left corner of the selected cell
            int xPos = selectedCol * cellWidth;
            int yPos = selectedRow * cellHeight;

            // Set color for the highlight border
            g.setColor(Color.BLUE);
            //g.setStroke(new BasicStroke(3));

            // Draw a rectangle around the selected cell
            g.drawRect(xPos, yPos, cellWidth, cellHeight);
        }
    }

    private void drawBoard(Graphics g) {
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int cellWidth = panelWidth / 8;
        int cellHeight = panelHeight / 8;

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

        int pieceWidth = panelWidth / 8;
        int pieceHeight = panelHeight / 8;

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
