package Organisms.Plants;

import Utlis.ORGANISM_TYPE;

import java.awt.geom.Point2D;

public class Guarana extends Plant {
    public Guarana(Point2D _position) {
        super(_position, 0);
        type = ORGANISM_TYPE.GUARANA;
    }
}
