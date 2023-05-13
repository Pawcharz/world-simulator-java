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
        super(_position, 5, 4);
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
                String message = "You cant use this ability for the next " + specialAbilityCooldown + " turns.";
                displayer.AddLog(message);
            }
        }
        else {
            boolean success = HandleMovement();
		    errorOccured = !success;
        }

        return errorOccured;
    }

    public void Action() {
        World world = World.GetInstance();
        Controller controller = world.GetController();

        controller.ProcessKeyboardInput();

        UpdateState();
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

        Point2D moveTo = position;

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
            return false;
        }
    }
}