package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Big Blue Robotics on 10/4/2017.
 */

public class MecanumDrive {
    public DcMotor leftFront = null;
    public DcMotor leftRear = null;
    public DcMotor rightFront = null;
    public DcMotor rightRear = null;
    public double strafeAdjustment = 0.2;

    public MecanumDrive(HardwareMap hardwareMap){
        leftFront = hardwareMap.get(DcMotor.class, "left_front");
        leftRear = hardwareMap.get(DcMotor.class, "left_rear");
        rightFront = hardwareMap.get(DcMotor.class, "right_front");
        rightRear = hardwareMap.get(DcMotor.class, "right_rear");

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftRear.setDirection(DcMotor.Direction.REVERSE);
    }

    public void move(double leftFrontPower, double rightFrontPower, double leftRearPower, double rightRearPower){
        leftFront.setPower(leftFrontPower);
        rightFront.setPower(rightFrontPower);
        leftRear.setPower(leftRearPower);
        rightRear.setPower(rightRearPower);
    }

    public void moveEncoderStrafeRight(double inches, double power){
        power = Math.abs(power);
        leftFront.setTargetPosition((int) (leftFront.getCurrentPosition() + (inches * 140 / Math.PI))); // should be divided by 4 times pi times 20 times 7
        rightFront.setTargetPosition((int) (rightFront.getCurrentPosition() - (inches * 140 / Math.PI))); // should be divided by 4 times pi times 20 times 7
        leftRear.setTargetPosition((int) (leftRear.getCurrentPosition() - (inches * 140 / Math.PI))); // should be divided by 4 times pi times 20 times 7
        rightRear.setTargetPosition((int) (rightRear.getCurrentPosition() + (inches * 140 / Math.PI))); // should be divided by 4 times pi times 20 times 7

        leftFront.setPower(power);
        rightFront.setPower(power);
        leftRear.setPower(power);
        rightRear.setPower(power);
    }

    public void moveEncoderStraight(double inches, double power){
        power = Math.abs(power);
        leftFront.setTargetPosition((int) (leftFront.getCurrentPosition() + (inches * 140 / Math.PI))); // should be divided by 4 times pi times 20 times 7
        rightFront.setTargetPosition((int) (rightFront.getCurrentPosition() + (inches * 140 / Math.PI))); // should be divided by 4 times pi times 20 times 7
        leftRear.setTargetPosition((int) (leftRear.getCurrentPosition() + (inches * 140 / Math.PI))); // should be divided by 4 times pi times 20 times 7
        rightRear.setTargetPosition((int) (rightRear.getCurrentPosition() + (inches * 140 / Math.PI))); // should be divided by 4 times pi times 20 times 7

        if(inches < 0){
            power *= -1;
        }
        leftFront.setPower(power);
        rightFront.setPower(power);
        leftRear.setPower(power);
        rightRear.setPower(power);
    }

    public void encoderTurn(double degrees, double power){
        boolean turnRight = degrees > 0;
        power = Math.abs(power);

        double inches = degrees/180 * Math.PI * 11.5;
        int leftFrontTarget = (int) (leftFront.getCurrentPosition() - (inches * 140 / Math.PI));
        int leftRearTarget = (int) (leftRear.getCurrentPosition() - (inches * 140 / Math.PI));
        int rightFrontTarget = (int) (rightFront.getCurrentPosition() + (inches * 140 / Math.PI));
        int rightRearTarget = (int) (rightRear.getCurrentPosition() + (inches * 140 / Math.PI));

        leftFront.setTargetPosition(leftFrontTarget);
        leftRear.setTargetPosition(leftRearTarget);
        rightFront.setTargetPosition(rightFrontTarget);
        rightRear.setTargetPosition(rightRearTarget);
        if(turnRight){
            leftFront.setPower(-power);
            leftRear.setPower(-power);
            rightFront.setPower(power);
            rightRear.setPower(power);
        }else{
            leftFront.setPower(power);
            leftRear.setPower(power);
            rightFront.setPower(-power);
            rightRear.setPower(-power);
        }
    }

    public boolean encoderDone(){
        return encoderLeftFrontDone() || encoderLeftRearDone() || encoderRightFrontDone() || encoderRightRearDone();
    }

    public boolean encoderLeftFrontDone(){
        if(leftFront.getPower() < 0){
            return leftFront.getCurrentPosition() < leftFront.getTargetPosition();
        }else {
            return leftFront.getCurrentPosition() > leftFront.getTargetPosition();
        }
    }

    public boolean encoderLeftRearDone(){
        if(leftRear.getPower() < 0){
            return leftRear.getCurrentPosition() < leftRear.getTargetPosition();
        }else {
            return leftRear.getCurrentPosition() < leftRear.getTargetPosition();
        }
    }

    public boolean encoderRightFrontDone(){
        if(rightFront.getPower() < 0){
            return rightFront.getCurrentPosition() < rightFront.getTargetPosition();
        }else {
            return rightFront.getCurrentPosition() > rightFront.getTargetPosition();
        }
    }

    public boolean encoderRightRearDone(){
        if(rightRear.getPower() < 0){
            return rightRear.getCurrentPosition() < rightRear.getTargetPosition();
        }else {
            return rightRear.getCurrentPosition() < rightRear.getTargetPosition();
        }
    }

    public void polarMove(double angle, double turn, double power){
        final double v1 = power * Math.cos(angle) + turn;
        final double v2 = power * Math.sin(angle) - turn;
        final double v3 = power * Math.sin(angle) + turn;
        final double v4 = power * Math.cos(angle) - turn;

        leftFront.setPower(-1 * v1);
        rightFront.setPower(-1 * v2);
        leftRear.setPower(-1 * v3);
        rightRear.setPower(-1 * v4);
    }
    public void brake(){
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftRear.setPower(0);
        rightRear.setPower(0);
    }
}