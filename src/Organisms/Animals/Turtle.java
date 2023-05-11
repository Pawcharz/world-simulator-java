package Organisms.Animals;

import Utils.ORGANISM_TYPE;
import World.World;

import java.awt.geom.Point2D;

public class Turtle extends Animal {
    public Turtle(Point2D _position) {
        super(_position, 2, 1);
        type = ORGANISM_TYPE.TURTLE;
    }

    @Override
    protected Animal GiveBirth(Point2D breedPosition) {
        World world = World.GetInstance();

        Animal child = new Turtle(breedPosition);
        world.GetOrganisms().add(child);

        return child;
    }
}