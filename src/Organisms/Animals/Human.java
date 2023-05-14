package Organisms.Animals;

import Organisms.Organism;
import Utils.Utils;
import World.World;
import World.Controller;
import World.Displayer;
import Utils.ORGANISM_TYPE;

import java.awt.geom.Point2D;

public class Human extends Animal {
    private int specialAbilityCooldown;

    private int baseStrength;

    private int strengthBuff;

    public Human(Point2D _position) {
        super(_position, 5, 5);

        baseStrength = 5;
        strengthBuff = 0;
        specialAbilityCooldown = 0;

        type = ORGANISM_TYPE.HUMAN;
    }

    @Override
    protected Animal GiveBirth(Point2D breedPosition) {
        return null;
    }

    public boolean HandleControlledAction() {
        boolean errorOccured = false;
        World world = World.GetInstance();
        Controller controller = world.GetController();
        Displayer displayer = world.GetDisplayer();

        char pressedKey = controller.GetPressedCharacter();

        if (pressedKey == ' ') {
            if (specialAbilityCooldown == 0) {
                UseSpecialAbility();
            }
            else {
                displayer.AddLog("You cant use this ability for the next " + specialAbilityCooldown + " turns.");
                displayer.UpdateInterface();
                errorOccured = true;
            }
        }
        else if(pressedKey != 0) {
            boolean success = HandleMovement();
		    errorOccured = !success;
        }

        return errorOccured;
    }

    public void Action() {
        World world = World.GetInstance();
        Controller controller = world.GetController();

        world.GetDisplayer().UpdateInterface();
        controller.ProcessKeyboardInput();

        UpdateState();
        world.GetDisplayer().UpdateInterface();
    }

    private void UseSpecialAbility() {
        strengthBuff = 5;
        specialAbilityCooldown = 5;

        Displayer displayer = World.GetInstance().GetDisplayer();
        displayer.AddLog("You drink magic potion, strength + " + strengthBuff);
    }

    private void UpdateState() {
        strength = baseStrength + strengthBuff;

        strengthBuff -= 1;

        if (strengthBuff < 0) {
            strengthBuff = 0;
        }

        specialAbilityCooldown -= 1;

        if (specialAbilityCooldown < 0) {
            specialAbilityCooldown = 0;
        }
    }

    private boolean HandleMovement() {
        World world = World.GetInstance();
        Controller controller = world.GetController();
        Displayer displayer = world.GetDisplayer();

        char pressedKey = controller.GetPressedCharacter();

        Point2D moveTo = null;

        if (pressedKey == 'a') {
            moveTo = Utils.PointsSum(position, new Point2D.Double(-1, 0));
        }
        else if (pressedKey == 'd') {
            moveTo = Utils.PointsSum(position, new Point2D.Double(1, 0));
        }
        else if (pressedKey == 'w') {
            moveTo = Utils.PointsSum(position, new Point2D.Double(0, 1));
        }
        else if (pressedKey == 's') {
            moveTo = Utils.PointsSum(position, new Point2D.Double(0, -1));
        }

        if (world.IsWithinBorders(moveTo)) {
            Organism target = world.GetOrganismAtPosition(moveTo);

            displayer.ResetLogs();
            if (target != null) {

                Attack(target);
                return true;
            }

		    position = moveTo;
            return true;
        }
        else {
            displayer.ResetLogs();
            displayer.AddLog("Tried to move outside of the map, try again");
            displayer.UpdateInterface();
            return false;
        }
    }


    @Override
    public String GetName() {
        return "Human";
    }

    public void SetStrengthBuff(int newStrengthBuff) {
        strengthBuff = newStrengthBuff;
    }
    public int GetStrengthBuff() {
        return strengthBuff;
    }

    public void SetAbilityCooldown(int newCooldown) {
        specialAbilityCooldown = newCooldown;
    }
    public int GetAbilityCooldown() {
        return specialAbilityCooldown;
    }



}