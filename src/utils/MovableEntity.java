package utils;

public interface MovableEntity {
    void update();

    int[][] getTheoreticalMoves();

    String getName();

    void setX(int x);

    void setY(int y);

    int getX();

    int getY();

    void printData();
}