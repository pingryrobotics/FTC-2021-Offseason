package org.firstinspires.ftc.teamcode.testing;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.ArrayList;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import com.qualcomm.robotcore.hardware.HardwareMap;


// sourced from https://github.com/FIRST-Tech-Challenge/FtcRobotController/blob/master/FtcRobotController/src/main/java/org/firstinspires/ftc/robotcontroller/external/samples/ConceptTensorFlowObjectDetection.java

/**
 * Wrapper class for tensorflow object detection
 * Note: to get more information on Recognition objects, look at the class file (command b on
 * android studio, or just go to declaration). You can get the label,
 * confidence, estimated angle, coordinate in the image, and other things
 */
public class TfObjectDetector {
    // tensorflow model settings
    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Quad";
    private static final String LABEL_SECOND_ELEMENT = "Single";

    // tfod is the main tensorflow object detection object
    private TFObjectDetector tfod;
    private HardwareMap hardwareMap;
    private VuforiaLocalizer vuforia;

    /**
     * Initialize the tensorflow object detection object
     * @param hardwareMap The hardware map for the robot
     * @param vuforia The vuforia object that the tensorflow object uses for recognitions. This the
     *                actual vuforia object, not an instance of our class
     */
    public Tensorflow(HardwareMap hardwareMap, VuforiaLocalizer vuforia) {
        this.vuforia = vuforia;
        this.hardwareMap = hardwareMap;
    }

    /**
     * Gets an updated list of tf recognitions
     * @return the list of recognition objects
     */
    public List<Recognition> getRecognitionList() {
        List<Recognition> recognitionList = tfod.getUpdatedRecognitions();
        return recognitionList;
    }

    /**
     * Gets the number of recognitions. You could just call getRecognitionList then get the size,
     * but this is easier for some things. Depending on what tfod actually recognizes, this might get
     * changed to only recognize certain objects.
     * @return The total number of recognitions
     */
    public int getRecognitionCount() {
        List<Recognition> recognitionList = tfod.getUpdatedRecognitions();
        return recognitionList.size();
    }
    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }


}
