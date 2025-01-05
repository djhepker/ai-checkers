
import gameworld.BoardManager;
import gameworld.BoardRenderer;

public class Main {
    public static void main(String[] args) {
        BoardManager bMgr = new BoardManager();
        BoardRenderer bRdr = new BoardRenderer(bMgr.getCells(), bMgr.getPieces());

        //bMgr.printAllPiecesInPlay();
        //bMgr.printAllCellsInPlay();

        while (bRdr.windowOpen()) {
            bRdr.repaint();
        }
    }
}
