package Organisms.Plants;

import Utlis.ORGANISM_TYPE;

import java.awt.geom.Point2D;

public class SosnowskysHogweed extends Plant {
    public SosnowskysHogweed(Point2D _position) {
        super(_position, 10);
        type = ORGANISM_TYPE.SOSNOWSKYS_HOGWEED;
    }
}
