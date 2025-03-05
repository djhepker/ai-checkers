package hepker.game.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class SpriteSheet {
    private BufferedImage spritesheet;

    public SpriteSheet(String filePath) {
        try {
            spritesheet = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: Could not load spritesheet from " + filePath);
        }
    }

    public BufferedImage getSprite(int x, int y, int width, int height) {
        return spritesheet.getSubimage(x, y, width, height);
    }
}
