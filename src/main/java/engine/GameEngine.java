package main.java.engine;

import main.java.ai.NPCManager;
import main.java.game.graphics.GraphicsHandler;
import main.java.game.gameworld.PieceManager;
import main.java.game.graphics.InputHandler;
import main.java.game.entity.GameBoardPiece;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameEngine {

    private static final Logger logger = LoggerFactory.getLogger(GameEngine.class);

    private final boolean LIGHT_CHOICE;
    private final boolean HAS_PLAYER;
    private final boolean IS_TRAINING;

    private EntityCreator creator;
    private PieceManager pMgr;
    private GraphicsHandler graphicsHandler;
    private InputHandler inputHandler;
    private NPCManager npcMgr;

    private boolean playerTurn;
    private boolean gameOver;

    public GameEngine(boolean isTraining) {
        this.IS_TRAINING = isTraining;
        loadGameWorld();
        if (!IS_TRAINING) {
            renderUI();
        }
        if (IS_TRAINING) {
            this.LIGHT_CHOICE = true;
            this.HAS_PLAYER = false;
            this.npcMgr = new NPCManager(pMgr, LIGHT_CHOICE, "Agent Vs Stochastic");
        } else {
            String gameMode = graphicsHandler.showGameModeDialog();
            this.HAS_PLAYER = gameMode.endsWith("Player");
            if (HAS_PLAYER) {
                this.LIGHT_CHOICE = this.graphicsHandler.showPopUpColorDialog();
            } else {
                this.LIGHT_CHOICE = false;
            }
            this.playerTurn = LIGHT_CHOICE;
            this.npcMgr = new NPCManager(pMgr, LIGHT_CHOICE, gameMode);
        }
        this.gameOver = false;
    }

    public void updateGame() {
        try {
            if (HAS_PLAYER) {
                inputHandler.update();
                if (playerTurn) { // TODO: Continue error logging here
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
            if (!IS_TRAINING) {
                graphicsHandler.repaint();
            }
        } catch (AssertionError e) {
            logger.error("Invalid mouse selection in InputHandler" , e);
        }
    }

    public boolean gameOver() {
        if (!IS_TRAINING) {
            if (!graphicsHandler.windowOpen() || gameOver) {
                this.gameOver = true;
                if (LIGHT_CHOICE) {
                    npcMgr.finishGame(pMgr.getNumLight() == 0);
                } else {
                    npcMgr.finishGame(pMgr.getNumDusky() == 0);
                }
            }
        } else if (gameOver) {
            if (LIGHT_CHOICE) {
                npcMgr.finishGame(pMgr.getNumLight() == 0);
            } else {
                npcMgr.finishGame(pMgr.getNumDusky() == 0);
            }
        }
        return gameOver;
    }

    private void handleInput() {
        if (inputHandler.movementChosen()) {
            int firstXPos = inputHandler.getFirstXPos();
            int firstYPos = inputHandler.getFirstYPos();
            GameBoardPiece piece = pMgr.getPiece(firstXPos, firstYPos);
            if (piece != null
                    && LIGHT_CHOICE == piece.isLight()
                    && pMgr.movePiece(piece,
                            inputHandler.getSelectedCol(), inputHandler.getSelectedRow(),
                            inputHandler.getFirstXPos(), inputHandler.getFirstYPos())) {

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
        this.pMgr = new PieceManager(creator);
    }

    private void renderUI() {
        this.graphicsHandler = new GraphicsHandler(creator.getCachedCells(), pMgr, inputHandler);
    }

    private void printSelectedPiece() {
        GameBoardPiece piece = pMgr.getPiece(inputHandler.getFirstXPos(), inputHandler.getFirstYPos());
        if (piece != null) {
            piece.printData();
        }
    }
}
