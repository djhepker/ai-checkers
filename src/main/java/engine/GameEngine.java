package main.java.engine;

import main.java.ai.Agent;
import main.java.game.gameworld.BoardManager;
import main.java.game.graphics.GameWindow;
import main.java.game.graphics.GraphicsHandler;
import main.java.game.gameworld.PieceManager;
import main.java.game.graphics.InputHandler;
import main.java.game.utils.GameBoardPiece;

import java.awt.Image;

public class GameEngine {
    private EntityCreator creator;
    private BoardManager bMgr;
    private PieceManager pMgr;
    private GraphicsHandler graphicsHandler;
    private InputHandler inputHandler;
    private GameWindow window;
    private Agent zero;

    private GameBoardPiece[][] pieces;

    private final Image[] cachedTiles;

    private final boolean lightChoice;
    private final boolean DEBUG = true;

    private boolean playerTurn;

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
        this.playerTurn = true;
        this.zero = new Agent(pieces, lightChoice);
    }

    public void updateGame() {
        inputHandler.update();
        if (playerTurn && inputHandler.movementChosen()) {
            int firstXPos = inputHandler.getFirstXPos();
            int firstYPos = inputHandler.getFirstYPos();
            GameBoardPiece piece = pieces[firstXPos][firstYPos];
            if (DEBUG && piece != null && pMgr.movePiece(piece)) {
                pMgr.updateAllPieces();
                zero.printQueue();
            } else if (piece != null && lightChoice == piece.isLight() && pMgr.movePiece(piece)) {
               pMgr.updateAllPieces();
               playerTurn = !playerTurn;
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
