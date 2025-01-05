package gameworld;

import entity.DarkPiece;
import utils.EntityCreator;
import utils.EntityList;
import utils.AssetManager;

/*
* Handles setting up and maintaining our gameboard
* */
public class BoardManager {
    private final int cellWidth = 16;
    private final int cellHeight = 16;
    private final int darkTileStart = 16;
    private final int lightTileStart = 0;

    private boolean[][] inUseArr;
    private EntityList pieces;
    private EntityList cells;
    private EntityCreator creator;

    public BoardManager() {
        //  default set to false
         inUseArr = new boolean[8][8];
         pieces = new EntityList();
         cells = new EntityList();
         creator = new EntityCreator();
         setUpCheckersBoard();
    }

    public EntityList getPieces() {
        return pieces;
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

    public void printAllPiecesInPlay() {
        pieces.printEntities();
    }

    public void printNumPieces() {
        System.out.println("The number of pieces: " + pieces.getNumEntities());
    }

    private void setUpCheckersBoard() {
        createBoardCells();
        createBeginningPieces();
    }

    private void createBoardCells() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                cells.addEntity(creator.createCell(i, j));
            }
        }
    }

    private void createBeginningPieces() {
        // starting from top left
        // dark starts on 2nd index, so x = 1
        int x = 1;
        int y = 0;
        while (y < 3) {
            if (x > 7) {
                x = 0;
                y++;
                if (y % 2 == 0) {
                    x += 1;
                }
            } else {
                pieces.addEntity(creator.createDarkPiece(x, y));
                x += 2;
            }
        }
        y += 2;
        while (y < 8) {
            if (x > 7) {
                x = 0;
                y++;
                if (y % 2 == 0) {
                    x += 1;
                }
            } else {
                pieces.addEntity(creator.createLightPiece(x, y));
                x += 2;
            }
        }
    }

    public String coordinatesToString(int row, int column) {
        char col = (char) ('A' + column);
        return "" + col + row;
    }

    public int[] stringCoordinateToIntArr(String position) {
        char column = position.charAt(0);
        int row = Integer.parseInt(position.substring(1));
        int x = column - 'A';
        int y = row - 1;

        return new int[] {x, y};
    }
}
