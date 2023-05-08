package Organisms.Animals;

import Organisms.Organism;

import java.awt.geom.Point2D;

public class Animal extends Organism {
    protected Animal(Point2D _position, int _strength, int _initiative) {
        super(_position, _strength, _initiative);
    }

    @Override
    public void Action() {

    }
}
