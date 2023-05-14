package Organisms;

import Organisms.Animals.*;

import Organisms.Plants.*;
import Utils.ORGANISM_TYPE;

import java.awt.geom.Point2D;

public class OrganismFactory {


    public static Organism Create(ORGANISM_TYPE organismType, Point2D _position) {
        switch (organismType) {
            case SHEEP:
                return new Sheep(_position);
            case WOLF:
                return new Wolf(_position);
            case FOX:
                return new Fox(_position);
            case TURTLE:
                return new Turtle(_position);
            case ANTELOPE:
                return new Antelope(_position);
            case GRASS:
                return new Grass(_position);
            case SOW_THISTLE:
                return new SowThistle(_position);
            case GUARANA:
                return new Guarana(_position);
            case BELLADONNA:
                return new Belladonna(_position);
            case SOSNOWSKYS_HOGWEED:
                return new SosnowskysHogweed(_position);
            default:
                return null;
        }
    }
}
