package Organisms.Plants;

import Utils.ORGANISM_TYPE;

import java.awt.geom.Point2D;

public class SowThistle extends Plant {
    public SowThistle(Point2D _position) {
        super(_position, 0);
        type = ORGANISM_TYPE.SOW_THISTLE;
    }
}
