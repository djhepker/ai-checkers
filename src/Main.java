
import gameworld.BoardManager;
import gameworld.BoardRenderer;

public class Main {
    public static void main(String[] args) {
        BoardManager boardManager = new BoardManager();
        BoardRenderer boardRenderer = new BoardRenderer(boardManager.getCells(), boardManager.getPieces());

        while (boardRenderer.windowOpen()) {
            boardRenderer.repaint();
        }
    }
}
