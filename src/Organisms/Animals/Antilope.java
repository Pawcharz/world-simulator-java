package Organisms.Animals;

import Utils.ORGANISM_TYPE;

import java.awt.geom.Point2D;

public class Antilope extends Animal {
    public Antilope(Point2D _position) {
        super(_position, 4, 4);
        type = ORGANISM_TYPE.ANTILOPE;
    }
}