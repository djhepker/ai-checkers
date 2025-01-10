package com.hepker.ai_checkers.gameworld;

import com.hepker.ai_checkers.entity.Entity;

import java.awt.image.BufferedImage;

public class Cell extends Entity {
    public Cell(String cellType, int x, int y, BufferedImage image) {
        super(cellType, x, y, image);
    }
}
