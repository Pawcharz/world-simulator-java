package Organisms.Animals;

import Utils.ORGANISM_TYPE;
import World.World;

import java.awt.geom.Point2D;

public class Sheep extends Animal {
    public Sheep(Point2D _position) {
        super(_position, 4, 4);
        type = ORGANISM_TYPE.SHEEP;
    }

    @Override
    protected Animal GiveBirth(Point2D breedPosition) {
        World world = World.GetInstance();

        Animal child = new Sheep(breedPosition);
        world.GetOrganisms().add(child);

        return child;
    }


    @Override
    public String GetName() {
        return "Sheep";
    }
}
