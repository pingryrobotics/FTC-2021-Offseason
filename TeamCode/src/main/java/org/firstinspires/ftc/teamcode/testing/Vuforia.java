package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.qualcomm.robotcore.hardware.HardwareMap;


/**
 * Wrapper class for vuforia functionality
 * Adapted from here:
 * https://github.com/FIRST-Tech-Challenge/FtcRobotController/blob/master/FtcRobotController/src/main/java/org/firstinspires/ftc/robotcontroller/external/samples/ConceptVuforiaNavigation.java
 *  ^ comments in that file explain this a lot more in depth
 */
public class Vuforia {
    public static final String TAG = "Vuforia Localizer";

    private VuforiaLocalizer vuforiaLocalizer;
    private OpenGLMatrix lastLocation = null;
    private HardwareMap hardwareMap;
    private VuforiaLocalizer.CameraDirection cameraDirection;

    private OpenGLMatrix phoneLocationOnRobot;

    private Hashtable<TrackableItem, VuforiaTrackable> trackablesList;

    // constants for robot/field measurements
    private static final float mmPerInch        = 25.4f; // constant for mm to inches
    private static final float inchesBotWidth = 18; // width of robot in inches. SET MANUALLY
    private static final float mmBotWidth       = inchesBotWidth * mmPerInch; // width of robot to mm

    private static final float mmFTCFieldWidth  = (12*12 - 2) * mmPerInch; // width of field in mm. SET MANUALLY

    /** BEGIN INITIALIZATIION */

    /**
     * Initialize the vuforia localizer, trackable locations, and everything else
     * @param hardwareMap the hardware map
     */
    public Vuforia(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
        cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        // initialize pure vuforia things
        initializeVuforia();
        setPhoneLocation();
        initializeTrackables();

    }

