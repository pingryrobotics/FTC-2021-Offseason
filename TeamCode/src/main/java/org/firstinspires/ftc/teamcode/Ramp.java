package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Ramp {
    private CRServo ramp;
    public Ramp(HardwareMap hardwareMap){
        ramp = hardwareMap.get(CRServo.class, "ramp");
    }
    public void upRamp(){
        ramp.setPower(0.7);
    }
    public void downRamp(){
        ramp.setPower(-0.4);
    }
    public void stop(){
        ramp.setPower(0);
    }
}
