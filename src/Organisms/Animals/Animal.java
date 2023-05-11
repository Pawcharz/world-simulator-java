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


    private void Movement() {

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

    private void Attack(Organism target) {
        World world = World.GetInstance();

        Point2D targetInitialPosition = target.GetPosition();

        DEFENCE_RESULT result = target.Defend(this);

        Displayer displayer = world.GetDisplayer();


        if (result == DEFENCE_RESULT.TARGET_KILLED) {
            displayer.AddLog(GetDescribtion() + " killed " + target.GetDescribtion());

            position = targetInitialPosition;
            target.Die();
        }
        else if (result == DEFENCE_RESULT.ATTACKER_KILLED) {
            displayer.AddLog(target.GetDescribtion() + " killed " + GetDescribtion());

            Die();
        }
        else if (result == DEFENCE_RESULT.TARGET_ESCAPED) {

            // FIX - Should probably be implemented in the antelope class
            ArrayList<Point2D> availablePositions = world.GetFieldsInRadius(targetInitialPosition, 1);
            int positionsCount = availablePositions.size();

            if (positionsCount == 0) {
                displayer.AddLog(GetDescribtion() + " killed " + target.GetDescribtion());

                position = target.GetPosition();
                target.Die();

                return;
            }

            position = targetInitialPosition;

            int index = Utils.RandomInteger(0, positionsCount);

            displayer.AddLog(target.GetDescribtion() + " escaped from attack of " + GetDescribtion());

            target.SetPosition(availablePositions.get(index));

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
        displayer.AddLog(GetDescribtion() + " and " + partner.GetDescribtion() + " born the new " + child.GetDescribtion());
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

    private ArrayList<Point2D> GetPositionsToMove() {
        World world = World.GetInstance();

        return world.GetFieldsInRadius(position, 1);
    }


    @Override
    public void Action() {
        age += 1;
        Movement();
    }
}
