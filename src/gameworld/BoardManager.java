package gameworld;

import utils.EntityCreator;
import utils.EntityList;

public class BoardManager {
    private EntityList cells;
    private EntityCreator creator;
    private boolean[][] isOccupied;

    public BoardManager(EntityList cells, EntityCreator creator) {
         this.cells = cells;
         this.creator = creator;
         isOccupied = new boolean[8][8];
         createBoardCells();
    }

    private void createBoardCells() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                cells.addEntity(creator.createCell(i, j));
            }
        }
    }

    public void updateSpace(int x, int y) {
        isOccupied[x][y] = !isOccupied[x][y];
    }

    public void printAllIsCellOccupied() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(isOccupied[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void printNumCells() {
        System.out.println("The number of Cells: " + cells.getNumEntities());
    }

    public void printAllCellsInPlay() {
        cells.printEntities();
    }
}
