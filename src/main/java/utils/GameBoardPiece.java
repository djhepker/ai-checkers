package main.java.utils;

public interface GameBoardPiece {
    enum PieceColor {
        LIGHT,
        DUSKY
    }

    void update();

    void generateLegalMoves();

    void printLegalMoves();

    int[][] getLegalMoves();

    void clearMovementList();

    String getName();

    void setX(int x);

    void setY(int y);

    int getX();

    int getY();

    void printData();
}