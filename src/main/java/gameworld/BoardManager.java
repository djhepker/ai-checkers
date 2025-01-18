package main.java.gameworld;

import main.java.engine.EntityCreator;

import java.awt.Image;

public class BoardManager {
    private EntityCreator creator;
    private final Image[] cachedTiles;

    public BoardManager(EntityCreator creator) {
         this.creator = creator;
         this.cachedTiles = creator.getCachedCells();
    }

    public Image[] getCachedTiles() {
        return cachedTiles;
    }

    private int[][] getBoardPattern() {
        int[][] boardPattern = new int[8][8];
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                boardPattern[i][j] = (i + j) & 1;
            }
        }
        return boardPattern;
    }
}
