package gameworld;

import utils.EntityCreator;
import utils.EntityList;

public class BoardManager {
    private EntityList cells;
    private EntityCreator creator;

    public BoardManager(EntityList cells, EntityCreator creator) {
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
}
