package hepker.engine;

import hepker.ai.agentintegration.AIEngine;
import hepker.game.graphics.GraphicsHandler;
import hepker.game.gameworld.PieceManager;
import hepker.game.graphics.InputHandler;
import hepker.game.entity.GameBoardPiece;
import hepker.utils.EpisodeStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.SwingUtilities;
import java.util.ConcurrentModificationException;

public final class GameEngine {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameEngine.class);

    private final AIEngine agentMgr;

    private final boolean lightChosen;
    private final boolean hasPlayer;
    private final boolean isTraining;

    private PieceManager pMgr;
    private GraphicsHandler graphicsHandler;
    private InputHandler inputHandler;

    private boolean playerTurn;
    private boolean gameOver;

    private int numTurnsWithoutCapture;
    private int totalTurnCount;

    public GameEngine(boolean aiIsTraining) {
        this.isTraining = aiIsTraining;
        EntityCreator creator = new EntityCreator();
        loadGameWorld(creator);
        if (!this.isTraining) {
            this.inputHandler = new InputHandler();
            renderUI(creator);
        }
        if (isTraining) {
            this.lightChosen = true;
            this.hasPlayer = false;
            this.agentMgr = new AIEngine(pMgr, lightChosen, "Agent Vs Stochastic");
        } else {
            String gameMode = graphicsHandler.showGameModeDialog();
            this.hasPlayer = gameMode.endsWith("Player");
            if (hasPlayer) {
                this.lightChosen = this.graphicsHandler.showPopUpColorDialog();
            } else {
                this.lightChosen = true;
            }
            this.playerTurn = lightChosen;
            this.agentMgr = new AIEngine(pMgr, lightChosen, gameMode);
        }
        this.gameOver = false;
        this.numTurnsWithoutCapture = 0;
        this.totalTurnCount = 0;
    }

    public void updateGame() {
        ++totalTurnCount;
        try {
            if (hasPlayer) {
                handleHumanGameLogic();
            } else {
                trainAgent();
            }
            if (!isTraining) {
                SwingUtilities.invokeLater(() -> graphicsHandler.repaint());
            }
            if (numTurnsWithoutCapture >= 25 || pMgr.sideDefeated()) {
                gameOver = true;
            }
            pMgr.updateAllPieces();
        } catch (AssertionError e) {
            LOGGER.error("Invalid mouse selection in InputHandler", e);
        } catch (ConcurrentModificationException e) {
            LOGGER.error("ConcurrentModificationError occurred during updateGame()", e);
        } catch (Exception e) {
            LOGGER.error("Unexpected error in updateGame()", e);
        }
    }

    public boolean gameOver() {
        try {
            if (!isTraining) {
                if (!graphicsHandler.windowOpen() || gameOver) {
                    this.gameOver = true;
                    if (lightChosen) {
                        agentMgr.finishGame(pMgr.getNumLight() == 0);
                    } else {
                        agentMgr.finishGame(pMgr.getNumDusky() == 0);
                    }
                }
            } else if (gameOver) {
                EpisodeStatistics.processEpisode(AIEngine.getNumTurns());
                AIEngine.setNumTurns(0);
                if (lightChosen) {
                    agentMgr.finishGame(pMgr.getNumLight() == 0);
                } else {
                    agentMgr.finishGame(pMgr.getNumDusky() == 0);
                }
            }
            if (gameOver) {
                StringBuilder debugBuilder = new StringBuilder()
                        .append("Total number of turns: ")
                        .append(totalTurnCount)
                        .append(" Turns without capture: ")
                        .append(numTurnsWithoutCapture);
                if (numTurnsWithoutCapture < 25) {
                    debugBuilder.append(" * Successful Game");
                }
                LOGGER.info(debugBuilder.toString());
            }
            return gameOver;
        } catch (Exception e) {
            LOGGER.error("Unexpected error in gameOver()", e);
            return true;
        }
    }

    private void handleHumanGameLogic() {
        inputHandler.update();
        if (playerTurn) {
            if (inputHandler.movementChosen()) {
                handleInput();
            }
        } else {
            agentMgr.update();
            playerTurn = !playerTurn;
        }
    }

    private void handleInput() {
        int firstXPos = inputHandler.getFirstXPos();
        int firstYPos = inputHandler.getFirstYPos();
        GameBoardPiece piece = pMgr.getPiece(firstXPos, firstYPos);
        if (piece != null && lightChosen == piece.isLight() && pMgr.movePiece(piece, inputHandler)) {
            if (piece.isReadyForPromotion()) {
                pMgr.promotePiece(piece);
            }
            playerTurn = !playerTurn;
        } else {
            inputHandler.resetClicks();
        }
    }

    private void trainAgent() {
        int numPiecesNaught = pMgr.getNumPiecesInPlay();
        agentMgr.update();
        agentMgr.flipAgentSwitch();
        if (numPiecesNaught == pMgr.getNumPiecesInPlay()) {
            ++numTurnsWithoutCapture;
        } else {
            numTurnsWithoutCapture = 0;
        }
        if (agentMgr.agentZeroTurn()) {
            pMgr.updateAllPieces();
            trainAgent();
        }
    }

    /**
     * Initializes PieceManager
     * @param creator Used for creating each piece in PieceManager
     */
    private void loadGameWorld(EntityCreator creator) {
        this.pMgr = new PieceManager(creator);
    }

    /**
     * Helper method for creating graphics-relevant details for the game. Intializes GraphicsHandler and
     * sets opening display
     * @param creator Sends cachedCells to GraphicsHandler for use
     */
    private void renderUI(EntityCreator creator) {
        this.graphicsHandler = new GraphicsHandler(creator.getCachedCells(), pMgr, inputHandler);
    }
}
