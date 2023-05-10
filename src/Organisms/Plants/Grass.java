package Organisms.Plants;

import Utlis.ORGANISM_TYPE;

import java.awt.geom.Point2D;

public class Grass extends Plant {
    public Grass(Point2D _position) {
        super(_position, 0);
        type = ORGANISM_TYPE.GRASS;
    }
}
