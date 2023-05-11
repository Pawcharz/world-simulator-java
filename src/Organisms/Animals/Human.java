package Organisms.Animals;

import Utils.ORGANISM_TYPE;

import java.awt.geom.Point2D;

public class Human extends Animal {
    public Human(Point2D _position) {
        super(_position, 5, 4);
        type = ORGANISM_TYPE.HUMAN;
    }
}