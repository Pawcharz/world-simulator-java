package Organisms.Animals;

import Organisms.Organism;
import Utils.DEFENCE_RESULT;
import Utils.ORGANISM_TYPE;
import World.World;

import java.awt.geom.Point2D;

public class Turtle extends Animal {

    private static final double MOVEMENT_CHANCE = 0.25;
    private static final int REFLECT_MAX_STRENGTH = 4;

    public Turtle(Point2D _position) {
        super(_position, 2, 1);
        type = ORGANISM_TYPE.TURTLE;
    }

    @Override
    protected Animal GiveBirth(Point2D breedPosition) {
        World world = World.GetInstance();

        Animal child = new Turtle(breedPosition);
        world.GetOrganisms().add(child);

        return child;
    }

    @Override
    protected void Movement() {
        double random = Math.random();

        if (random <= MOVEMENT_CHANCE) {
            ((Animal) this).Movement();
        }
    }

    @Override
    public DEFENCE_RESULT Defend(Organism attacker) {
        int attackerStength = attacker.GetStrength();

        if (attackerStength < strength) {
            return DEFENCE_RESULT.ATTACKER_KILLED;
        }
        else if (attackerStength <= REFLECT_MAX_STRENGTH) {
            return DEFENCE_RESULT.TARGET_BLOCKS;
        }

        return DEFENCE_RESULT.TARGET_KILLED;
    }
}