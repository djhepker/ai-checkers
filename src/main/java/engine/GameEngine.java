package main.java.engine;

import main.java.game.ai.Agent;
import main.java.game.gameworld.BoardManager;
import main.java.game.graphics.GameWindow;
import main.java.game.graphics.GraphicsHandler;
import main.java.game.gameworld.PieceManager;
import main.java.game.graphics.InputHandler;
import main.java.game.utils.GameBoardPiece;

public class GameEngine {
    private EntityCreator creator;
    private BoardManager bMgr;
    private PieceManager pMgr;
    private GraphicsHandler graphicsHandler;
    private InputHandler inputHandler;
    private GameWindow window;
    private Agent zero;

    private final boolean lightChoice;
    private final boolean DEBUG = true;

    private boolean playerTurn;

    public GameEngine() {
        loadGameWorld();
        renderUI();
        this.lightChoice = window.lightChosen();
        this.zero = new Agent(pMgr, lightChoice);
        this.playerTurn = lightChoice;
    }

    public void updateGame() {
        inputHandler.update();
        if (playerTurn) {
            handleInput();
        }
        graphicsHandler.repaint();
    }

    public boolean isOpen() {
        return window.isOpen();
    }

    private void handleInput() {
        if (inputHandler.movementChosen()) {
            int firstXPos = inputHandler.getFirstXPos();
            int firstYPos = inputHandler.getFirstYPos();
            GameBoardPiece piece = pMgr.getPiece(firstXPos, firstYPos);
            if (DEBUG && piece != null && pMgr.movePiece(piece)) {
                pMgr.updateAllPieces();
            } else if (piece != null && lightChoice == piece.isLight() && pMgr.movePiece(piece)) {
                pMgr.updateAllPieces();
                // playerTurn = !playerTurn;
            }
            inputHandler.resetClicks();
            printSelectedPiece();
        }
    }

    private void loadGameWorld() {
        this.inputHandler = new InputHandler();
        this.creator = new EntityCreator();
        this.pMgr = new PieceManager(creator, inputHandler);
    }

    private void renderUI() {
        this.bMgr = new BoardManager(creator);
        this.graphicsHandler = new GraphicsHandler(bMgr.getCachedTiles(), pMgr, inputHandler);
        this.inputHandler.setGraphicsHandler(graphicsHandler);
        this.window = new GameWindow(graphicsHandler);
        this.window.showPopUpColorDialog();
    }

    private void printSelectedPiece() {
        GameBoardPiece piece = pMgr.getPiece(inputHandler.getFirstXPos(), inputHandler.getFirstYPos());
        if (piece != null) {
            piece.printData();
        }
    }

    public void printAllPiecesInPlay() {
        pMgr.printAllPiecesInPlay();
    }
}
