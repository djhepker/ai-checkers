package gameworld;

import utils.EntityCreator;
import utils.EntityArray;

public class BoardManager {
    private EntityArray cells;
    private EntityCreator creator;

    public BoardManager(EntityArray cells, EntityCreator creator) {
         this.cells = cells;
         this.creator = creator;
         createBoardCells();
    }

    private void createBoardCells() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                cells.addEntity(creator.createCell(i, j));
            }
        }
    }

    public void printNumCells() {
        System.out.println("The number of Cells: " + cells.getNumEntities());
    }

    public void printAllCellsInPlay() {
        cells.printEntities();
    }
}
