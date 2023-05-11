package Organisms.Plants;

import Utils.ORGANISM_TYPE;

import java.awt.geom.Point2D;

public class Belladonna extends Plant {
    public Belladonna(Point2D _position) {
        super(_position, 99);
        type = ORGANISM_TYPE.BELLADONNA;
    }
}
