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

public class GraphicsHandler extends JPanel {
    private EntityList cells;
    private EntityList pieces;
    private GameWindow gameWindow;
    private InputHandler inHndlr;

    public GraphicsHandler(EntityList cells, EntityList pieces) {
        this.cells = cells;
        this.pieces = pieces;
        this.gameWindow = new GameWindow(this);
        this.inHndlr = new InputHandler(this);

        Border blackLine = BorderFactory.createLineBorder(Color.BLACK, 8); // 10px black border
        setBorder(blackLine);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                inHndlr.handleMouseClick(e);
            }
        });
    }

    public boolean windowOpen() {
        return gameWindow.isOpen();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        drawBoard(g2d);
        drawPieces(g2d);
        inHndlr.update();
        if (inHndlr.cellIsChosen()) {
            g2d.setColor(Color.BLUE);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(inHndlr.getXPosCell(), inHndlr.getYPosCell(), getWidth() / 8, getHeight() / 8);
            inHndlr.setCellChosen(false);
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
