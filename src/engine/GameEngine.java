package engine;

import gameworld.BoardManager;
import graphics.BoardRenderer;
import gameworld.PieceManager;
import utils.EntityCreator;
import utils.EntityList;

public class GameEngine {
    private EntityCreator creator;
    private BoardManager bMgr;
    private PieceManager pMgr;
    private BoardRenderer bdRdr;

    private EntityList cells;
    private EntityList pieces;

    public GameEngine() {
        this.creator = new EntityCreator();
        this.pieces = new EntityList();
        this.cells = new EntityList();
        this.bMgr = new BoardManager(cells, creator);
        this.pMgr = new PieceManager(pieces, creator);
        this.bdRdr = new BoardRenderer(cells, pieces);

        for (entity.Entity piece : pieces) {
            updateCellUse(piece.getX(), piece.getY());
        }
    }

    // TODO: Figure out how to create and test movement logic for LightPiece
    public void movePiece(int x, int y) {

    }

    public boolean windowExists() {
        return bdRdr.windowOpen();
    }

    public void repaintWindow() {
        bdRdr.repaint();
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
