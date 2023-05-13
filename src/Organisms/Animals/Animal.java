package Organisms.Animals;

import Organisms.Organism;
import Utils.Utils;
import Utils.DEFENCE_RESULT;

import World.World;
import World.Displayer;

import java.awt.geom.Point2D;
import java.util.ArrayList;


public abstract class Animal extends Organism {
    protected Animal(Point2D _position, int _strength, int _initiative) {
        super(_position, _strength, _initiative);
    }


    protected void Movement() {

        World world = World.GetInstance();

        ArrayList<Point2D> availablePositions = GetPositionsToMove();
        int positionsCount = availablePositions.size();
        if (positionsCount == 0) {
            return;
        }

        int index = Utils.RandomInteger(0, positionsCount);

        Point2D newPosition = availablePositions.get(index);

        Organism foundOrganism = world.GetOrganismAtPosition(newPosition);

        if (foundOrganism != null) {
            Collision(foundOrganism);
        }
        else {
            position = newPosition;
        }
    }

    private void Collision(Organism target) {
        if (target.GetType() == type) {
            int BREEDING_MIN_AGE = 3; // FIX - move to global scope
            if (target.GetAge() >= BREEDING_MIN_AGE && GetAge() >= BREEDING_MIN_AGE) {
                Breed((Animal) target);
            }
        }
        else {
            Attack(target);
        }
    }

    protected void Attack(Organism target) {
        World world = World.GetInstance();

        Point2D targetInitialPosition = target.GetPosition();

        DEFENCE_RESULT result = target.Defend(this);

        Displayer displayer = world.GetDisplayer();


        if (result == DEFENCE_RESULT.TARGET_KILLED) {
            displayer.AddLog(GetDescription() + " killed " + target.GetDescription());

            position = targetInitialPosition;
            target.Die();
        }
        else if (result == DEFENCE_RESULT.ATTACKER_KILLED) {
            displayer.AddLog(target.GetDescription() + " killed " + GetDescription());

            Die();
        }
        else if (result == DEFENCE_RESULT.TARGET_ESCAPED) {
            position = targetInitialPosition;

            displayer.AddLog(target.GetDescription() + " escaped from attack of " + GetDescription());
        }
        else if (result == DEFENCE_RESULT.TARGET_BLOCKS) {
            displayer.AddLog(target.GetDescription() + " blocks attack of " + GetDescription());
        }
    }

    protected abstract Animal GiveBirth(Point2D breedPosition);
    private void Breed(Animal partner) {
        World world = World.GetInstance();
        Point2D breedPosition = partner.GetBreedPosition(partner.GetPosition());

        if (breedPosition == null) {
            return;
        }

        Animal child = GiveBirth(breedPosition);

        Displayer displayer = world.GetDisplayer();
        displayer.AddLog(GetDescription() + " and " + partner.GetDescription() + " born the new " + child.GetDescription());
    }

    private Point2D GetBreedPosition(Point2D partnerPosition) {
        World world = World.GetInstance();

        ArrayList<Point2D> neighbouringFields = world.GetFreeNeighbouringFields(partnerPosition);
        int length = neighbouringFields.size();

        if (length == 0) {
            neighbouringFields = world.GetFreeNeighbouringFields(partnerPosition);
            length = neighbouringFields.size();
        }

        if (length == 0) {
            return null;
        }

        int index = Utils.RandomInteger(0, length - 1);
        return neighbouringFields.get(index);
    }

    protected ArrayList<Point2D> GetPositionsToMove() {
        World world = World.GetInstance();

        return world.GetFieldsInRadius(position, 1);
    }

    public DEFENCE_RESULT Defend(Animal attacker) {
        return ((Organism) this).Defend(attacker);
    }

    @Override
    public void Action() {
        age += 1;
        Movement();
    }

    @Override
    public abstract String GetName();
}
