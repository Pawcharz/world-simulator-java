package Organisms.Plants;

import Utils.ORGANISM_TYPE;
import World.World;

import java.awt.geom.Point2D;

public class Guarana extends Plant {
    public Guarana(Point2D _position) {
        super(_position, 0);
        type = ORGANISM_TYPE.GUARANA;
    }

    @Override
    protected Plant GrowNew(Point2D spreadPosition) {
        World world = World.GetInstance();

        Plant child = new Guarana(spreadPosition);
        world.GetOrganisms().add(child);

        return child;
    }
}
