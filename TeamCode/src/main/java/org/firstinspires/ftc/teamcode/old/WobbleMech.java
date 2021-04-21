package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo; //only if we need the additional feeder.
import com.qualcomm.robotcore.hardware.HardwareMap;


public class WobbleMech {
    private DcMotor wobbleMotor;
    private Servo servo;
    private int position;
//    private int targetPosition;
//    private boolean isMoving;
    //private Servo feeder;
    public WobbleMech(HardwareMap hardwareMap) {
        wobbleMotor = hardwareMap.get(DcMotor.class, "wobbleMotor");
        wobbleMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //wobbleMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //wobbleMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //wobbleMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        servo = hardwareMap.get(Servo.class, "wobbleServo");
        servo.setDirection(Servo.Direction.FORWARD);
        servo.scaleRange(0, .4);
        position = getPosition();
    }

    public void up(){
        //wobbleMotor.setTargetPosition(1);
        //974
        //wobbleMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        wobbleMotor.setPower(-0.75);
    }

    public void down() {
        //wobbleMotor.setTargetPosition(-1);
        //974
        //wobbleMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        wobbleMotor.setPower(0.75);
//      System.out.println("running");
    }

    public void grab(){
      servo.setPosition(1.0);
    }

    public int getPosition(){return wobbleMotor.getCurrentPosition();}

    public void letGo(){
        servo.setPosition(0.0);
    }
//    public void checkPosition() {
//        if (isMoving && getPosition() >= targetPosition) {
//            isMoving = false;
//
//        }
//    }

    public String getMode() {
        String mode = wobbleMotor.getMode() + "";
        return mode;
    }

    public int getTargetPosition() {
        return wobbleMotor.getTargetPosition();
    }

    public void stop() {
        wobbleMotor.setPower(0);
    }
}
