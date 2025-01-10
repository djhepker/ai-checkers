package com.hepker.ai_checkers.engine;

import com.hepker.ai_checkers.entity.Entity;
import com.hepker.ai_checkers.gameworld.BoardManager;
import com.hepker.ai_checkers.graphics.GameWindow;
import com.hepker.ai_checkers.graphics.GraphicsHandler;
import com.hepker.ai_checkers.gameworld.PieceManager;
import com.hepker.ai_checkers.graphics.InputHandler;
import com.hepker.ai_checkers.utils.EntityCreator;
import com.hepker.ai_checkers.utils.EntityArray;
import com.hepker.ai_checkers.utils.GameBoardPiece;

public class GameEngine {
    private EntityCreator creator;
    private BoardManager bMgr;
    private PieceManager pMgr;
    private GraphicsHandler graphicsHandler;
    private InputHandler inputHandler;
    private GameWindow window;

    private EntityArray cells;
    private EntityArray pieces;

    public GameEngine() {
        this.creator = new EntityCreator();
        this.pieces = new EntityArray();
        this.cells = new EntityArray();
        this.bMgr = new BoardManager(cells, creator);
        this.graphicsHandler = new GraphicsHandler(cells, pieces);
        this.inputHandler = graphicsHandler.getInputHandler();
        this.window = graphicsHandler.getGameWindow();
        this.pMgr = new PieceManager(pieces, creator, inputHandler);
    }

    public void updateGame() {
        inputHandler.update();
        if (inputHandler.movementChosen()) {
            int firstXPos = inputHandler.getFirstXPos();
            int firstYPos = inputHandler.getFirstYPos();

            pMgr.movePiece(pieces.getEntity(firstXPos, firstYPos));
            inputHandler.resetClicks();
            printSelectedPiece();
        }
        graphicsHandler.repaint();
    }

    public boolean isOpen() {
        return window.isOpen();
    }

    private void printSelectedPiece() {
        int xCell = inputHandler.getFirstXPos();
        int yCell = inputHandler.getFirstYPos();

        Entity e = pMgr.getPiece(xCell, yCell);
        if (e instanceof GameBoardPiece) {
            GameBoardPiece piece = (GameBoardPiece) e;
            piece.printData();
        }

    }

    public void printAllPiecesInPlay() {
        pMgr.printAllPiecesInPlay();
    }
}
