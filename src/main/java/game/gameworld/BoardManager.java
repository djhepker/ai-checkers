package main.java.game.gameworld;

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
}
