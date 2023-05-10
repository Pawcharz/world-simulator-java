package Organisms.Animals;

import Utlis.ORGANISM_TYPE;

import java.awt.geom.Point2D;

public class Fox extends Animal {
    public Fox(Point2D _position) {
        super(_position, 3, 7);
        type = ORGANISM_TYPE.FOX;
    }
}