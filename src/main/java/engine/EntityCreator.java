package main.java.engine;

import main.java.game.entity.Checker;
import main.java.game.utils.AssetManager;
import main.java.game.utils.GameBoardPiece;

import java.awt.Image;

public class EntityCreator {
    private final AssetManager assetManager;
    private GameBoardPiece[][] pieces;

    public EntityCreator(GameBoardPiece[][] pieces) {
        this.assetManager = new AssetManager();
        this.pieces = pieces;
    }

    public Image[] getCachedCells() {
        return new Image[]{
                assetManager.getSpriteByName("LightTile"),
                assetManager.getSpriteByName("DuskyTile")};
    }

    public Checker createChecker(String name, int x, int y) {
        return new Checker(name, x, y, assetManager.getSpriteByName(name), pieces);
    }
}
