package main.java.game.entity;

import java.awt.image.BufferedImage;

public abstract class Entity {
    private final String name;
    private final BufferedImage sprite;
    private int x;
    private int y;
    private final boolean isLight;

    protected Entity(String inputName, int x, int y, BufferedImage image, boolean isLight) {
        this.name = inputName;
        this.x = x;
        this.y = y;
        this.sprite = image;
        this.isLight = isLight;
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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isLight() {
        return isLight;
    }

    public BufferedImage getSprite() {
        return sprite;
    }
}
