package utils;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class AssetManager {
    private final String spriteSheetFilePath = "assets/images/chckers_spritesheet.png";

    private final int cellWidth = 16;
    private final int cellHeight = 16;
    private final int darkTileStart = 16;
    private final int lightTileStart = 0;
    private final int lightPieceStart = 32;
    private final int darkPieceStart = 48;

    private SpriteSheet spriteSheet;
    private HashMap<String, BufferedImage> spriteMap;

    public AssetManager() {
        spriteSheet = new SpriteSheet(spriteSheetFilePath);
        spriteMap = new HashMap<>();
        loadSprites();
    }

    public BufferedImage getSpriteByLookup(String entityName) {
        return spriteMap.get(entityName);
    }

    private void loadSprites() {
        spriteMap.put("LightTile", spriteSheet.getSprite(lightTileStart, 0, cellWidth, cellHeight));
        spriteMap.put("DarkTile", spriteSheet.getSprite(darkTileStart, 0, cellWidth, cellHeight));
        spriteMap.put("LightPiece", spriteSheet.getSprite(lightPieceStart, 0, cellWidth, cellHeight));
        spriteMap.put("DarkPiece", spriteSheet.getSprite(darkPieceStart, 0, cellWidth, cellHeight));
    }
}
