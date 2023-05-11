package Organisms;

import Utils.DEFENCE_RESULT;
import Utils.ORGANISM_TYPE;

import java.awt.geom.Point2D;

public abstract class Organism
{
    protected int strength;
    protected int initiative;
    protected int age;
    protected Point2D position;
    protected boolean alive;

    protected ORGANISM_TYPE type;

    protected Organism(Point2D _position, int _strength, int _initiative) {
        position = _position;
        strength = _strength;
        initiative = _initiative;

        alive = true;
        age = 0;

        type = ORGANISM_TYPE.UNKNOWN;
    }


    public abstract void Action();

    protected DEFENCE_RESULT Defend(Organism attacker) {
        return null;
    }


    public void SetStrength(int newStrength) {
        strength = newStrength;
    }

    public int GetStrength() {
        return strength;
    }


    public void SetInitiative(int newInitiative) {
        initiative = newInitiative;
    }

    public int GetInitiative() {
        return initiative;
    }


    public void SetPosition(Point2D newPosition) {
        position = newPosition;
    }

    public Point2D GetPosition() {
        return position;
    }


    public void Die() {
        alive = false;
    }

    public boolean IsAlive() {
        return alive;
    }


    public void SetAge(int newAge) {
        age = newAge;
    }

    public int GetAge() {
        return age;
    }


    public String GetDescribtion() {
        return "No Description";
    }


    public ORGANISM_TYPE GetType() {
        return type;
    }
};