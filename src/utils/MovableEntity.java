package utils;

public interface MovableEntity {
    int[][] getTheoreticalMoves();

    void setX(int x);

    void setY(int y);

    int getX();

    int getY();
}