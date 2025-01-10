package com.hepker.ai_checkers.utils;

import com.hepker.ai_checkers.entity.Checker;
import com.hepker.ai_checkers.gameworld.Cell;

public class EntityCreator {
    private final AssetManager assetManager;

    public EntityCreator() {
        assetManager = new AssetManager();
    }

    public Cell createCell(int x, int y) {
        if ((x + y) % 2 == 0) {
            return new Cell("LightTile", x, y, assetManager.getSpriteByName("LightTile"));
        } else {
            return new Cell("DuskyTile", x, y, assetManager.getSpriteByName("DuskyTile"));
        }
    }

    public Checker createChecker(String name, int x, int y) {
        return new Checker(name, x, y, assetManager.getSpriteByName(name));
    }
}
