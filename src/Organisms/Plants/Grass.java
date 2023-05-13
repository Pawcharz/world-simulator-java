package Organisms.Plants;

import Utils.ORGANISM_TYPE;
import World.World;

import java.awt.geom.Point2D;

public class Grass extends Plant {
    public Grass(Point2D _position) {
        super(_position, 0);
        type = ORGANISM_TYPE.GRASS;
    }

    @Override
    protected Plant GrowNew(Point2D spreadPosition) {
        World world = World.GetInstance();

        Plant child = new Grass(spreadPosition);
        world.GetOrganisms().add(child);

        return child;
    }


    @Override
    public String GetName() {
        return "Grass";
    }
}
