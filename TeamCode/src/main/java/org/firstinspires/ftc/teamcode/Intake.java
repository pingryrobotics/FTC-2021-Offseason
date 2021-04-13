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

    public void intake_in(){
        intakeMotor.setPower(0.8);
    }

    public void intake_out(){
        intakeMotor.setPower(-0.8);
    }

    public void stop(){
        intakeMotor.setPower(0);
    }
}
