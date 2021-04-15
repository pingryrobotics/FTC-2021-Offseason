package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo; //only if we need the additional feeder.
import com.qualcomm.robotcore.hardware.HardwareMap;


public class WobbleMech {
    private DcMotor motor;
    private Servo servo;
    //private Servo feeder;
    public WobbleMech(HardwareMap hardwareMap) {
        motor = hardwareMap.get(DcMotor.class, "wobbleMotor");
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        servo = hardwareMap.get(Servo.class, "wobbleServo");
        servo.setDirection(Servo.Direction.FORWARD);
        servo.scaleRange(0, .25);
    }

    public void up(){
      motor.setTargetPosition(1);
      motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      motor.setPower(0.5);
    }

    public void down() {
      motor.setTargetPosition(-1);
      motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      motor.setPower(0.5);
    }

    public void grab(){
      servo.setPosition(1.0);

    }

    public void stop() {
        motor.setPower(0);
    }

    public void letGo(){

        servo.setPosition(0.0);
    }
}
