package utils;

import entity.DarkPiece;
import entity.LightPiece;
import gameworld.Cell;

public class EntityCreator {
    private final AssetManager assetManager;

    public EntityCreator() {
        assetManager = new AssetManager();
    }

    public Cell createCell(int x, int y) {
        if ((x + y) % 2 == 0) {
            return new Cell("LightTile", x, y, assetManager.getSpriteByLookup("LightTile"));
        } else {
            return new Cell("DarkTile", x, y, assetManager.getSpriteByLookup("DarkTile"));
        }
    }

    public LightPiece createLightPiece(int x, int y) {
        return new LightPiece(x, y, assetManager.getSpriteByLookup("LightPiece"));
    }

    public DarkPiece createDarkPiece(int x, int y) {
        return new DarkPiece(x, y, assetManager.getSpriteByLookup("DarkPiece"));
    }
}
