package engine;

import gameworld.BoardManager;
import graphics.GraphicsHandler;
import gameworld.PieceManager;
import graphics.InputHandler;
import utils.EntityCreator;
import utils.EntityList;

public class GameEngine {
    private EntityCreator creator;
    private BoardManager bMgr;
    private PieceManager pMgr;
    private GraphicsHandler gHndlr;
    private InputHandler inHndlr;

    private EntityList cells;
    private EntityList pieces;

    public GameEngine() {
        this.creator = new EntityCreator();
        this.pieces = new EntityList();
        this.cells = new EntityList();
        this.bMgr = new BoardManager(cells, creator);
        this.pMgr = new PieceManager(pieces, creator);
        this.gHndlr = new GraphicsHandler(cells, pieces);

        for (entity.Entity piece : pieces) {
            updateCellUse(piece.getX(), piece.getY());
        }
    }

    public void updateGame() {

        gHndlr.repaint();
    }

    // TODO: Figure out how to create and test movement logic for LightPiece
    private void movePiece(int x, int y) {

    }

    public boolean windowExists() {
        return gHndlr.windowOpen();
    }

    private void updateCellUse(int x, int y) {
        bMgr.updateSpace(x, y);
    }

    public void printAllCellsUsage() {
        bMgr.printAllIsCellOccupied();
    }

    public void printAllPiecesInPlay() {
        pMgr.printAllPiecesInPlay();
    }
}
