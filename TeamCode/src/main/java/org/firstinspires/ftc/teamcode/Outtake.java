package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo; //only if we need the additional feeder.
import com.qualcomm.robotcore.hardware.HardwareMap;


public class Outtake {
    private DcMotor flywheel;
    private Servo feeder;
    private double targetPosition;
    private boolean isMoving;
    //private Servo feeder;
    public Outtake(HardwareMap hardwareMap) {
        flywheel = hardwareMap.get(DcMotor.class, "flywheel");
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        feeder = hardwareMap.get(Servo.class, "feeder"); //We might use this as a way for us to add an additional feed into the flywheel
        feeder.scaleRange(0, .5);
        feeder.setDirection(Servo.Direction.FORWARD);
        targetPosition = 0;
    }

    public void outtake(String goal) {
        if(goal.equals("high")){
            flywheel.setPower(0.9);
        }
        else if(goal.equals("middle")) {
            flywheel.setPower(0.6);
        }
        else{
            flywheel.setPower(0.3);
        }
    }

    public void outtake() {
        flywheel.setPower(0.8);
    }

    public void outtake_in() {
        flywheel.setPower(-0.2);
    }

    public void pushRing() {
      feeder.setPosition(1.0);
      isMoving = true;
    }

    public void retract() {
        feeder.setPosition(0);
        isMoving = true;
    }

    public boolean isPositionReached() {
        if (feeder.getPosition() == targetPosition && isMoving) {
            isMoving = false;
            return true;
        }
        return false;
    }


    public void shootAndPushRingsStop() {
      pushRing();
      stop();
    }

    public void stop(){
        flywheel.setPower(0);
    }
}
