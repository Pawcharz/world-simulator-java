package Organisms.Animals;

import Utlis.ORGANISM_TYPE;

import java.awt.geom.Point2D;

public class Wolf extends Animal {
    public Wolf(Point2D _position) {
        super(_position, 9, 5);
        type = ORGANISM_TYPE.WOLF;
    }
}
