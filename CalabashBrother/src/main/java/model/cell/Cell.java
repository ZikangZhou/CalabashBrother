package model.cell;

import model.Coordinate;
import model.Creature;

public class Cell {

    private final Coordinate coordinate;
    private Creature creature;

    Cell(Coordinate coordinate) {
        creature = null;
        this.coordinate = coordinate;
    }

    public Cell(Creature creature, Coordinate coordinate) {
        this.creature = creature;
        this.coordinate = coordinate;
    }

    public Creature getCreature() {
        return creature;
    }

    public void setCreature(Creature creature) {
        this.creature = creature;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public String getName() {
        return creature.getName();
    }

    void remove() {
        if (creature != null) {
            creature = null;
        }
    }

    public boolean isEmpty() {
        return creature == null;
    }
}

