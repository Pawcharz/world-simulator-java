package Organisms.Animals;

import Utils.ORGANISM_TYPE;
import World.World;

import java.awt.geom.Point2D;

public class Fox extends Animal {
    public Fox(Point2D _position) {
        super(_position, 3, 7);
        type = ORGANISM_TYPE.FOX;
    }

    @Override
    protected Animal GiveBirth(Point2D breedPosition) {
        World world = World.GetInstance();

        Animal child = new Fox(breedPosition);
        world.GetOrganisms().add(child);

        return child;
    }
}