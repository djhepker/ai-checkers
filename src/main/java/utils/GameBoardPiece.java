package main.java.utils;

import main.java.entity.movement.LocationNode;

import java.awt.image.BufferedImage;

public interface GameBoardPiece {
    enum PieceColor {
        LIGHT,
        DUSKY
    }

    void update();

    void generateLegalMoves();

    LocationNode getMoveListPointer();

    void printLegalMoves();

    String getName();

    BufferedImage getSprite();

    void setX(int x);

    void setY(int y);

    int getX();

    int getY();

    short getPieceValue();

    PieceColor getColor();

    void printData();
}