    /**
     * Initialize the vuforia localizer object
     */
    public void initializeVuforia() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "cameraMonitorViewId",
                "id", hardwareMap.appContext.getPackageName());

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = "AVnPa5X/////AAABmUhfO30V7UvEiFRLKEAy25cwZ/uQDK2M0Z8GllUIhUOhFey2tkv1iKXqY4JdAjTHq4vlEUqn4F9sgeh+1ZiBsoPbGnSCdRnnHyQKmIU1hRoCyh24OvMfaG+6JQnpWlHorMoGWAqcEGt1+GXI9x3v2GLwooT1Dv/biDVn2DKar6tKms7EEEwIWkMN5YVaiQo53rbSSajpWuEROYYIrUrgzmgyorf4ngUWmjPrWHPES0OkUW6YVrZXoGT3Rwkiyl0Y7j5Rc5qT7iFBmI4v6E9udfPpnIsYrGzlhcL7GqHBntY8TuMYMTNIcklCO+ATWT4guojTwEOaNK+bVHG3XXxJsodhBK+Tbf7QX262rIbWvQto";

        // set camera direction
        parameters.cameraDirection = cameraDirection;
        // instantiate the vuforia localizer
        vuforiaLocalizer = ClassFactory.getInstance().createVuforia(parameters);
    }

    /**
     * Initialize the trackable objects
     * Custom datasets can be added here:
     * https://developer.vuforia.com/target-manager
     * See the comments in the source github (linked above this class) for more info
     *
     * Currently, this is just using the defaults from the source
     */
    public void initializeTrackables() {
        trackablesList = new Hashtable<TrackableItem, VuforiaTrackable>();

        // load the StonesAndChips dataset
        // custom datasets also use this, probably, but you load a different thing obviously
        VuforiaTrackables stonesAndChips = vuforiaLocalizer.loadTrackablesFromAsset("StonesAndChips");

        /**
         * RED TARGET
         * Position:
         * Image: stones
         */
        // assign a name to each trackable in the dataset, add to the list, & set location
        VuforiaTrackable redTarget = stonesAndChips.get(0);
        trackablesList.put(TrackableItem.RED_TARGET, redTarget);
        redTarget.setName("RedTarget");  // Stones

        // set red location on field
        // this is wrong btw, we need to set manually
        // look at the github
        OpenGLMatrix redTargetLocationOnField = OpenGLMatrix
                .translation(-mmFTCFieldWidth/2, 0, 0)
                .multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.XZX,
                        AngleUnit.DEGREES, 90, 90, 0));
        redTarget.setLocation(redTargetLocationOnField);
        RobotLog.ii(TAG, "Red Target=%s", format(redTargetLocationOnField));

        // let red target listener know where phone is
        ((VuforiaTrackableDefaultListener)redTarget.getListener()).setPhoneInformation(phoneLocationOnRobot, cameraDirection);

        /**
         * BLUE TARGET
         * Position:
         * Image: chips
         */
        // set blue target
        VuforiaTrackable blueTarget  = stonesAndChips.get(1);
        trackablesList.put(TrackableItem.BLUE_TARGET, blueTarget);

        blueTarget.setName("BlueTarget");  // Chips

        // also wrong, we gotta set it manually
        OpenGLMatrix blueTargetLocationOnField = OpenGLMatrix
                /* Then we translate the target off to the Blue Audience wall.
                Our translation here is a positive translation in Y.*/
                .translation(0, mmFTCFieldWidth/2, 0)
                .multiplied(Orientation.getRotationMatrix(
                        /* First, in the fixed (field) coordinate system, we rotate 90deg in X */
                        AxesReference.EXTRINSIC, AxesOrder.XZX,
                        AngleUnit.DEGREES, 90, 0, 0));
        blueTarget.setLocation(blueTargetLocationOnField);
        RobotLog.ii(TAG, "Blue Target=%s", format(blueTargetLocationOnField));

        // let blue target listener know where phone is
        ((VuforiaTrackableDefaultListener)blueTarget.getListener()).setPhoneInformation(phoneLocationOnRobot, cameraDirection);
    }

    /**
     * Sets the position of the phone on the robot
     * SEE THE GITHUB FOR HOW TO DO THIS, ITS REALLY DETAILED
     * GO READ IT.
     * https://github.com/FIRST-Tech-Challenge/FtcRobotController/blob/master/FtcRobotController/src/main/java/org/firstinspires/ftc/robotcontroller/external/samples/ConceptVuforiaNavigation.java
     */
    public void setPhoneLocation() {
        phoneLocationOnRobot = OpenGLMatrix
                .translation(mmBotWidth/2,0,0)
                .multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.YZY,
                        AngleUnit.DEGREES, -90, 0, 0));

        RobotLog.ii(TAG, "phone=%s", format(phoneLocationOnRobot));
    }

    /** END INITIALIZATIION */


    /**
     * Extracts positioning info from a transformation matrix and returns it as a readable string
     * Taken from the github
     * @param transformationMatrix The matrix to get info from
     * @return the information as a readable string
     */
    public static String format(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }

    // get location
    // check if target is visible
    // maybe make enum for targets to make this easier

    // enum of targets to make everything easier
    // add new trackables to here first then add the trackable object to the hashtable
    public enum TrackableItem {
        RED_TARGET,
        BLUE_TARGET
    }

    /**
     * Determines whether a given trackable is visible
     * @param item the trackable to check
     * @return whether the trackable is visible
     */
    public boolean isTrackableVisible(TrackableItem item) {
        VuforiaTrackable trackable = trackablesList.get(item);
        return ((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible();
    }

    /**
     * Gets the position of the robot if there's a new position
     * @return the updated robot position
     */
    public OpenGLMatrix getUpdatedRobotPosition() {
        for (TrackableItem item : TrackableItem.values()) {
            VuforiaTrackable trackable = trackablesList.get(item);
            OpenGLMatrix currentPosition = ((VuforiaTrackableDefaultListener) trackable.getListener()).getUpdatedRobotLocation();
            if (currentPosition != null && !currentPosition.equals(lastLocation)) {
                lastLocation = currentPosition;
            }
        }
        return lastLocation;
    }




}
