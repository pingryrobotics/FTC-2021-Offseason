package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;


@TeleOp(name="Concept: Vuforia Navigation", group ="Concept")
@Disabled
public class VuforiaNavigation extends LinearOpMode {

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

	@Override public void runOpMode() {
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
		blueTowerGoal.setTarget("BlueTowerGoal");
		redTowerGoal.setTarget("RedTowerGoal");
		redWallTarget.setTarget("RedWallTarget");
		blueWallTarget.setTarget("BlueWallTarget");
		frontWall.setTarget("FrontWall");	

		List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();

		// adds all of the previously declared pictures, we can also do it manually for each picture
		allTrackables.addAll(UltimateGoal);

		// mm bc they're the recommended units and it's easier to use mm for calculations
		float mmPerInch = 25.4f;
		float mmBotWidth = 17.7 * mmPerInch; // change the 17.7 to whatever the robot width is
		float mmTargetHeight = (6) * mmPerInch; // the height of the center of the target image above the floor

		// Constants for perimeter targets
		float halfField = 72 * mmPerInch;
		float quadField = 36 * mmPerInch;

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


		// rotate camera so that it faces forward
		phoneYRotate = -90;

		// transformation matrix that describes where camera is on robot
		// initially the webcam is assumed to face up and the top points to the left of the robot

		// the top of the webcam is just the highest part when it's mounted correctly

		// measure these out and change them to be applicable to the current robot
		final float CAMERA_FORWARD_DISPLACEMENT  = 4.0f * mmPerInch;   // eg: Camera is 4 Inches in front of robot-center
		final float CAMERA_VERTICAL_DISPLACEMENT = 8.0f * mmPerInch;   // eg: Camera is 8 Inches above ground
		final float CAMERA_LEFT_DISPLACEMENT = -3.0f * mmPerInch;     // eg: Camera is ON the robot's center line

		OpenGLMatrix robotFromCamera = OpenGLMatrix
					.translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
					.multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES, phoneYRotate, phoneZRotate, phoneXRotate));

		// tell trackables (pictures) the camera position
		for (VuforiaTrackable trackable : allTrackables) {
			((VuforiaTrackableDefaultListener) trackable.getListener()).setPhoneInformation(robotFromCamera, parameters.cameraDirection);
		}

		

		// DS camera preview feature; a bunch of stuff to tell driver station where the robot is

		// Note: To use the remote camera preview:
		// AFTER you hit Init on the Driver Station, use the "options menu" to select "Camera Stream"
		// Tap the preview window to receive a fresh image.

		// DON'T PUT ANY DRIVING COMMANDS IN THIS LOOP

		// targetsUltimateGoal.activate();
		// while (!isStopRequested()) {

		// 	// check all the trackable targets to see which one (if any) is visible.
		// 	targetVisible = false;
		// 	for (VuforiaTrackable trackable : allTrackables) {
		// 		if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {
		// 			telemetry.addData("Visible Target", trackable.getName());
		// 			targetVisible = true;

		// 			// getUpdatedRobotLocation() will return null if no new information is available since
		// 			// the last time that call was made, or if the trackable is not currently visible.
		// 			OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener)trackable.getListener()).getUpdatedRobotLocation();
		// 			if (robotLocationTransform != null) {
		// 				lastLocation = robotLocationTransform;
		// 			}
		// 			break;
		// 		}
		// 	}

		// 	// Provide feedback as to where the robot is located (if we know).
		// 	if (targetVisible) {
		// 		// express position (translation) of robot in inches.
		// 		VectorF translation = lastLocation.getTranslation();
		// 		telemetry.addData("Pos (in)", "{X, Y, Z} = %.1f, %.1f, %.1f",
		// 				translation.get(0) / mmPerInch, translation.get(1) / mmPerInch, translation.get(2) / mmPerInch);

		// 		// express the rotation of the robot in degrees.
		// 		Orientation rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
		// 		telemetry.addData("Rot (deg)", "{Roll, Pitch, Heading} = %.0f, %.0f, %.0f", rotation.firstAngle, rotation.secondAngle, rotation.thirdAngle);
		// 	}
		// 	else {
		// 		telemetry.addData("Visible Target", "none");
		// 	}
		// 	telemetry.update();
		// }

		// // Disable Tracking when we are done;
		// targetsUltimateGoal.deactivate();


		// normal opmode
		/**
		 * A brief tutorial: here's how all the math is going to work:
		 *
		 * C = robotFromCamera          maps   camera coords -> robot coords
		 * P = tracker.getPose()        maps   image target coords -> camera coords
		 * L = redTargetLocationOnField maps   image target coords -> field coords
		 *
		 * So
		 *
		 * C.inverted()                 maps   robot coords -> camera coords
		 * P.inverted()                 maps   camera coords -> imageTarget coords
		 *
		 * Putting that all together,
		 *
		 * L x P.inverted() x C.inverted() maps robot coords to field coords.
		 *
		 * @see VuforiaTrackableDefaultListener#getRobotLocation()
		 */

		/** Wait for the game to begin */
		telemetry.addData(">", "Press Play to start tracking");
		telemetry.update();
		waitForStart();

		// track images
		UltimateGoal.activate();

		boolean buttonPressed = false;
		while (opModeIsActive()) {

			if (gamepad1.b && !buttonPressed) {
				captureFrameToFile();
			}
			buttonPressed = gamepad1.b;

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
		}
	}

	/**
	 * A simple utility that extracts positioning information from a transformation matrix
	 * and formats it in a form palatable to a human being.
	 */
	String format(OpenGLMatrix transformationMatrix) {
		return transformationMatrix.formatAsTransform();
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