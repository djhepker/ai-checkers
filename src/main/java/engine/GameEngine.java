package main.java.engine;

import main.java.entity.Entity;
import main.java.gameworld.BoardManager;
import main.java.graphics.GameWindow;
import main.java.graphics.GraphicsHandler;
import main.java.gameworld.PieceManager;
import main.java.graphics.InputHandler;
import main.java.utils.GameBoardPiece;

public class GameEngine {
    private EntityCreator creator;
    private BoardManager bMgr;
    private PieceManager pMgr;
    private GraphicsHandler graphicsHandler;
    private InputHandler inputHandler;
    private GameWindow window;

    private Entity[][] cells;
    private Entity[][] pieces;

    public GameEngine() {
        this.pieces = new Entity[8][8];
        this.cells = new Entity[8][8];
        this.creator = new EntityCreator(pieces);
        this.bMgr = new BoardManager(cells, creator);
        this.graphicsHandler = new GraphicsHandler(cells, pieces);
        this.inputHandler = graphicsHandler.getInputHandler();
        this.pMgr = new PieceManager(pieces, creator, inputHandler);
        this.window = graphicsHandler.getGameWindow();
    }

    public void updateGame() {
        inputHandler.update();
        if (inputHandler.movementChosen()) {
            int firstXPos = inputHandler.getFirstXPos();
            int firstYPos = inputHandler.getFirstYPos();

            pMgr.movePiece(pieces[firstXPos][firstYPos]);
            inputHandler.resetClicks();
            printSelectedPiece();
        }
        graphicsHandler.repaint();
    }

    public boolean isOpen() {
        return window.isOpen();
    }

    private void printSelectedPiece() {
        int xCell = inputHandler.getFirstXPos();
        int yCell = inputHandler.getFirstYPos();

        Entity e = pMgr.getPiece(xCell, yCell);
        if (e instanceof GameBoardPiece) {
            GameBoardPiece piece = (GameBoardPiece) e;
            piece.printData();
        }

    }

    public void printAllCellsInPlay() {
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                Entity e = cells[i][j];
                System.out.println("Name: " + e.getName() + "; Coordinates: " + e.getX() + ", " + e.getY());
            }
        }
    }

    public void printAllPiecesInPlay() {
        pMgr.printAllPiecesInPlay();
    }
}
