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

	public static final String TAG = "Vuforia Navigation";

	OpenGLMatrix lastLocation = null;

	VuforiaLocalizer vuforia;

	// for capturing frame to file method
	int captureCounter = 0;
	File captureDirectory = AppUtil.ROBOT_DATA_DIR;

	// webcam
	WebcamName webcamName;

	// variables we'll use later for the webcam
	private boolean targetVisible = false;
	private float phoneXRotate = 0;
	private float phoneYRotate = 0;
	private float phoneZRotate = 0;

	// mm bc they're the recommended units and it's easier to use mm for calculations
	private float mmPerInch = 25.4f;
	private float mmBotWidth = (float) 17.7 * mmPerInch; // change the 17.7 to whatever the robot width is
	private float mmTargetHeight = (6) * mmPerInch; // the height of the center of the target image above the floor

	// Constants for perimeter targets
	private float halfField = 72 * mmPerInch;
	private float quadField = 36 * mmPerInch;
	
	List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();

    public Vuforia(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
        // initialize pure vuforia things
        initializeVuforia();
        setPhoneLocation();
        initializeTrackables();
	}

    /**
     * Initialize the vuforia localizer object
     */
    public void initializeVuforia() {
		// initialize camera and vuforia
		webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");

		int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
		VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

		parameters.vuforiaLicenseKey = "AVnPa5X/////AAABmUhfO30V7UvEiFRLKEAy25cwZ/uQDK2M0Z8GllUIhUOhFey2tkv1iKXqY4JdAjTHq4vlEUqn4F9sgeh+1ZiBsoPbGnSCdRnnHyQKmIU1hRoCyh24OvMfaG+6JQnpWlHorMoGWAqcEGt1+GXI9x3v2GLwooT1Dv/biDVn2DKar6tKms7EEEwIWkMN5YVaiQo53rbSSajpWuEROYYIrUrgzmgyorf4ngUWmjPrWHPES0OkUW6YVrZXoGT3Rwkiyl0Y7j5Rc5qT7iFBmI4v6E9udfPpnIsYrGzlhcL7GqHBntY8TuMYMTNIcklCO+ATWT4guojTwEOaNK+bVHG3XXxJsodhBK+Tbf7QX262rIbWvQto";
		parameters.cameraName = webcamName;
		parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

		parameters.useExtendedTracking = false;

		vuforia = ClassFactory.getInstance().createVuforia(parameters);
		vuforia.enableConvertFrameToBitmap();

		// for capturing frame to file method
		AppUtil.getInstance().ensureDirectoryExists(captureDirectory);

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
		// loading the trackables from xml file, replace with name of xml file in the current year
		VuforiaTrackables UltimateGoal = this.vuforia.loadTrackablesFromAsset("UltimateGoal");

		// these are the pictures that vuforia tracks to determine the robot location; change them out according to the current year's pictures

		// Template
		// VuforiaTrackable imageUse = xmlFileName.get(image index out of all images in xml file)
		VuforiaTrackable blueTowerGoal = UltimateGoal.get(0);
		VuforiaTrackable redTowerGoal = UltimateGoal.get(1);
		VuforiaTrackable redWallTarget = UltimateGoal.get(2); 
		VuforiaTrackable blueWallTarget = UltimateGoal.get(3);
		VuforiaTrackable frontWall = UltimateGoal.get(4);

		// set names
		blueTowerGoal.setName("BlueTowerGoal");
		redTowerGoal.setName("RedTowerGoal");
		redWallTarget.setName("RedWallTarget");
		blueWallTarget.setName("BlueWallTarget");
		frontWall.setName("FrontWall");

		// adds all of the previously declared pictures, we can also do it manually for each picture
		allTrackables.addAll(UltimateGoal);


		/**
		 * now we have to position the images
		 * they're initially at the center of the field facing up
		 * we need to move them according to their actual positions
		 * need to rotate x/y/z and actually postiion the location
		 * x points to right, y points to top of image, z points out of image towards the other side of the field
		 */

		redAllianceTarget.setLocation(OpenGLMatrix
				.translation(0, -halfField, mmTargetHeight)
				.multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180)));

		blueAllianceTarget.setLocation(OpenGLMatrix
				.translation(0, halfField, mmTargetHeight)
				.multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0)));
		frontWallTarget.setLocation(OpenGLMatrix
				.translation(-halfField, 0, mmTargetHeight)
				.multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0 , 90)));

		// The tower goal targets are located a quarter field length from the ends of the back perimeter wall.
		blueTowerGoalTarget.setLocation(OpenGLMatrix
				.translation(halfField, quadField, mmTargetHeight)
				.multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0 , -90)));
		redTowerGoalTarget.setLocation(OpenGLMatrix
				.translation(halfField, -quadField, mmTargetHeight)
				.multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));
    }

	public void setPhoneLocation() {
		// rotate camera so that it faces forward
		phoneYRotate = -90;

		// transformation matrix that describes where camera is on robot
		// initially the webcam is assumed to face up and the top points to the left of the robot

		// the top of the webcam is just the highest part when it's mounted correctly

		// measure these out and change them to be applicable to the current robot
		final float CAMERA_FORWARD_DISPLACEMENT  = 200.0f;   // eg: Camera is ___ mm in front of robot-center
		final float CAMERA_VERTICAL_DISPLACEMENT = 160.0f;   // eg: Camera is ___ mm above ground
		final float CAMERA_LEFT_DISPLACEMENT = 155.0f;     // eg: Camera is __ mm to the right of the robot's center line

		OpenGLMatrix robotFromCamera = OpenGLMatrix
					.translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
					.multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES, phoneYRotate, phoneZRotate, phoneXRotate));

		// tell trackables (pictures) the camera position
		for (VuforiaTrackable trackable : allTrackables) {
			((VuforiaTrackableDefaultListener) trackable.getListener()).setPhoneInformation(robotFromCamera, parameters.cameraDirection);
		}
	}


    /** END INITIALIZATIION */

	/**
	 * A simple utility that extracts positioning information from a transformation matrix
	 * and formats it in a form palatable to a human being.
	 */
	String format(OpenGLMatrix transformationMatrix) {
		return transformationMatrix.formatAsTransform();
	}

	// location method (adapted from opmode vuforia)

	public OpenGLMatrix robotPosition() {
		for (VuforiaTrackable trackable : allTrackables) {
			/**
			 * getUpdatedRobotLocation() will return null if no new information is available since
			 * the last time that call was made, or if the trackable is not currently visible.
			 * getRobotLocation() will return null if the trackable is not currently visible.
			 */
			telemetry.addData(trackable.getName(), ((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible() ? "Visible" : "Not Visible");    //

			OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener)trackable.getListener()).getUpdatedRobotLocation();
			if (robotLocationTransform != null) {
				lastLocation = robotLocationTransform;
			}
		}
		/**
		 * Provide feedback as to where the robot was last located (if we know).
		 */
		if (lastLocation != null) {
			telemetry.addData("Pos", format(lastLocation));
		} else {
			telemetry.addData("Pos", "Unknown");
		}
		telemetry.update();
		return lastLocation;
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

		// method to convert what the webcam is seeing into a .png, 
	void captureFrameToFile() {
		vuforia.getFrameOnce(Continuation.create(ThreadPool.getDefault(), new Consumer<Frame>()
			{
			@Override public void accept(Frame frame)
				{
				Bitmap bitmap = vuforia.convertFrameToBitmap(frame);
				if (bitmap != null) {
					File file = new File(captureDirectory, String.format(Locale.getDefault(), "VuforiaFrame-%d.png", captureCounter++));
					try {
						FileOutputStream outputStream = new FileOutputStream(file);
						try {
							bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
						} finally {
							outputStream.close();
							telemetry.log().add("captured %s", file.getName());
						}
					} catch (IOException e) {
						RobotLog.ee(TAG, e, "exception in captureFrameToFile()");
					}
				}
			}
		}));
	}
}
