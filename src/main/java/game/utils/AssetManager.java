package main.java.game.utils;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public abstract class AssetManager {

    public static BufferedImage getSpriteByName(String entityName) {
        return spriteMap.get(entityName);
    }

    private static final HashMap<String, BufferedImage> spriteMap = loadSprites();

    private static HashMap<String, BufferedImage> loadSprites() {
        String spriteSheetFilePath = "src/main/assets/images/chckers_spritesheet.png";
        SpriteSheet spriteSheet = new SpriteSheet(spriteSheetFilePath);

        HashMap<String, BufferedImage> sprites = new HashMap<>();

        int cellWidth = 64;
        int cellHeight = 64;
        int duskyTileStart = 0;
        int lightTileStart = 64;
        int lightCheckerCoordinate = 128;
        int duskyCheckerCoordinate = 192;
        int lightCheckerKingSpriteX = 256;
        int duskyCheckerKingSpriteX = 320;

        sprites.put("LightTile", spriteSheet.getSprite(lightTileStart, 0, cellWidth, cellHeight));
        sprites.put("DuskyTile", spriteSheet.getSprite(duskyTileStart, 0, cellWidth, cellHeight));
        sprites.put("LIGHTChecker", spriteSheet.getSprite(lightCheckerCoordinate, 0, cellWidth, cellHeight));
        sprites.put("DUSKYChecker", spriteSheet.getSprite(duskyCheckerCoordinate, 0, cellWidth, cellHeight));
        sprites.put("LIGHTCheckerKing", spriteSheet.getSprite(lightCheckerKingSpriteX, 0, cellWidth, cellHeight));
        sprites.put("DUSKYCheckerKing", spriteSheet.getSprite(duskyCheckerKingSpriteX, 0, cellWidth, cellHeight));

        return sprites;
    }
}
