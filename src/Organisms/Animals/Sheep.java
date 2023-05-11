package Organisms.Animals;

import Utils.ORGANISM_TYPE;

import java.awt.geom.Point2D;

public class Sheep extends Animal {
    public Sheep(Point2D _position) {
        super(_position, 4, 4);
        type = ORGANISM_TYPE.SHEEP;
    }
}
