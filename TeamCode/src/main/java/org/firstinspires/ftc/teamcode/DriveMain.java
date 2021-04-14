/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all iterative OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

/*
    Upload to robot:
    1. connect to robot wifi (6069 RC)
    2. Tools > External Tools > ADB Connect
        - make sure device says REV Robotics control hub v1.0 or something
    3. Green play button (run button)
 */

@TeleOp(name="Basic: Iterative OpMode", group="Iterative Opmode")
@Disabled
public class DriveMain extends OpMode
{
    // Declare OpMode members.

    private ElapsedTime runtime;
    private MecanumDrive mecanumDrive;
    private int offsetAngle;
    private Outtake outtake;
    private WobbleMech wobbleMech;
    private Intake intake;
    private boolean[] previousButtonStates;


    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initializing");
        mecanumDrive = new MecanumDrive(hardwareMap);
        outtake = new Outtake(hardwareMap);
        intake = new Intake(hardwareMap);
        wobbleMech = new WobbleMech(hardwareMap);
        runtime = new ElapsedTime();
        offsetAngle = 0;
        previousButtonStates = updateButtonList();
        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).


        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
    }


    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {

        boolean[] currentButtonStates = updateButtonList();
        // Setup a variable for each drive wheel to save power level for telemetry

        // Show the elapsed game time and wheel power.
        double speed = 0.5;
        //if(gamepad1.right_trigger > 0.5){
        //    speed += (1-speed)*(2*(gamepad1.right_trigger - 0.5));
        //}
        double magnitude = Math.hypot(-gamepad1.left_stick_x, gamepad1.left_stick_y);
        double robotAngle = Math.atan2(gamepad1.left_stick_y, -gamepad1.left_stick_x) - Math.PI / 4;
        telemetry.addData("robot angle", robotAngle);
        robotAngle += offsetAngle / 180.0 * Math.PI;
        double rightX = -gamepad1.right_stick_x;
        mecanumDrive.polarMove(robotAngle, rightX, speed * magnitude);
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.update();

        // Turn flywheel on


        if (previousButtonStates[0] != currentButtonStates[0]) {
            if (currentButtonStates[0]) {
              intake.intake_in_both();
            } else {
              intake.stop();
            }
        } else if (previousButtonStates[1] != currentButtonStates[1]) {
            if (currentButtonStates[1]) {
              outtake.pushRing();
            }
        } else if (previousButtonStates[2] != currentButtonStates[2]) {
            if (currentButtonStates[2]) {
              wobbleMech.letGo();
            }
        } else if(previousButtonStates[3] != currentButtonStates[3]) {
            if (currentButtonStates[3]) {
              wobbleMech.grab();
            }
        } else if (previousButtonStates[4] != currentButtonStates[4]) {
          if (currentButtonStates[4]){
            wobbleMech.up();
          }
        } else if (previousButtonStates[5] != currentButtonStates[5]) {
          if (currentButtonStates[5]) {
            wobbleMech.down();
          }
        } else if (previousButtonStates[6] != currentButtonStates[6]) {
          if(currentButtonStates[6]){
            outtake.outtake();
          }
        } else if (previousButtonStates[7] != currentButtonStates[7]) {
          if (currentButtonStates[7]) {
            outtake.stop();
          }
        } else if (previousButtonStates[8] != currentButtonStates[8]) {
          if (currentButtonStates[8]) {
            intake.intake_out_both();
          } else {
            intake.stop();
          }
        }

        previousButtonStates = currentButtonStates;
        // if(gamepad1.a){
        //     intake.intake_in();
        // }
        // else if(gamepad1.b){
        //     intake.intake_out();
        // }
        // else if(gamepad1.x){
        //     ramp.upRamp();
        // }
        // else if(gamepad1.right_bumper){
        //     ramp.downRamp();
        // }
        // else if(gamepad1.left_bumper){
        //     outtake.outtake();
        // }
    }
    /*
    Controls:
    left bumper - intake
    right bumper - push servo + outtake + retract servo


    left joystck - movement
    right joystick - direction

    a, b, x, y: automations

    up, down, left, right: backups
    up - push servo
    down - retract servo

    */

    /**
    Updates button list
    */
    public boolean[] updateButtonList() {
        // Array with Button values order
        // Left Trigger, Right trigger, x, y, left_bumper, right_bumper, b, back
        boolean[] buttonList = {
          // main controls
          (gamepad1.left_trigger > 0), // intake
          (gamepad1.right_trigger > 0), // shoot
          gamepad1.x, // open wobble grabber
          gamepad1.y, // close wobble
          gamepad1.left_bumper, // bring arm up
          gamepad1.right_bumper, // bring arm down
          gamepad1.a, // turn flywheel on
          gamepad1.b, // turn flywheel off
          // gamepad1.back,
          gamepad1.dpad_down // reverse intake
        };

        return buttonList;
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        mecanumDrive.brake();
        intake.stop();
        outtake.stop();
        wobbleMech.stop();
    }

}
