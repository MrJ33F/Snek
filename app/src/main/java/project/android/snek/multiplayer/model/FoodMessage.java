package project.android.snek.multiplayer.model;

import project.android.snek.utils.Vector;

public class FoodMessage {

    private int id;
    private Vector position;
    private double radius;
    private int color;

    public int getColor() {
        return color;
    }

    public int getId() {
        return id;
    }

    public Vector getPosition() {
        return position;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return "FoodMessage{" +
                "id=" + id +
                ", position=" + position +
                ", radius=" + radius +
                '}';
    }
}
