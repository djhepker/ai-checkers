package main.java.game.entity;

import main.java.game.utils.GameBoardPiece;

import java.awt.image.BufferedImage;

public class KingChecker extends Checker implements GameBoardPiece {
    public KingChecker(String name, int x, int y, BufferedImage image) {
        super(name, x, y, image);
    }


}
