package World;

import Organisms.Organism;

import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controller extends KeyAdapter {
    private static final char KEY_ENTER = '\n';
    private char pressedCharacter;

    private SIMULATION_MODE mode;

    private String fileName;

    private boolean isActionKeyPressed = false;

    Controller() {
        mode = SIMULATION_MODE.SIMULATION_PLAYING;
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

                if(pressedCharacter == '.') {
                    isActionKeyPressed = false;
                    mode = SIMULATION_MODE.FILE_SAVING;
                    return;
                }
                else if(pressedCharacter == ',') {
                    isActionKeyPressed = false;

                    mode = SIMULATION_MODE.FILE_LOADING;
                    return;
                }
                else {
                    errorOccured = world.GetPlayer().HandleControlledAction();

                    isActionKeyPressed = false;

                    if (!errorOccured) {
                        return;
                    }
                }
            }
        }
    }

    public void keyPressed(KeyEvent event) {
        pressedCharacter = event.getKeyChar();

        List<Character> available = Arrays.asList('a', 's', 'd', 'w', ' ', '.', ',');

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

    public String GetFileName() {
        return fileName;
    }

    public MouseListener GetCellClickAListener(int cellX, int cellY) {
        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Mouse clicked cell at (" + cellX + ", " + cellY + ")");

                World world = World.GetInstance();

                Organism existing = world.GetOrganismAtPosition(new Point2D.Double(cellX, cellY));

                if (existing == null) {
                    mode = SIMULATION_MODE.ADDING_ORGANISM;
                }
            }
        };

        return mouseListener;
    }

    public void SetMode(SIMULATION_MODE newMode) {
        mode = newMode;
    }

    public ActionListener GetSaveButtonListener() {
        ActionListener mouseListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                World world = World.GetInstance();

                mode = SIMULATION_MODE.FILE_SAVING;
//                world.SaveToFile("test.txt");
            }
        };

        return mouseListener;
    }
};
