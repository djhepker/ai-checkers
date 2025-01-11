package main.java.graphics;

import main.java.entity.Entity;
import main.java.utils.GameBoardPiece;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import java.util.Set;
import java.awt.Point;
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
    private Entity[][] cells;
    private GameBoardPiece[][] pieces;
    private GameWindow gameWindow;
    private InputHandler inputHandler;

    private int entityWidth;
    private int entityHeight;
    private int highlightRectangleX;
    private int highlightRectangleY;

    private boolean windowResized;

    public GraphicsHandler(Entity[][] cells, GameBoardPiece[][] pieces) {
        this.cells = cells;
        this.pieces = pieces;
        this.gameWindow = new GameWindow(this);
        this.inputHandler = new InputHandler(this);
        this.entityWidth = 0;
        this.entityHeight = 0;
        this.highlightRectangleX = 0;
        this.highlightRectangleY = 0;
        this.windowResized = true;
        Border blackLine = BorderFactory.createLineBorder(Color.BLACK, 8);
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

            drawHighlightRectangles(g2d);
        }
    }

    // TODO: FIX ADJASCENT HIGHLIGHT BOXES, NOT WORKING AT ALL
    private void drawHighlightRectangles(Graphics2D g2d) {
        g2d.setColor(Color.BLUE);
        g2d.setStroke(new BasicStroke(3));
        int xCoordinate = inputHandler.getSelectedCol();
        int yCoordinate = inputHandler.getSelectedRow();
        highlightRectangleX = getWidth() / 8 * xCoordinate;
        highlightRectangleY = getHeight() / 8 * yCoordinate;

        GameBoardPiece piece = pieces[xCoordinate][yCoordinate];
        if (piece != null) {
            g2d.drawRect(highlightRectangleX, highlightRectangleY, getWidth() / 8, getHeight() / 8);
            Set<Point> legalMoves = piece.getLegalMoves();

            for (Point move : legalMoves) {
                int x = getWidth() / 8 * (int) move.getX();
                int y = getWidth() / 8 * (int) move.getY();
                System.out.println("Point x: " + x);
                System.out.println("Point y: " + y);
                g2d.drawRect(x, y, getWidth() / 8, getHeight() / 8);
            }
        } else {
            inputHandler.resetClicks();
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
                g2d.drawImage(cells[i][j].getSprite(), xPos, yPos, entityWidth, entityHeight, null);
            }
        }
    }

    private void drawPieces(Graphics2D g2d) {
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                if (pieces[i][j] != null) {
                    GameBoardPiece piece = pieces[i][j];
                    int xPos = piece.getX() * entityWidth;
                    int yPos = piece.getY() * entityHeight;
                    g2d.drawImage(piece.getSprite(), xPos, yPos, entityWidth, entityHeight, null);
                    Set<Point> legalMoves = piece.getLegalMoves();
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
