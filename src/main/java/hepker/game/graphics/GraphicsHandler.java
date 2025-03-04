package hepker.game.graphics;

import hepker.game.entity.movement.ActionNode;
import hepker.game.gameworld.PieceManager;
import hepker.game.entity.GameBoardPiece;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public final class GraphicsHandler extends JPanel {
    private final InputHandler inputHandler;

    private final Image[] cachedTiles;
    private final GameBoardPiece[] displayPieces;

    private int entityWidth;
    private int entityHeight;
    private int highlightRectangleX;
    private int highlightRectangleY;

    private boolean lightChosen;
    private final JFrame frame;

    public GraphicsHandler(Image[] inputTileImgs, PieceManager inputPMgr, InputHandler inputInputHandler) {
        this.inputHandler = inputInputHandler;
        this.cachedTiles = inputTileImgs;
        this.displayPieces = inputPMgr.getDisplayPieces();
        this.entityWidth = 0;
        this.entityHeight = 0;
        this.highlightRectangleX = 0;
        this.highlightRectangleY = 0;
        this.lightChosen = false;
        Border blackLine = BorderFactory.createLineBorder(Color.BLACK, 8);
        setBorder(blackLine);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateEntitySize();
                super.componentResized(e);
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                inputInputHandler.handleMouseClick(e, getWidth(), getHeight());
            }
        });
        this.frame = new JFrame("Checkers dev");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.add(this);
        this.frame.setSize(800, 800);
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
        this.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawBoard(g2d);
        drawPieces(g2d);
        if (inputHandler.hasSelectedPiece()) {
            drawHighlightRectangles(g2d);
        }
    }

    public boolean windowOpen() {
        return frame.isVisible();
    }

    public boolean showPopUpColorDialog() {
        try {
            String[] colors = {"White", "Black"};
            String selectedColor = (String) JOptionPane.showInputDialog(
                    this, "Choose your battle color", "Checkers", JOptionPane.QUESTION_MESSAGE,
                    null, colors, colors[0]);
            lightChosen = selectedColor.equals("White");
            return lightChosen;
        } catch (Exception e) {
            frame.dispose();
        }
        return false;
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
            frame.dispose();
            System.exit(0);
        }
        return selectedGameMode;
    }

    private void drawHighlightRectangles(Graphics2D g2d) {
        g2d.setColor(Color.BLUE);
        g2d.setStroke(new BasicStroke(3));
        int xCoordinate = inputHandler.getSelectedCol();
        int yCoordinate = inputHandler.getSelectedRow();
        highlightRectangleX = getWidth() / 8 * xCoordinate;
        highlightRectangleY = getHeight() / 8 * yCoordinate;
        GameBoardPiece piece = displayPieces[yCoordinate * 8 + xCoordinate];
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
                GameBoardPiece piece = displayPieces[8 * j + i];
                if (piece != null) {
                    int xPos = piece.getX() * entityWidth;
                    int yPos = piece.getY() * entityHeight;
                    g2d.drawImage(piece.getSprite(), xPos, yPos, entityWidth, entityHeight, null);
                }
            }
        }
    }
}