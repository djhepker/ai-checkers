package engine;

import gameworld.BoardManager;
import gameworld.BoardRenderer;
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
    }

    public boolean windowExists() {
        return bdRdr.windowOpen();
    }

    public void repaintWindow() {
        bdRdr.repaint();
    }

    public EntityList getPieces() {
        return pieces;
    }

    public void printAllPiecesInPlay() {
        pieces.printEntities();
    }
    public void printNumPieces() {
        System.out.println("The number of pieces: " + pieces.getNumEntities());
    }

    public EntityList getCells() {
        return cells;
    }

    public void printAllCellsInPlay() {
        cells.printEntities();
    }

    public void printNumCells() {
        System.out.println("The number of Cells: " + cells.getNumEntities());
    }
}
