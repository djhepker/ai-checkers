package engine;

import entity.Entity;
import entity.LightPiece;
import gameworld.BoardManager;
import graphics.GameWindow;
import graphics.GraphicsHandler;
import gameworld.PieceManager;
import graphics.InputHandler;
import utils.EntityCreator;
import utils.EntityArray;

public class GameEngine {
    private EntityCreator creator;
    private BoardManager bMgr;
    private PieceManager pMgr;
    private MovementManager moveMgr;
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
        this.pMgr = new PieceManager(pieces, creator);
        this.moveMgr = new MovementManager();
        this.graphicsHandler = new GraphicsHandler(cells, pieces);
        this.inputHandler = graphicsHandler.getInputHandler();
        this.window = graphicsHandler.getGameWindow();
    }

    public void updateGame() {
        inputHandler.update();
        if (inputHandler.movementChosen()) {
            //moveMgr.movePiece(inputHandler.getFirstXPos(), inputHandler.getFirstYPos());
            inputHandler.resetClicks();
        }
        graphicsHandler.repaint();
    }

    public boolean isOpen() {
        return window.isOpen();
    }

    public void printAllPiecesInPlay() {
        pMgr.printAllPiecesInPlay();
    }

    public class MovementManager {
//        private void movePiece(int x, int y) {
//            for (Entity e : pieces) {
//                System.out.println("e.getX(): " + e.getX() + " x: " + x);
//                if (e.getX() == x && e.getY() == y) {
//                    if (e instanceof LightPiece) {
//                        LightPiece movementPiece = (LightPiece) e;
//                        int[][] possibleMoves = movementPiece.getTheoreticalMoves();
//                        for (int i = 0; i < possibleMoves.length; i++) {
//                            if (possibleMoves[i][0] == x && possibleMoves[i][1] == y) {
//                                System.out.println(
//                                        "Match found at: (" + possibleMoves[i][0] + ", " + possibleMoves[i][1] + ")");
//                                return;
//                            }
//                        }
//                    } else {
//                        break;
//                    }
//                }
//            }
//        }
    }
}
