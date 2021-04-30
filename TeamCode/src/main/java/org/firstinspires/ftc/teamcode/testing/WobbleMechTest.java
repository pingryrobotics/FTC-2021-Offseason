package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo; //only if we need the additional feeder.
import com.qualcomm.robotcore.hardware.HardwareMap;


public class WobbleMech {
    private DcMotor wobbleMotor;
    private Servo servo;
    private double upPosition;
    private boolean up = true;
    private double downPosition;
//    private int targetPosition;
//    private boolean isMoving;
    //private Servo feeder;
    public WobbleMechTest(HardwareMap hardwareMap) {
        wobbleMotor = hardwareMap.get(DcMotor.class, "wobbleMotor");
        wobbleMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //wobbleMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //wobbleMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //wobbleMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        servo = hardwareMap.get(Servo.class, "wobbleServo");
        servo.setDirection(Servo.Direction.FORWARD);
        servo.scaleRange(0, .4);
        upPosition = getPosition();
        downPosition = upPosition - 50;
    }
    

    public void setUp(boolean up){
        this.up = up;
    }

    public setPower(double power)
    {
        wobbleMotor.setPower(power);
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
        if(up)
        {
            return downPosition;
        }

        else 
        {
            return upPosition;
        }
    }

    public void stop() {
        wobbleMotor.setPower(0);
    }
}
