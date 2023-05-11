package Organisms.Animals;

import Utils.ORGANISM_TYPE;
import World.World;

import java.awt.geom.Point2D;

public class Antelope extends Animal {
    public Antelope(Point2D _position) {
        super(_position, 4, 4);
        type = ORGANISM_TYPE.ANTILOPE;
    }

    @Override
    protected Animal GiveBirth(Point2D breedPosition) {
        World world = World.GetInstance();

        Animal child = new Antelope(breedPosition);
        world.GetOrganisms().add(child);

        return child;
    }
}