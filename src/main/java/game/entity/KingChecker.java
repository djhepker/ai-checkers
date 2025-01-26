package main.java.game.entity;

import main.java.game.utils.GameBoardPiece;

import java.awt.image.BufferedImage;

/*
* TODO: create movement logic for KingChecker, likely just need to Override generateLegalMoves()
* */

public class KingChecker extends Checker implements GameBoardPiece {

    public KingChecker(String name, int x, int y, BufferedImage image) {
        super(name, x, y, image);
    }

    @Override
    public void generateLegalMoves() {

    }

    @Override
    public boolean isReadyForPromotion() {
        return false;
    }
}
