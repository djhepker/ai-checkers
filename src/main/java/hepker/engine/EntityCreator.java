package hepker.engine;

import hepker.game.entity.piece.Checker;
import hepker.game.entity.piece.KingChecker;
import hepker.game.utils.AssetManager;
import hepker.game.entity.GameBoardPiece;

import java.awt.Image;

public class EntityCreator {
    private final AssetManager assetManager;

    public EntityCreator() {
        this.assetManager = new AssetManager();
    }

    public GameBoardPiece createPiece(String name, int x, int y) {
        String pieceName = name.substring(5);
        switch (pieceName) {
            case "Checker":
                return createChecker(name, x, y);
            case "CheckerKing":
                return createKingChecker(name, x, y);
            default:
                throw new IllegalArgumentException("Invalid piece name: " + name + " tested as: " + pieceName);
        }
    }

    private Checker createChecker(String name, int x, int y) {
        return new Checker(name, x, y, assetManager.getSpriteByName(name));
    }

    private KingChecker createKingChecker(String name, int x, int y) {
        return new KingChecker(name, x, y, assetManager.getSpriteByName(name));
    }

    public Image[] getCachedCells() {
        return new Image[]{
                assetManager.getSpriteByName("LightTile"),
                assetManager.getSpriteByName("DuskyTile")};
    }
}
