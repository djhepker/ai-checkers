package main.java.graphics;

import main.java.entity.Entity;
import main.java.utils.EntityArray;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// TODO make border outline resizable
public class GraphicsHandler extends JPanel {
    private EntityArray cells;
    private EntityArray pieces;
    private GameWindow gameWindow;
    private InputHandler inputHandler;

    private int entityWidth;
    private int entityHeight;
    private int highlightRectangleX;
    private int highlightRectangleY;

    private boolean windowResized;

    public GraphicsHandler(EntityArray cells, EntityArray pieces) {
        this.cells = cells;
        this.pieces = pieces;
        this.gameWindow = new GameWindow(this);
        this.inputHandler = new InputHandler(this);
        this.entityWidth = 0;
        this.entityHeight = 0;
        this.highlightRectangleX = 0;
        this.highlightRectangleY = 0;
        this.windowResized = true;
        Border blackLine = BorderFactory.createLineBorder(Color.BLACK, 8); // 10px black border
        setBorder(blackLine);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                windowResized = true;
                super.componentResized(e);
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                inputHandler.handleMouseClick(e);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (windowResized) {
            updateEntitySize();
        }
        drawBoard(g2d);
        drawPieces(g2d);
        if (inputHandler.hasSelectedPiece()) {
            g2d.setColor(Color.BLUE);
            g2d.setStroke(new BasicStroke(3));
            highlightRectangleX = inputHandler.getSelectedCol() * (getWidth() / 8);
            highlightRectangleY = inputHandler.getSelectedRow() * (getHeight()/ 8);
            g2d.drawRect(highlightRectangleX, highlightRectangleY, getWidth() / 8, getHeight() / 8);
        }
    }

    private void updateEntitySize() {
        entityWidth = getWidth() / 8;
        entityHeight = getHeight() / 8;
        windowResized = false;
    }

    private void drawBoard(Graphics2D g2d) {
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                int xPos = i * entityWidth;
                int yPos = j * entityHeight;
                g2d.drawImage(cells.getEntity(i, j).getSprite(), xPos, yPos, entityWidth, entityHeight, null); // Resize the cell
            }
        }
    }

    private void drawPieces(Graphics2D g2d) {
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                if (!pieces.spaceIsNull(i, j)) {
                    Entity entity = pieces.getEntity(i, j);
                    int xPos = entity.getX() * entityWidth;
                    int yPos = entity.getY() * entityHeight;
                    g2d.drawImage(entity.getSprite(), xPos, yPos, entityWidth, entityHeight, null);
                }
            }
        }
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }

    public GameWindow getGameWindow() {
        return gameWindow;
    }
}
