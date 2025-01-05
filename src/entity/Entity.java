package entity;

import java.awt.image.BufferedImage;

public abstract class Entity {
    private String name;
    private BufferedImage sprite;
    private int x;
    private int y;

    public Entity(String inputName, int x, int y, BufferedImage image) {
        this.name = inputName;
        this.x = x;
        this.y = y;
        this.sprite = image;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public void setSprite(BufferedImage inputSprite) {
        sprite = inputSprite;
    }
}
