package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;
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
import java.util.List;

@TeleOp(name="Concept: Vuforia Navigation", group ="Concept")
@Disabled
public class VuforiaNavigation extends LinearOpMode {
	OpenGLMatrix lastLocation = null;

	VuforiaLocalizer vuforia;

	@Override public void runOpMode() {
		// initialize camera and vuforia
		int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
		parameters.vuforiaLicenseKey = "";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
	
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
		float mmFTCFieldWidth = (12*12 - 2) * mmPerInch; // the FTC field is ~11'10" center-to-center of the glass panels
	}
}