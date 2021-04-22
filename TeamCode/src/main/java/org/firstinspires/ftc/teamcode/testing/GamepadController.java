package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.Hashtable;

public class GamepadController implements Gamepad.GamepadCallback {
    private Gamepad gamepad;
    public Hashtable<Button, ButtonState> buttonStates;

    public GamepadController(Gamepad gamepad) {
        this.gamepad = gamepad;
        buttonStates = new Hashtable<Button, ButtonState>();
        // set all to inactive (maybe loop or something)
        buttonStates.put(Button.A, ButtonState.KEY_INACTIVE);
        buttonStates.put(Button.B, ButtonState.KEY_INACTIVE);
        updateButtonStates();
    }

    public void gamepadChanged(Gamepad updatedGamepad) {
        if (updatedGamepad == gamepad) {
            updateButtonStates();
        }
    }

    public void updateButtonStates() {

    }

    // maybe have different enum for variable buttons like triggers and joysticks
    public enum Button {
        A,
        B,
        X,
        Y,
        DPAD_UP,
        DPAD_DOWN,
        DPAD_LEFT,
        DPAD_RIGHT,
        LEFT_TRIGGER,
        LEFT_BUMPER,
        RIGHT_TRIGGER,
        RIGHT_BUMPER; // determine whether or not to use triggers as buttons, maybe make a state for that
    }

    public enum ButtonState {
        KEY_DOWN,
        KEY_UP,
        KEY_HOLD,
        KEY_INACTIVE
    }
}
