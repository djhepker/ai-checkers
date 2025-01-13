package main.java.engine;

import main.java.entity.Checker;
import main.java.gameworld.Cell;
import main.java.utils.AssetManager;
import main.java.utils.GameBoardPiece;

public class EntityCreator {
    private final AssetManager assetManager;
    private GameBoardPiece[][] pieces;

    public EntityCreator(GameBoardPiece[][] pieces) {
        assetManager = new AssetManager();
        this.pieces = pieces;
    }

    public Cell createCell(int x, int y) {
        if ((x + y) % 2 == 0) {
            return new Cell("LightTile", x, y, assetManager.getSpriteByName("LightTile"));
        } else {
            return new Cell("DuskyTile", x, y, assetManager.getSpriteByName("DuskyTile"));
        }
    }

    public Checker createChecker(String name, int x, int y) {
        return new Checker(name, x, y, assetManager.getSpriteByName(name), pieces);
    }
}
