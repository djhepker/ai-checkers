package main.java.utils;

import main.java.entity.movement.MovementNode;

import java.awt.image.BufferedImage;

public interface GameBoardPiece {
    enum PieceColor {
        LIGHT,
        DUSKY
    }

    void update();

    void generateLegalMoves();

    MovementNode getMoveListPointer();

    void printLegalMoves();

    String getName();

    BufferedImage getSprite();

    void setX(int x);

    void setY(int y);

    int getX();

    int getY();

    void printData();
}