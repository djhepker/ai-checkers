package main.java.engine;

import main.java.ai.NPCManager;
import main.java.game.gameworld.BoardManager;
import main.java.game.graphics.GameWindow;
import main.java.game.graphics.GraphicsHandler;
import main.java.game.gameworld.PieceManager;
import main.java.game.graphics.InputHandler;
import main.java.game.entity.GameBoardPiece;

public class GameEngine {
    private EntityCreator creator;
    private BoardManager bMgr;
    private PieceManager pMgr;
    private GraphicsHandler graphicsHandler;
    private InputHandler inputHandler;
    private GameWindow window;
    private NPCManager npcMgr;

    private final boolean LIGHT_CHOICE;
    private final boolean HAS_PLAYER;
    private final boolean IS_TRAINING;

    private boolean playerTurn;
    private boolean gameOver;

    public GameEngine(boolean isTraining) {
        this.IS_TRAINING = isTraining;
        loadGameWorld();
        renderUI();
        if (IS_TRAINING) {
            this.LIGHT_CHOICE = true;
            this.HAS_PLAYER = false;
            this.npcMgr = new NPCManager(pMgr, LIGHT_CHOICE, "Agent Vs Stochastic");
        } else {
            String gameMode = window.showGameModeDialog();
            this.HAS_PLAYER = gameMode.endsWith("Player");
            if (HAS_PLAYER) {
                this.window.showPopUpColorDialog();
            }
            this.LIGHT_CHOICE = !HAS_PLAYER || window.lightChosen();
            this.playerTurn = LIGHT_CHOICE;
            this.npcMgr = new NPCManager(pMgr, LIGHT_CHOICE, gameMode);
        }
        this.gameOver = false;
    }

    public void updateGame() {
        if (HAS_PLAYER) {
            inputHandler.update();
            if (playerTurn) {
                handleInput();
            } else {
                npcMgr.update();
                playerTurn = !playerTurn;
            }
        } else {
            npcMgr.update();
        }
        if (pMgr.sideDefeated()) {
            this.gameOver = true;
        }
        graphicsHandler.repaint();
    }

    public boolean gameOver() {
        if (!window.isOpen() || gameOver) {
            this.gameOver = true;
            if (window.lightChosen() || npcMgr.isStochasticVsAgent()) {
                npcMgr.finishGame(pMgr.getNumLight() == 0);
            } else {
                npcMgr.finishGame(pMgr.getNumDusky() == 0);
            }
            if (IS_TRAINING) {
                window.close();
            }
        }
        return gameOver;
    }

    private void handleInput() {
        if (inputHandler.movementChosen()) {
            int firstXPos = inputHandler.getFirstXPos();
            int firstYPos = inputHandler.getFirstYPos();
            GameBoardPiece piece = pMgr.getPiece(firstXPos, firstYPos);
            if (piece != null && LIGHT_CHOICE == piece.isLight() && pMgr.movePiece(piece)) {
                if (piece.isReadyForPromotion()) {
                    pMgr.promotePiece(piece);
                }
                pMgr.updateAllPieces();
                playerTurn = !playerTurn;
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
