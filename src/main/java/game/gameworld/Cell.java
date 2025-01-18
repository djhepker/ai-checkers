package main.java.game.gameworld;

import main.java.game.entity.Entity;

import java.awt.image.BufferedImage;

public class Cell extends Entity {
    public Cell(String cellType, int x, int y, BufferedImage image) {
        super(cellType, x, y, image);
    }
}
