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
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GraphicsHandler extends JPanel {
    private EntityList cells;
    private EntityList pieces;
    private GameWindow gameWindow;
    private InputHandler inputHandler;

    private int entityWidth;
    private int entityHeight;
    private boolean windowResized;


    public GraphicsHandler(EntityList cells, EntityList pieces) {
        this.cells = cells;
        this.pieces = pieces;
        this.gameWindow = new GameWindow(this);
        this.inputHandler = new InputHandler(this);
        this.entityWidth = 0;
        this.entityHeight = 0;
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
        if (inputHandler.getFirstXPos() != -1) {
            g2d.setColor(Color.BLUE);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(inputHandler.getFirstXPos(), inputHandler.getFirstYPos(), getWidth() / 8, getHeight() / 8);
        }
    }

    private void updateEntitySize() {
        entityWidth = getWidth() / 8;
        entityHeight = getHeight() / 8;
        windowResized = false;
    }

    private void drawBoard(Graphics2D g2d) {
        for (Entity entity : cells) {
            Cell cell = (Cell) entity;
            int xPos = cell.getX() * entityWidth;
            int yPos = cell.getY() * entityHeight;
            g2d.drawImage(cell.getSprite(), xPos, yPos, entityWidth, entityHeight, null); // Resize the cell
        }
    }

    private void drawPieces(Graphics2D g2d) {for (Entity entity : pieces) {
            if (entity.getName().equals("DarkPiece")) {
                DarkPiece dark = (DarkPiece) entity;
                int xPos = dark.getX() * entityWidth;
                int yPos = dark.getY() * entityHeight;
                g2d.drawImage(dark.getSprite(), xPos, yPos, entityWidth, entityHeight, null);
            } else if (entity.getName().equals("LightPiece")) {
                LightPiece light = (LightPiece) entity;
                int xPos = light.getX() * entityWidth;
                int yPos = light.getY() * entityHeight;
                g2d.drawImage(light.getSprite(), xPos, yPos, entityWidth, entityHeight, null);
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
