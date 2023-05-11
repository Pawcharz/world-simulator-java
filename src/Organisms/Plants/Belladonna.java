package Organisms.Plants;

import Organisms.Animals.Animal;
import Organisms.Animals.Antelope;
import Utils.ORGANISM_TYPE;
import World.World;

import java.awt.geom.Point2D;

public class Belladonna extends Plant {
    public Belladonna(Point2D _position) {
        super(_position, 99);
        type = ORGANISM_TYPE.BELLADONNA;
    }

    @Override
    protected Plant GrowNew(Point2D spreadPosition) {
        World world = World.GetInstance();

        Plant child = new Belladonna(spreadPosition);
        world.GetOrganisms().add(child);

        return child;
    }
}
