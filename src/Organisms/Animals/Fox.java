package Organisms.Animals;

import Organisms.Organism;
import Utils.ORGANISM_TYPE;
import World.World;

import java.awt.geom.Point2D;
import java.util.ArrayList;

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

    @Override
    protected ArrayList<Point2D> GetPositionsToMove() {
        World world = World.GetInstance();

        ArrayList<Point2D> positions = world.GetFieldsInRadius(position, 1);
        int positionsCount = positions.size();

        ArrayList<Point2D> available = new ArrayList<Point2D>();

        for (int i = 0; i < positionsCount; i++)
        {
            Point2D neighbouringPosition = positions.get(i);
            Organism organism = world.GetOrganismAtPosition(neighbouringPosition);

            if (organism == null) {
                available.add(neighbouringPosition);
            }
            else if (organism.GetStrength() <= strength) {
                available.add(neighbouringPosition);
            }
        }

        return available;
    }    
}