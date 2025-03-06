package hepker.engine;

import hepker.engine.agentintegration.AIEngine;
import hepker.game.graphics.GraphicsHandler;
import hepker.game.gameworld.PieceManager;
import hepker.game.graphics.InputHandler;
import hepker.game.entity.GameBoardPiece;
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

    private EntityCreator creator;
    private PieceManager pMgr;
    private GraphicsHandler graphicsHandler;
    private InputHandler inputHandler;

    private boolean playerTurn;
    private boolean gameOver;

    private int numTurnsWithoutCapture;
    private int totalTurnCount;

    public GameEngine(boolean aiIsTraining) {
        this.isTraining = aiIsTraining;
        loadGameWorld();
        if (!this.isTraining) {
            renderUI();
            graphicsHandler.cacheBoard(pMgr.getPiecesContainer());
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
        if (!lightChosen) {
            pMgr.reverseBoard();
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
            if (numTurnsWithoutCapture >= 50 || pMgr.sideDefeated()) {
                gameOver = true;
            }
        } catch (AssertionError e) {
            LOGGER.error("Invalid mouse selection in InputHandler", e);
        } catch (ConcurrentModificationException e) {
            LOGGER.error("ConcurrentModificationError occurred during updateGame()", e);
        } catch (Exception e) {
            LOGGER.error("Unexpected error in updateGame()", e);
        }
    }

    private void handleHumanGameLogic() {
        inputHandler.update();
        if (playerTurn) {
            if (inputHandler.movementChosen()) {
                handleInput();
            }
        } else {
            agentTurn();
            playerTurn = !playerTurn;
        }
    }

    private void handleInput() {
        int firstXPos = inputHandler.getFirstXPos();
        int firstYPos = inputHandler.getFirstYPos();
        GameBoardPiece piece = pMgr.getPiece(firstXPos, firstYPos);
        if (piece != null && lightChosen == piece.isLight() && pMgr.movePiece(piece)) {
            if (piece.isReadyForPromotion()) {
                pMgr.promotePiece(piece);
            }
            graphicsHandler.cacheBoard(pMgr.getPiecesContainer());
            prepBoardForOtherPlayer();
            playerTurn = !playerTurn;
        } else {
            inputHandler.resetClicks();
        }
    }

    private void agentTurn() {
        agentMgr.update();
        prepBoardForOtherPlayer();
        graphicsHandler.cacheBoard(pMgr.getPiecesContainer());
    }

    private void trainAgent() {
        int numPiecesNaught = pMgr.getNumPiecesInPlay();
        agentMgr.update();
        if (agentMgr.agentOneTurn() && !isTraining) {
            graphicsHandler.cacheBoard(pMgr.getPiecesContainer());
        }
        prepBoardForOtherPlayer();
        agentMgr.flipAgentSwitch();
        if (numPiecesNaught == pMgr.getNumPiecesInPlay()) {
            ++numTurnsWithoutCapture;
        } else {
            numTurnsWithoutCapture = 0;
        }
    }

    private void prepBoardForOtherPlayer() {
        pMgr.reverseBoard();
        pMgr.updateAllPieces();
    }

    private void loadGameWorld() {
        this.inputHandler = new InputHandler();
        this.creator = new EntityCreator();
        this.pMgr = new PieceManager(creator, inputHandler);
    }

    private void renderUI() {
        this.graphicsHandler = new GraphicsHandler(creator.getCachedCells(), pMgr, inputHandler);
    }

    public boolean gameOver() {
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
            if (numTurnsWithoutCapture != 50) {
                debugBuilder.append(" * Successful Game");
            }
            LOGGER.info(debugBuilder.toString());
        }
        return gameOver;
    }
}
