package com.hepker.ai_checkers.utils;

import com.hepker.ai_checkers.entity.Entity;

public class EntityArray {
    private Entity[][] entityArray;
    private int numEntities;

    public EntityArray() {
        entityArray = new Entity[8][8];
    }

    public void addEntity(Entity e) {
        entityArray[e.getX()][e.getY()] = e;
        numEntities++;
    }

    public Entity getEntity(int column, int row) {
        return entityArray[column][row];
    }

    public void removeEntity(int column, int row) {
        entityArray[column][row] = null;
        numEntities--;
    }

    public int getNumEntities() {
        return numEntities;
    }

    public void printEntities() {
        for (int j = 0; j < entityArray.length; j++) {
            for (int i = 0; i < entityArray[j].length; i++) { // Access each element in row `j`
                if (entityArray[j][i] != null) { // Corrected the index
                    GameBoardPiece e = (GameBoardPiece) entityArray[j][i];
                    System.out.println("Entity is: " + entityArray[j][i] + " with (x,y): "
                            + e.getX() + ", " + e.getY());
                }
            }
        }
    }

}
