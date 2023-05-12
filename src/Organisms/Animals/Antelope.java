package Organisms.Animals;

import Organisms.Organism;
import Utils.DEFENCE_RESULT;
import Utils.ORGANISM_TYPE;
import Utils.Utils;
import World.World;
import World.Displayer;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Antelope extends Animal {
    private static final float ANTELOPE_ESCAPE_CHANCE = 1F;

    public Antelope(Point2D _position) {
        super(_position, 4, 4);
        type = ORGANISM_TYPE.ANTILOPE;
    }

    @Override
    protected Animal GiveBirth(Point2D breedPosition) {
        World world = World.GetInstance();

        Animal child = new Antelope(breedPosition);
        world.GetOrganisms().add(child);

        return child;
    }

    @Override
    protected void Attack(Organism target) {
        World world = World.GetInstance();
        Displayer displayer = world.GetDisplayer();

        Point2D targetPosition = target.GetPosition();

        DEFENCE_RESULT result = target.Defend(this);

        if (result == DEFENCE_RESULT.TARGET_KILLED) {
            displayer.AddLog(GetDescribtion() + " killed " + target.GetDescribtion());

            position = targetPosition;
            target.Die();
        }
        else if (result == DEFENCE_RESULT.ATTACKER_KILLED) {

            double random = Math.random();
            if (random <= ANTELOPE_ESCAPE_CHANCE) {

                boolean escapeSuccess = EscapeFromFight(position);

                if (escapeSuccess) {
                    displayer.AddLog(GetDescribtion() + " attacked and escaped from " + target.GetDescribtion());
                }
                else {
                    displayer.AddLog(target.GetDescribtion() + " killed " + GetDescribtion());
                    this.Die();
                }
            }
            else {
                displayer.AddLog(target.GetDescribtion() + " killed " + GetDescribtion());
                this.Die();
            }
        }
        else if (result == DEFENCE_RESULT.TARGET_ESCAPED) {
            displayer.AddLog(target.GetDescribtion() + " escaped from " + GetDescribtion());
            position = targetPosition;
        }
    }

    @Override
    public DEFENCE_RESULT Defend(Organism attacker) {
        World world = World.GetInstance();

        double random = Math.random();

        if (random > ANTELOPE_ESCAPE_CHANCE) {
            return ((Animal) this).Defend(attacker);
        }

        // Tries to escape
        boolean escapeSuccess = EscapeFromFight(position);

        if (escapeSuccess) {
            return DEFENCE_RESULT.TARGET_ESCAPED;
        }

        return DEFENCE_RESULT.TARGET_KILLED;
    }
    private boolean EscapeFromFight(Point2D startPosition) {
        World world = World.GetInstance();
        ArrayList<Point2D> availablePositions = world.GetFreeNeighbouringFields(startPosition);
        int positionsCount = availablePositions.size();

        if (positionsCount == 0) {
            return false;
        }

        int index = Utils.RandomInteger(0, positionsCount);

        Point2D newPosition = availablePositions.get(index);

        position = newPosition;
        return true;
    }
}