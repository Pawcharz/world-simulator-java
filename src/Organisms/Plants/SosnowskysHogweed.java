package Organisms.Plants;

import Organisms.Organism;
import Utils.DEFENCE_RESULT;
import Utils.ORGANISM_TYPE;
import World.World;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class SosnowskysHogweed extends Plant {
    public SosnowskysHogweed(Point2D _position) {
        super(_position, 10);
        type = ORGANISM_TYPE.SOSNOWSKYS_HOGWEED;
    }

    @Override
    protected Plant GrowNew(Point2D spreadPosition) {
        return null;
    }


    @Override
    public void Action() {
        World world = World.GetInstance();

        ArrayList<Point2D> neighbourhood = world.GetFieldsInRadius(position, 1);

        for (Point2D pos : neighbourhood) {
            Organism organism = world.GetOrganismAtPosition(pos);

            if (organism != null) {

                if (organism.GetType() != ORGANISM_TYPE.CYBER_SHEEP) {
                    organism.Die();
                }
            }
        }
    }

    @Override
    public DEFENCE_RESULT Defend(Organism attacker) {
        if (attacker.GetType() != ORGANISM_TYPE.CYBER_SHEEP) {
            attacker.Die();
            return DEFENCE_RESULT.ATTACKER_KILLED;
        }
        return super.Defend(attacker);
    }


    @Override
    public String GetName() {
        return "Sosnowsky's Hogweed";
    }
}
