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

    private final boolean chooseLight;
    private final boolean hasPlayer;
    private final boolean isTraining;

    private boolean playerTurn;
    private boolean gameOver;

    private int numTurnsWithoutCapture;
    private int totalTurnCount;

    public GameEngine(boolean aiIsTraining) {
        this.isTraining = aiIsTraining;
        loadGameWorld();
        if (!this.isTraining) {
            renderUI();
        }
        if (this.isTraining) {
            this.chooseLight = true;
            this.hasPlayer = false;
            this.agentMgr = new AIEngine(pMgr, chooseLight, "Agent Vs Stochastic");
        } else {
            String gameMode = graphicsHandler.showGameModeDialog();
            this.hasPlayer = gameMode.endsWith("Player");
            if (hasPlayer) {
                this.chooseLight = this.graphicsHandler.showPopUpColorDialog();
            } else {
                this.chooseLight = true;
            }
            this.playerTurn = chooseLight;
            this.agentMgr = new AIEngine(pMgr, chooseLight, gameMode);
        }
        if (!chooseLight) {
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
                inputHandler.update();
                if (playerTurn) {
                    if (inputHandler.movementChosen()) {
                        if (handleInput()) {
                            pMgr.reverseBoard();
                            pMgr.updateAllPieces();
                        }
                    }
                } else {
                    agentMgr.update();
                    playerTurn = !playerTurn;
                }
            } else {
                int numPiecesNaught = pMgr.getNumPiecesInPlay();
                agentMgr.update();
                if (numPiecesNaught == pMgr.getNumPiecesInPlay()) {
                    ++numTurnsWithoutCapture;
                } else {
                    numTurnsWithoutCapture = 0;
                }
            }
            if (pMgr.sideDefeated()) {
                this.gameOver = true;
            }
            if (!isTraining) {
                graphicsHandler.repaint();
            }
        } catch (AssertionError e) {
            LOGGER.error("Invalid mouse selection in InputHandler", e);
        } catch (Exception e) {
            LOGGER.error("Unexpected error in updateGame()", e);
        }
        if (numTurnsWithoutCapture >= 50) {
            gameOver = true;
        }
    }

    public boolean gameOver() {
        if (!isTraining) {
            if (!graphicsHandler.windowOpen() || gameOver) {
                this.gameOver = true;
                if (chooseLight) {
                    agentMgr.finishGame(pMgr.getNumLight() == 0);
                } else {
                    agentMgr.finishGame(pMgr.getNumDusky() == 0);
                }
            }
        } else if (gameOver) {
            if (chooseLight) {
                agentMgr.finishGame(pMgr.getNumLight() == 0);
            } else {
                agentMgr.finishGame(pMgr.getNumDusky() == 0);
            }
        }
        if (false) {
            StringBuilder debugBuilder = new StringBuilder()
                    .append("Total number of turns: ")
                    .append(totalTurnCount)
                    .append(" Turns without capture: ")
                    .append(numTurnsWithoutCapture);
            if (numTurnsWithoutCapture != 75) {
                debugBuilder.append(" * Successful Game");
            }
            System.out.println(debugBuilder);
        }
        return gameOver;
    }

    private boolean handleInput() {
        int firstXPos = inputHandler.getFirstXPos();
        int firstYPos = inputHandler.getFirstYPos();
        GameBoardPiece piece = pMgr.getPiece(firstXPos, firstYPos);
        if (piece != null && chooseLight == piece.isLight() && pMgr.movePiece(piece)) {
            if (piece.isReadyForPromotion()) {
                pMgr.promotePiece(piece);
            }
            playerTurn = !playerTurn;
        }
        inputHandler.resetClicks();
        return !playerTurn;
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
