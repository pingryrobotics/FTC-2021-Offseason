package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class Intake {
    private DcMotor intakeMotor;
    private DcMotor secondIntakeMotor;
    public Intake(HardwareMap hardwareMap){
        intakeMotor = hardwareMap.get(DcMotor.class, "intake");
        secondIntakeMotor = hardwareMap.get(DcMotor.class, "secondIntake");
    }

    public void intake_in_both(){
        intake_in_first();
        intake_in_second();
    }

    public void intake_out_both(){
        intake_out_first();
        intake_out_second();
    }

    public void intake_in_first(){
        intakeMotor.setPower(0.8);
    }

    public void intake_in_second(){
        secondIntakeMotor.setPower(0.8);
    }

    public void intake_out_first(){
        intakeMotor.setPower(-0.8);
    }

    public void intake_out_second(){
        secondIntakeMotor.setPower(-0.8);
    }

    public void stop(){
        intakeMotor.setPower(0);
        secondIntakeMotor.setPower(0);
    }
}
