package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.Servo; //only if we need the additional feeder.
import com.qualcomm.robotcore.hardware.HardwareMap;


public class WobbleMech {
    private DcMotor motor;
    private Servo servo;
    //private Servo feeder;
    public WobbleMech(HardwareMap hardwareMap){
        motor = hardwareMap.get(DcMotor.class, "wobbleMotor");
        servo = hardwareMap.get(Servo.class, "wobbleServo");
        servo.scaleRange(0, .25);
    }

    public void up(){
      motor.setTargetPosition(1.25);
      motor.RUN_TO_POSITION();
    }

    public void down(){
      motor.setTargetPosition(0);
      motor.RUN_TO_POSITION();
    }

    public void grab(){
      servo.setPosition(1.0);

    }

    public void letGo(){
      servo.setPosition(0.0);
    }
}
