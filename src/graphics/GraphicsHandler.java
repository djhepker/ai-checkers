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
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class GraphicsHandler extends JPanel {
    private EntityList cells;
    private EntityList pieces;
    private GameWindow gameWindow;

    private int selectedRow;
    private int selectedCol;

    public GraphicsHandler(EntityList cells, EntityList pieces) {
        this.cells = cells;
        this.pieces = pieces;
        this.gameWindow = new GameWindow(this);
        Border blackLine = BorderFactory.createLineBorder(Color.BLACK, 10); // 10px black border
        setBorder(blackLine);
        this.selectedCol = -1;
        this.selectedRow = -1;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });
    }

    public boolean windowOpen() {
        return gameWindow.isOpen();
    }

    private void handleMouseClick(int mouseX, int mouseY) {
        selectedRow = mouseY / (getHeight() / 8);
        selectedCol = mouseX / (getWidth() / 8);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        drawBoard(g2d);
        drawPieces(g2d);

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int cellWidth = panelWidth / 8;
        int cellHeight = panelHeight / 8;

        if (selectedRow != -1 && selectedCol != -1) {
            int xPos = selectedCol * cellWidth;
            int yPos = selectedRow * cellHeight;

            // Set stroke and color
            g2d.setColor(Color.BLUE);
            g2d.setStroke(new BasicStroke(3)); // 3-pixel wide stroke

            // Draw a rectangle around the selected cell
            g2d.drawRect(xPos, yPos, cellWidth, cellHeight);
        }
    }

    private void drawBoard(Graphics2D g2d) {
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int cellWidth = panelWidth / 8;
        int cellHeight = panelHeight / 8;

        for (Entity entity : cells) {
            Cell cell = (Cell) entity;
            int xPos = cell.getX() * cellWidth;
            int yPos = cell.getY() * cellHeight;
            g2d.drawImage(cell.getSprite(), xPos, yPos, cellWidth, cellHeight, null); // Resize the cell
        }
    }

    private void drawPieces(Graphics2D g2d) {
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int pieceWidth = panelWidth / 8;
        int pieceHeight = panelHeight / 8;

        for (Entity entity : pieces) {
            if (entity.getName().equals("DarkPiece")) {
                DarkPiece dark = (DarkPiece) entity;
                int xPos = dark.getX() * pieceWidth;
                int yPos = dark.getY() * pieceHeight;
                g2d.drawImage(dark.getSprite(), xPos, yPos, pieceWidth, pieceHeight, null);
            } else if (entity.getName().equals("LightPiece")) {
                LightPiece light = (LightPiece) entity;
                int xPos = light.getX() * pieceWidth;
                int yPos = light.getY() * pieceHeight;
                g2d.drawImage(light.getSprite(), xPos, yPos, pieceWidth, pieceHeight, null);
            }
        }
    }
}
