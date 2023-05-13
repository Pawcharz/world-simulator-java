package Organisms.Animals;

import Utils.ORGANISM_TYPE;
import World.World;

import java.awt.geom.Point2D;

public class Wolf extends Animal {
    public Wolf(Point2D _position) {
        super(_position, 9, 5);
        type = ORGANISM_TYPE.WOLF;
    }

    @Override
    protected Animal GiveBirth(Point2D breedPosition) {
        World world = World.GetInstance();

        Animal child = new Wolf(breedPosition);
        world.GetOrganisms().add(child);

        return child;
    }


    @Override
    public String GetName() {
        return "Wolf";
    }
}
