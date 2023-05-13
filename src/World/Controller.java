package World;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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
            errorOccured = world.GetPlayer().HandleControlledAction();

            isActionKeyPressed = false;

            if (!errorOccured) {
                return;
            }
        }

    }

    public void keyPressed(KeyEvent event) {
        pressedCharacter = event.getKeyChar();
        isActionKeyPressed = true;
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
