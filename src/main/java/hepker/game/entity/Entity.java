package hepker.game.entity;

import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;

public abstract class Entity {

    @Getter
    private final String name;

    private final boolean isLight;

    @Getter
    private final BufferedImage sprite;

    @Setter
    @Getter
    private int x;

    @Setter
    @Getter
    private int y;


    protected Entity(String inputName, int x, int y, BufferedImage image, boolean isLight) {
        this.name = inputName;
        this.x = x;
        this.y = y;
        this.sprite = image;
        this.isLight = isLight;
    }

    public boolean isLight() {
        return isLight;
    }
}
