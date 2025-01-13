package main.java.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Set;

public interface GameBoardPiece {
    enum PieceColor {
        LIGHT,
        DUSKY
    }

    void update();

    void generateLegalMoves();

    void printLegalMoves();

    Set<Point> getLegalMoves();

    String getName();

    BufferedImage getSprite();

    void setX(int x);

    void setY(int y);

    int getX();

    int getY();

    void printData();
}