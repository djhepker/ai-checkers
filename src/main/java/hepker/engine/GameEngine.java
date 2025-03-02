package hepker.engine;

import hepker.engine.agentintegration.AIEngine;
import hepker.game.graphics.GraphicsHandler;
import hepker.game.gameworld.PieceManager;
import hepker.game.graphics.InputHandler;
import hepker.game.entity.GameBoardPiece;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GameEngine {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameEngine.class);

    private EntityCreator creator;
    private PieceManager pMgr;
    private GraphicsHandler graphicsHandler;
    private InputHandler inputHandler;
    private AIEngine agentMgr;

    private final boolean lightChoice;
    private final boolean hasPlayer;
    private final boolean isTraining;

    private boolean playerTurn;
    private boolean gameOver;

    private int numTurnsWithoutCapture;

    public GameEngine(boolean aiIsTraining) {
        this.isTraining = aiIsTraining;
        loadGameWorld();
        if (!this.isTraining) {
            renderUI();
        }
        if (this.isTraining) {
            this.lightChoice = true;
            this.hasPlayer = false;
            this.agentMgr = new AIEngine(pMgr, lightChoice, "Agent Vs Stochastic");
        } else {
            String gameMode = graphicsHandler.showGameModeDialog();
            this.hasPlayer = gameMode.endsWith("Player");
            if (hasPlayer) {
                this.lightChoice = this.graphicsHandler.showPopUpColorDialog();
            } else {
                this.lightChoice = true;
            }
            this.playerTurn = lightChoice;
            this.agentMgr = new AIEngine(pMgr, lightChoice, gameMode);
        }
        this.gameOver = false;
        this.numTurnsWithoutCapture = 0;
    }

    public void updateGame() {
        try {
            if (hasPlayer) {
                inputHandler.update();
                if (playerTurn) {
                    handleInput();
                } else {
                    int numPiecesNaught = pMgr.getNumPiecesInPlay();
                    agentMgr.update();
                    if (numPiecesNaught == pMgr.getNumPiecesInPlay()) {
                        ++numTurnsWithoutCapture;
                    } else {
                        numTurnsWithoutCapture = 0;
                    }
                    playerTurn = !playerTurn;
                }
            } else {
                agentMgr.update();
            }
            if (pMgr.sideDefeated()) {
                this.gameOver = true;
            }
            if (!isTraining) {
                graphicsHandler.repaint();
            }
        } catch (Exception e) {
            LOGGER.error("Unexpected error in updateGame()", e);
        } catch (AssertionError e) {
            LOGGER.error("Invalid mouse selection in InputHandler", e);
        }
        if (numTurnsWithoutCapture >= 75) {
            gameOver = true;
        }
    }

    public boolean gameOver() {
        if (!isTraining) {
            if (!graphicsHandler.windowOpen() || gameOver) {
                this.gameOver = true;
                if (lightChoice) {
                    agentMgr.finishGame(pMgr.getNumLight() == 0);
                } else {
                    agentMgr.finishGame(pMgr.getNumDusky() == 0);
                }
            }
        } else if (gameOver) {
            if (lightChoice) {
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
            if (piece != null && lightChoice == piece.isLight() && pMgr.movePiece(piece)) {
                if (piece.isReadyForPromotion()) {
                    pMgr.promotePiece(piece);
                }
                pMgr.updateAllPieces();
                playerTurn = !playerTurn;
            }
            inputHandler.resetClicks();
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
}
