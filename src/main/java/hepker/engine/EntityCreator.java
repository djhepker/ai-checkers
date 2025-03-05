package hepker.engine;

import hepker.game.entity.piece.Checker;
import hepker.game.entity.piece.KingChecker;
import hepker.game.utils.AssetManager;
import hepker.game.entity.GameBoardPiece;

import java.awt.Image;

public final class EntityCreator {
    private final AssetManager assetManager;

    public EntityCreator() {
        this.assetManager = new AssetManager();
    }

    public GameBoardPiece createPiece(String name, int x, int y) {
        String pieceName = name.substring(5);
        return switch (pieceName) {
            case "Checker" -> createChecker(name, x, y);
            case "CheckerKing" -> createKingChecker(name, x, y);
            default -> throw new IllegalArgumentException("Invalid piece name: " + name + " tested as: " + pieceName);
        };
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
