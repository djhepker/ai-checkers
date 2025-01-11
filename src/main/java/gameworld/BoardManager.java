package main.java.gameworld;

import main.java.engine.EntityCreator;
import main.java.entity.Entity;

public class BoardManager {
    private Entity[][] cells;
    private EntityCreator creator;

    public BoardManager(Entity[][] cells, EntityCreator creator) {
         this.cells = cells;
         this.creator = creator;
         createBoardCells();
    }

    private void createBoardCells() {
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                cells[i][j] = creator.createCell(i, j);
            }
        }
    }

    public void printNumCells() {
        System.out.println("The number of Cells: " + cells.length);
    }
}
