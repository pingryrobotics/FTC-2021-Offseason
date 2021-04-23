package org.firstinspires.ftc.teamcode.old;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class Intake {
    private DcMotor intakeMotor;
    private DcMotor secondIntakeMotor;
    private double power;
    public Intake(HardwareMap hardwareMap){
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeLower");
        intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        secondIntakeMotor = hardwareMap.get(DcMotor.class, "intakeUpper");
        secondIntakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        power = 1.25;
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
        intakeMotor.setPower(power * -1);
    }

    public void intake_in_second(){
        secondIntakeMotor.setPower(power);
    }

    public void intake_out_first(){
        intakeMotor.setPower(power);
    }

    public void intake_out_second(){
        secondIntakeMotor.setPower(power * -1);
    }

    public void stop(){
        intakeMotor.setPower(0);
        secondIntakeMotor.setPower(0);
    }
}
