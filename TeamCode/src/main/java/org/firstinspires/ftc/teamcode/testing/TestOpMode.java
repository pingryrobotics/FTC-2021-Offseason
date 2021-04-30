package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.testing.GamepadController.ToggleButton;
import org.firstinspires.ftc.teamcode.testing.GamepadController.ButtonState;
import org.firstinspires.ftc.teamcode.testing.GamepadController.FloatButton;


@TeleOp(name="Testing: Test opmode", group="Testing")
public class TestOpMode extends OpMode {
    // field declarations

    private ObjectDetector tfod;
    private GamepadController movementController;
    private GamepadController mechanismController;



    @Override
    public void init() {
        movementController = new GamepadController(gamepad1);
        movementController = new GamepadController(gamepad2);
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
    }

    @Override
    public void loop() {

        runControls();
    }

    /**
     * Does controls for the gamepads
     */
    public void runControls() {
        // examples to show how to use this
        ButtonState A = mechanismController.getButtonState(ToggleButton.A);
        switch (A) {
            case KEY_DOWN:
                //do something when the button is pressed
                break;
            case KEY_HOLD:
                // do something repeatedly while the button is held
                break;
            case KEY_UP:
                // do something when the button is released
                break;
            case KEY_INACTIVE:
                // do something repeatedly while the button isnt being held
                break;
        }

        float left_stick_x = movementController.getButtonState(FloatButton.LEFT_STICK_X);
        float left_stick_y = movementController.getButtonState(FloatButton.LEFT_STICK_Y);

    }



    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }
}
