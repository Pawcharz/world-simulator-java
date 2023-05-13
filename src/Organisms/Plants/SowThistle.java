package Organisms.Plants;

import Utils.ORGANISM_TYPE;
import World.World;

import java.awt.geom.Point2D;

public class SowThistle extends Plant {
    public SowThistle(Point2D _position) {
        super(_position, 0);
        type = ORGANISM_TYPE.SOW_THISTLE;
    }

    @Override
    protected Plant GrowNew(Point2D spreadPosition) {
        World world = World.GetInstance();

        Plant child = new SowThistle(spreadPosition);
        world.GetOrganisms().add(child);

        return child;
    }

    @Override
    void Spread() {

        for (int i = 0; i < 3; i++)
        {
            super.Spread();
        }
    }


    @Override
    public String GetName() {
        return "Sow Thistle";
    }
}
