package utils;

import entity.Entity;

import java.util.ArrayList;

public class EntityList extends ArrayList<Entity> {

    public EntityList() {
        super();
    }

    public boolean addEntity(Entity entity) {
        return super.add(entity);
    }

    public boolean removeEntity(Entity entity) {
        return super.remove((entity));
    }

    public int getNumEntities() {
        return super.size();
    }

    public void printEntities() {
        for (Entity e : this) {
            System.out.println("Entity is: " + e.getName() + " with (x,y): " + e.getX() + ", " + e.getY());
        }
    }
}
