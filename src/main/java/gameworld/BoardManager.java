package main.java.gameworld;

import main.java.engine.EntityCreator;

import java.awt.Image;

public class BoardManager {
    private EntityCreator creator;
    private final int[][] tilePattern;
    private final Image[] cachedTiles;

    public BoardManager(EntityCreator creator) {
         this.creator = creator;
         this.cachedTiles = creator.getCachedCells();
         this.tilePattern = getBoardPattern();
    }

    public int[][] getTilePattern() {
        return tilePattern;
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

    public void printNumCells() {
        System.out.println("The number of Cells: " + tilePattern.length);
    }
}
