package utils;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class AssetManager {
    private final String spriteSheetFilePath = "assets/images/chckers_spritesheet.png";

    private final int cellWidth = 16;
    private final int cellHeight = 16;
    private final int duskyTileStart = 0;
    private final int lightTileStart = 16;
    private final int lightCheckerCoordinate = 32;
    private final int duskyCheckerCoordinate = 48;

    private SpriteSheet spriteSheet;
    private HashMap<String, BufferedImage> spriteMap;

    public AssetManager() {
        spriteSheet = new SpriteSheet(spriteSheetFilePath);
        spriteMap = new HashMap<>();
        loadSprites();
    }

    public BufferedImage getSpriteByName(String entityName) {
        return spriteMap.get(entityName);
    }

    private void loadSprites() {
        spriteMap.put("LightTile", spriteSheet.getSprite(lightTileStart, 0, cellWidth, cellHeight));
        spriteMap.put("DarkTile", spriteSheet.getSprite(duskyTileStart, 0, cellWidth, cellHeight));
        spriteMap.put("LightChecker", spriteSheet.getSprite(lightCheckerCoordinate, 0, cellWidth, cellHeight));
        spriteMap.put("DuskyChecker", spriteSheet.getSprite(duskyCheckerCoordinate, 0, cellWidth, cellHeight));
    }
}
