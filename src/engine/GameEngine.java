package engine;

import entity.Entity;
import entity.LightPiece;
import gameworld.BoardManager;
import graphics.GameWindow;
import graphics.GraphicsHandler;
import gameworld.PieceManager;
import graphics.InputHandler;
import utils.EntityCreator;
import utils.EntityList;

public class GameEngine {
    private EntityCreator creator;
    private BoardManager bMgr;
    private PieceManager pMgr;
    private GraphicsHandler graphicsHandler;
    private InputHandler inputHandler;
    private GameWindow window;

    private EntityList cells;
    private EntityList pieces;

    public GameEngine() {
        this.creator = new EntityCreator();
        this.pieces = new EntityList();
        this.cells = new EntityList();
        this.bMgr = new BoardManager(cells, creator);
        this.pMgr = new PieceManager(pieces, creator);
        this.graphicsHandler = new GraphicsHandler(cells, pieces);
        this.inputHandler = graphicsHandler.getInputHandler();
        this.window = graphicsHandler.getGameWindow();

        for (entity.Entity piece : pieces) {
            updateCellUse(piece.getX(), piece.getY());
        }
    }

    public void updateGame() {
        inputHandler.update();
        if (inputHandler.movementChosen()) {
            movePiece(inputHandler.getFirstXPos(), inputHandler.getFirstYPos());
            inputHandler.resetClicks();
        }
        graphicsHandler.repaint();
    }

    private void movePiece(int x, int y) {
        for (Entity e : pieces) {
            System.out.println("e.getX(): " + e.getX() + " x: " + x);
            if (e.getX() == x && e.getY() == y) {
                if (e instanceof LightPiece) {
                    LightPiece movementPiece = (LightPiece) e;
                    int[][] possibleMoves = movementPiece.getTheoreticalMoves();
                    for (int i = 0; i < possibleMoves.length; i++) {
                        if (possibleMoves[i][0] == x && possibleMoves[i][1] == y) {
                            System.out.println(
                                    "Match found at: (" + possibleMoves[i][0] + ", " + possibleMoves[i][1] + ")");
                            return;
                        }
                    }
                } else {
                    break;
                }
            }
        }
    }

    public boolean isOpen() {
        return window.isOpen();
    }

    private void updateCellUse(int x, int y) {
        bMgr.updateSpace(x, y);
    }

    public void printAllCellsUsage() {
        bMgr.printAllIsCellOccupied();
    }

    public void printAllPiecesInPlay() {
        pMgr.printAllPiecesInPlay();
    }
}
