package Organisms.Plants;

import Utils.Utils;
import World.World;
import World.Displayer;

import Organisms.Organism;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public abstract class Plant extends Organism {
    private static final double PLANT_PROBABILITY_TO_SPREAD = 0.05;

    protected Plant(Point2D _position, int _strength) {
        super(_position, _strength, 0);
    }

    @Override
    public void Action() {
        age += 1;
        Spread();
    }

    protected abstract Plant GrowNew(Point2D spreadPosition);
    void Spread() {
        Point2D spreadPosition = GetSpreadPosition();
        if (spreadPosition != null) {
            World world = World.GetInstance();

            Plant newPlant = GrowNew(spreadPosition);

            Displayer displayer = world.GetDisplayer();

            displayer.AddLog(GetDescription() + " spread creating new " + newPlant.GetDescription());
        }
    }

    private Point2D GetSpreadPosition() {
        World world = World.GetInstance();

        double random = Math.random();

        if (random > PLANT_PROBABILITY_TO_SPREAD) {
            return null;
        }

        ArrayList<Point2D> neighbouringFields = world.GetFreeNeighbouringFields(position);
        int length = neighbouringFields.size();

        if (length == 0) {
            return null;
        }
        int index = Utils.RandomInteger(0, length - 1);
        return neighbouringFields.get(index);
    }


    @Override
    public abstract String GetName();
}
