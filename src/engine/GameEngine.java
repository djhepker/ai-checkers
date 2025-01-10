package engine;

import entity.Entity;
import gameworld.BoardManager;
import graphics.GameWindow;
import graphics.GraphicsHandler;
import gameworld.PieceManager;
import graphics.InputHandler;
import utils.EntityCreator;
import utils.EntityArray;
import utils.GameBoardPiece;

public class GameEngine {
    private EntityCreator creator;
    private BoardManager bMgr;
    private PieceManager pMgr;
    private GraphicsHandler graphicsHandler;
    private InputHandler inputHandler;
    private GameWindow window;

    private EntityArray cells;
    private EntityArray pieces;

    public GameEngine() {
        this.creator = new EntityCreator();
        this.pieces = new EntityArray();
        this.cells = new EntityArray();
        this.bMgr = new BoardManager(cells, creator);
        this.graphicsHandler = new GraphicsHandler(cells, pieces);
        this.inputHandler = graphicsHandler.getInputHandler();
        this.window = graphicsHandler.getGameWindow();
        this.pMgr = new PieceManager(pieces, creator, inputHandler);
    }

    public void updateGame() {
        inputHandler.update();
        if (inputHandler.movementChosen()) {
            int firstXPos = inputHandler.getFirstXPos();
            int firstYPos = inputHandler.getFirstYPos();

            pMgr.movePiece(pieces.getEntity(firstXPos, firstYPos));
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

    public void printAllPiecesInPlay() {
        pMgr.printAllPiecesInPlay();
    }
}
