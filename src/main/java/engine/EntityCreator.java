package main.java.engine;

import main.java.game.entity.Checker;
import main.java.game.utils.AssetManager;

import java.awt.Image;

public class EntityCreator {
    private final AssetManager assetManager;

    public EntityCreator() {
        this.assetManager = new AssetManager();
    }

    public Image[] getCachedCells() {
        return new Image[]{
                assetManager.getSpriteByName("LightTile"),
                assetManager.getSpriteByName("DuskyTile")};
    }

    public Checker createChecker(String name, int x, int y) {
        return new Checker(name, x, y, assetManager.getSpriteByName(name));
    }
}
