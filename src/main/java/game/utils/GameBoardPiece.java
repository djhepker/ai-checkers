package main.java.game.utils;

import main.java.game.entity.movement.ActionNode;

import java.awt.image.BufferedImage;
import java.util.stream.Stream;

public interface GameBoardPiece {
    enum PieceColor {
        LIGHT,
        DUSKY
    }

    Stream<ActionNode> getMoveListAsStream();

    boolean isLight();

    void update(GameBoardPiece[][] pieces);

    void generateLegalMoves(GameBoardPiece[][] pieces);

    ActionNode getMoveListPointer();

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