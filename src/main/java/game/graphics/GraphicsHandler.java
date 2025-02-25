package main.java.game.graphics;

import main.java.game.entity.movement.ActionNode;
import main.java.game.gameworld.PieceManager;
import main.java.game.entity.GameBoardPiece;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.Image;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.event.*;

public class GraphicsHandler extends JPanel {

    private InputHandler inputHandler;
    private PieceManager pMgr;

    private final Image[] cachedTiles;

    private int entityWidth;
    private int entityHeight;
    private int highlightRectangleX;
    private int highlightRectangleY;

    private boolean windowOpen;
    private boolean lightChosen;
    private final JFrame frame;


    private boolean windowResized;

    public GraphicsHandler(Image[] cachedTiles,
                           PieceManager pMgr,
                           InputHandler inputHandler) {
        this.inputHandler = inputHandler;
        this.cachedTiles = cachedTiles;
        this.pMgr = pMgr;
        this.entityWidth = 0;
        this.entityHeight = 0;
        this.highlightRectangleX = 0;
        this.highlightRectangleY = 0;
        this.windowResized = true;
        this.lightChosen = false;

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

        this.frame = new JFrame("Checkers dev");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.add(this);
        this.frame.setSize(800,800);
        this.frame.setLocationRelativeTo(null); // centered
        this.frame.setVisible(true);
        this.windowOpen = true;

        this.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                windowOpen = false;
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

    public void showPopUpColorDialog() {
        try {
            String[] colors = {"White", "Black"};
            String selectedColor = (String) JOptionPane.showInputDialog(
                    this, "Choose your battle color", "Checkers", JOptionPane.QUESTION_MESSAGE,
                    null, colors, colors[0]);
            lightChosen = selectedColor.equals("White");
        } catch (Exception e) {
            System.out.println("Color dialog was closed");
            System.exit(0);
        }
    }

    public String showGameModeDialog() {
        String[] playerOptions = {"Agent Vs Stochastic", "Agent Vs Player", "Stochastic Vs Player"};
        String selectedGameMode = (String) JOptionPane.showInputDialog(
                this,
                "Choose who will be playing",
                "Checkers",
                JOptionPane.QUESTION_MESSAGE,
                null, playerOptions, playerOptions[0]);
        if (selectedGameMode == null) {
            System.out.println("Game Mode Dialog closed.");
            frame.dispose();
            System.exit(0);
        }
        return selectedGameMode;
    }

    public boolean lightChosen() {
        return lightChosen;
    }

    public boolean windowOpen() {
        return windowOpen;
    }

    private void drawHighlightRectangles(Graphics2D g2d) {
        g2d.setColor(Color.BLUE);
        g2d.setStroke(new BasicStroke(3));
        int xCoordinate = inputHandler.getSelectedCol();
        int yCoordinate = inputHandler.getSelectedRow();
        highlightRectangleX = getWidth() / 8 * xCoordinate;
        highlightRectangleY = getHeight() / 8 * yCoordinate;
        GameBoardPiece piece = pMgr.getPiece(xCoordinate, yCoordinate);
        if (piece != null) {
            g2d.drawRect(highlightRectangleX, highlightRectangleY, getWidth() / 8, getHeight() / 8);
            ActionNode cursor = piece.getMoveListPointer();
            while (cursor != null) {
                int x = getWidth() / 8 * cursor.getfDataX();
                int y = getHeight() / 8 * cursor.getfDataY();
                g2d.drawRect(x, y, getWidth() / 8, getHeight() / 8);
                cursor = cursor.getNext();
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
                int tileIndex = (i + j) & 1;
                g2d.drawImage(cachedTiles[tileIndex], xPos, yPos, entityWidth, entityHeight, null);
            }
        }
    }

    private void drawPieces(Graphics2D g2d) {
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                if (!pMgr.spaceIsNull(i, j)) {
                    GameBoardPiece piece = pMgr.getPiece(i, j);
                    int xPos = piece.getX() * entityWidth;
                    int yPos = piece.getY() * entityHeight;
                    g2d.drawImage(piece.getSprite(), xPos, yPos, entityWidth, entityHeight, null);
                }
            }
        }
    }
}
