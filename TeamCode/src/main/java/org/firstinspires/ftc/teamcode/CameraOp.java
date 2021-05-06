package org.firstinspires.ftc.teamcode;

//import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import Merlin.BitmapRetriever;
import Merlin.MerlinPredict;


@TeleOp(name="Testing: Test opmode", group="Testing")
public class CameraOp extends OpMode {
    private MerlinPredict merl;
    private Vuforia vuforia;
    private BitmapRetriever bitmapRetriever;

    @Override
    public void init() {
        vuforia = new Vuforia(hardwareMap);
        merl = new MerlinPredict();
        bitmapRetriever = new BitmapRetriever(vuforia);
        try {
            merl.getPrediction(bitmapRetriever.getBitmap());
        } catch (Exception e) {
//            Log.e("via-e", e.toString());
        }

    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
    }

    @Override
    public void loop() {
    }
}
