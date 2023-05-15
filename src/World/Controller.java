package World;

import Organisms.Organism;
import Utils.AVAILABLE_ORGANISM_TYPE;
import Utils.ORGANISM_TYPE;

import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controller extends KeyAdapter {
    private static final char KEY_ENTER = '\n';
    private char pressedCharacter;

    private SIMULATION_MODE mode;

    private boolean isActionKeyPressed;

    private Point2D addOrganismPosition;

    Controller() {
        mode = SIMULATION_MODE.SIMULATION_PLAYING;
        addOrganismPosition = null;
        isActionKeyPressed = false;
    }

    public void ProcessKeyboardInput() {
        World world = World.GetInstance();

        while (true) {
            while(!isActionKeyPressed) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if(world.GetController().GetMode() != SIMULATION_MODE.SIMULATION_PLAYING) {
                    return;
                }
            }


            boolean errorOccured = true;

            if (mode == SIMULATION_MODE.SIMULATION_PLAYING) {

                errorOccured = world.GetPlayer().HandleControlledAction();

                isActionKeyPressed = false;

                if (!errorOccured) {
                    return;
                }
            }
        }
    }

    public void keyPressed(KeyEvent event) {
        pressedCharacter = event.getKeyChar();

        List<Character> available = Arrays.asList('a', 's', 'd', 'w', ' ');

        if(available.contains(pressedCharacter)) {
            isActionKeyPressed = true;
        }
        else {
            Displayer displayer = World.GetInstance().GetDisplayer();
            displayer.AddLog("Invalid key pressed - try again");
            displayer.UpdateInterface();
        }
    }

    public char GetPressedCharacter() {
        return pressedCharacter;
    }

    public SIMULATION_MODE GetMode() {
        return mode;
    }

    public MouseListener GetCellClickListener(int cellX, int cellY) {
        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(addOrganismPosition != null) {
                    return;
                }

                System.out.println("Mouse clicked cell at (" + cellX + ", " + cellY + ")");

                World world = World.GetInstance();

                Organism existing = world.GetOrganismAtPosition(new Point2D.Double(cellX, cellY));

                if (existing == null) {
                    addOrganismPosition = new Point2D.Double(cellX, cellY);
                    mode = SIMULATION_MODE.ADDING_ORGANISM;
                }
                else {
                    addOrganismPosition = null;
                }
            }
        };

        return mouseListener;
    }

    private void CloseAddOrganismWindow() {
        World world = World.GetInstance();
        addOrganismPosition = null;
        world.GetController().SetMode(SIMULATION_MODE.SIMULATION_PLAYING);
        world.GetDisplayer().SetAddOrganismPopupVisibility(false);
    }
    public WindowAdapter GetCloseAddOrganismWindowListener() {
        return new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                CloseAddOrganismWindow();
            }
        };
    }

    public void SetMode(SIMULATION_MODE newMode) {
        mode = newMode;
    }

    public ActionListener GetSaveButtonListener() {
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = SIMULATION_MODE.FILE_SAVING;
            }
        };

        return listener;
    }

    public ActionListener GetLoadButtonListener() {
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = SIMULATION_MODE.FILE_LOADING;
            }
        };

        return listener;
    }

    public ActionListener GetAddOrganismListener() {
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                World world = World.GetInstance();
                ORGANISM_TYPE type = world.GetDisplayer().GetSelectedOrganism();
                System.out.println(type);

                if(type == null) {
                    return;
                }

                world.CreateOrganism(type, addOrganismPosition);
                CloseAddOrganismWindow();
            }
        };

        return listener;
    }
};
