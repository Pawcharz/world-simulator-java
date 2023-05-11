package Organisms.Plants;

import Utils.ORGANISM_TYPE;
import World.World;

import java.awt.geom.Point2D;

public class SosnowskysHogweed extends Plant {
    public SosnowskysHogweed(Point2D _position) {
        super(_position, 10);
        type = ORGANISM_TYPE.SOSNOWSKYS_HOGWEED;
    }

    @Override
    protected Plant GrowNew(Point2D spreadPosition) {
        return null;
    }
}
