package Organisms.Animals;

import Utlis.ORGANISM_TYPE;

import java.awt.geom.Point2D;

public class Turtle extends Animal {
    public Turtle(Point2D _position) {
        super(_position, 2, 1);
        type = ORGANISM_TYPE.TURTLE;
    }
}