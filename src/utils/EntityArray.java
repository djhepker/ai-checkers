package utils;

import entity.Entity;

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
        for (int i = 0; i < entityArray.length; i++) {
            for (int j = 0; j < entityArray[i].length; j++) {
                System.out.println("Entity is: " + entityArray[j][i] + " with (x,y): " + j + ", " + i);
            }
        }
    }
}
