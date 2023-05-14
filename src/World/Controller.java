package World;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
        while (true) {
            while(!isActionKeyPressed) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            World world = World.GetInstance();

            boolean errorOccured = true;

            if (mode == SIMULATION_MODE.SIMULATION_PLAYING) {

                if(pressedCharacter == '.') {
                    pressedCharacter = 0;
                    world.SaveToFile("test.txt");
                    return;
                }
                if(pressedCharacter == ',') {
                    pressedCharacter = 0;
                    world.LoadFromFile("test.txt");
                    return;
                }

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
};
