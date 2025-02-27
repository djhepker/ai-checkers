package hepker.engine;

import hepker.engine.agentintegration.AIEngine;
import hepker.game.graphics.GraphicsHandler;
import hepker.game.gameworld.PieceManager;
import hepker.game.graphics.InputHandler;
import hepker.game.entity.GameBoardPiece;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameEngine {

    private static final Logger logger = LoggerFactory.getLogger(GameEngine.class);

    private EntityCreator creator;
    private PieceManager pMgr;
    private GraphicsHandler graphicsHandler;
    private InputHandler inputHandler;
    private AIEngine agentMgr;

    private final boolean LIGHT_CHOICE;
    private final boolean HAS_PLAYER;
    private final boolean IS_TRAINING;

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
            this.agentMgr = new AIEngine(pMgr, LIGHT_CHOICE, "Agent Vs Stochastic");
        } else {
            String gameMode = graphicsHandler.showGameModeDialog();
            this.HAS_PLAYER = gameMode.endsWith("Player");
            if (HAS_PLAYER) {
                this.LIGHT_CHOICE = this.graphicsHandler.showPopUpColorDialog();
            } else {
                this.LIGHT_CHOICE = false;
            }
            this.playerTurn = LIGHT_CHOICE;
            this.agentMgr = new AIEngine(pMgr, LIGHT_CHOICE, gameMode);
        }
        this.gameOver = false;
    }

    public void updateGame() {
        try {
            if (HAS_PLAYER) {
                inputHandler.update();
                if (playerTurn) {
                    handleInput();
                } else {
                    agentMgr.update();
                    playerTurn = !playerTurn;
                }
            } else {
                agentMgr.update();
            }
            if (pMgr.sideDefeated()) {
                this.gameOver = true;
            }
            if (!IS_TRAINING) {
                graphicsHandler.repaint();
            }
        } catch (Exception e) {
            logger.error("Unexpected error in updateGame()" , e);
        } catch (AssertionError e) {
            logger.error("Invalid mouse selection in InputHandler" , e);
        }
    }

    public boolean gameOver() {
        if (!IS_TRAINING) {
            if (!graphicsHandler.windowOpen() || gameOver) {
                this.gameOver = true;
                if (LIGHT_CHOICE) {
                    agentMgr.finishGame(pMgr.getNumLight() == 0);
                } else {
                    agentMgr.finishGame(pMgr.getNumDusky() == 0);
                }
            }
        } else if (gameOver) {
            if (LIGHT_CHOICE) {
                agentMgr.finishGame(pMgr.getNumLight() == 0);
            } else {
                agentMgr.finishGame(pMgr.getNumDusky() == 0);
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
        this.graphicsHandler = new GraphicsHandler(creator.getCachedCells(), pMgr, inputHandler);
    }

    private void printSelectedPiece() {
        GameBoardPiece piece = pMgr.getPiece(inputHandler.getFirstXPos(), inputHandler.getFirstYPos());
        if (piece != null) {
            piece.printData();
        }
    }
}
