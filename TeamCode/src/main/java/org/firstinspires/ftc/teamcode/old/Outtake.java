package org.firstinspires.ftc.teamcode.old;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo; //only if we need the additional feeder.
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoController;


public class Outtake {
    private DcMotor flywheel;
    private Servo feeder;
    private double targetPosition;
    private boolean isMoving;
    private ServoController controller;
    private int port;
    private double start;
    private double target;
    //private Servo feeder;
    public Outtake(HardwareMap hardwareMap){
        flywheel = hardwareMap.get(DcMotor.class, "flywheel");
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        feeder = hardwareMap.get(Servo.class, "feeder"); //We might use this as a way for us to add an additional feed into the flywheel
//        feeder.scaleRange(0.4,0.75);
//        feeder.setDirection(Servo.Direction.REVERSE);
        targetPosition = 0;
        controller = feeder.getController();
        port = feeder.getPortNumber();
        start = 0.63;
        target = 0.5;
        isMoving = false;
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
    }

    public void outtake(){
        flywheel.setPower(0.9);
    }

    public void outtake_in(){
        flywheel.setPower(0.2);
    }

    public void pushRing()
    {
        System.out.println("push " + controller.getServoPosition(port));
        isMoving = true;
        feeder.setPosition(target);
        targetPosition = target;
//        feeder.setDirection(Servo.Direction.REVERSE);
//        System.out.println("push " + controller.getServoPosition(port));
//      controller.setServoPosition(port,0);
//      targetPosition = 0;

//      feeder.setPosition(0.0);
    }

    public void retract() {
        System.out.println("retract " + controller.getServoPosition(port));
        feeder.setPosition(start);
//        targetPosition = max;
//        feeder.setDirection(Servo.Direction.FORWARD);
//        System.out.println("retract " + controller.getServoPosition(port));
//        controller.setServoPosition(port, 0);
//        feeder.setPosition(0);
//        System.out.println("target: " + feeder.getPosition());
//        feeder.setPosition(0);
//        targetPosition = 1;
//        isMoving = true;
    }

//    public boolean isPositionReached() {
////        System.out.println("port" + feeder.getPortNumber());
//        System.out.println("controller pos: " + controller.getServoPosition(port));
////        System.out.println("pos " + controller.getServoPosition(port));
////        System.out.println("condition: " + (controller.getServoPosition(port) >= target - 0.05));
//        if ((controller.getServoPosition(port) >= target - 0.05) && isMoving) {
//            System.out.println("true");
//            isMoving = false;
//            return true;
//        } else if (isMoving) {
//            System.out.println("not there");
//        }
////        else {
////            System.out.println("false");
////        }
////        if (controller.getServoPosition(3) <= targetPosition && isMoving) {
////            System.out.println("retract " + controller.getServoPosition(port));
////////            targetPosition = 1;
////            isMoving = false;
////            return true;
////        }
//        return false;
//    }


    public void shootAndPushRingsStop()
    {
      pushRing();
      stop();
    }

    public void stop(){
        flywheel.setPower(0);
    }
}
