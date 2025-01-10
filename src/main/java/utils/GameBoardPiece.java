package main.java.utils;

public interface GameBoardPiece {
    enum PieceColor {
        LIGHT,
        DUSKY
    }

    void update();

    void generateTheoreticalMoves();

    void printTheoreticalMoves();

    int[][] getTheoreticalMoves();

    void clearMovementList();

    String getName();

    void setX(int x);

    void setY(int y);

    int getX();

    int getY();

    void printData();
}