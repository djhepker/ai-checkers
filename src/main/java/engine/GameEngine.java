package main.java.engine;

import main.java.gameworld.BoardManager;
import main.java.graphics.GameWindow;
import main.java.graphics.GraphicsHandler;
import main.java.gameworld.PieceManager;
import main.java.graphics.InputHandler;
import main.java.utils.GameBoardPiece;

import java.awt.Image;

public class GameEngine {
    private EntityCreator creator;
    private BoardManager bMgr;
    private PieceManager pMgr;
    private GraphicsHandler graphicsHandler;
    private InputHandler inputHandler;
    private GameWindow window;

    private GameBoardPiece[][] pieces;

    private final Image[] cachedTiles;

    private final boolean lightChoice;

    public GameEngine() {
        this.pieces = new GameBoardPiece[8][8];
        this.creator = new EntityCreator(pieces);
        this.bMgr = new BoardManager(creator);
        this.cachedTiles = bMgr.getCachedTiles();

        this.inputHandler = new InputHandler();
        this.graphicsHandler = new GraphicsHandler(cachedTiles, pieces, inputHandler);
        this.inputHandler.setGraphicsHandler(graphicsHandler);
        this.pMgr = new PieceManager(pieces, creator, inputHandler);

        this.window = new GameWindow(graphicsHandler);
        this.window.showPopUpColorDialog();

        this.lightChoice = window.lightChosen();
    }

    public void updateGame() {
        inputHandler.update();
        if (inputHandler.movementChosen()) {
            int firstXPos = inputHandler.getFirstXPos();
            int firstYPos = inputHandler.getFirstYPos();
            GameBoardPiece piece = pieces[firstXPos][firstYPos];
            if (piece != null && lightChoice == piece.isLight() && pMgr.movePiece(piece)) {
               pMgr.updateAllPieces();
            }
            inputHandler.resetClicks();
            printSelectedPiece();
        }
        graphicsHandler.repaint();
    }

    public boolean isOpen() {
        return window.isOpen();
    }

    private void printSelectedPiece() {
        GameBoardPiece piece = pieces[inputHandler.getFirstXPos()][inputHandler.getFirstYPos()];
        if (piece != null) {
            piece.printData();
        }
    }

    public void printAllPiecesInPlay() {
        pMgr.printAllPiecesInPlay();
    }
}
