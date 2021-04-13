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
    }

    

    public void outtake(String goal){
        if(goal.equals("high")){
            flywheel.setPower(0.9);
        }
        else if(goal.equals("middle")){
            flywheel.setPower(0.6);
        }
        else{
            flywheel.setPower(0.3);
        }

        feeder.scaleRange(0, .25);
    }
    public void outtake(){
        flywheel.setPower(0.8);
    }
    public void outtake_in(){
        flywheel.setPower(0.2);
    }

    public void pushRing()
    {
      feeder.setPosition(1.0);
      feeder.setPosition(0.0);
    }

    public void stop(){
        flywheel.setPower(0);
    }

}
