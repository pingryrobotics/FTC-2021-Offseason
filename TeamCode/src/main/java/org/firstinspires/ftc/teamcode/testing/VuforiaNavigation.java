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

		/**
         * We use units of mm here because that's the recommended units of measurement for the
         * size values specified in the XML for the ImageTarget trackables in data sets. E.g.:
         *      <ImageTarget name="stones" size="247 173"/>
         * You don't *have to* use mm here, but the units here and the units used in the XML
         * target configuration files *must* correspond for the math to work out correctly.
         */
        float mmPerInch        = 25.4f;
        float mmBotWidth       = 17.7 * mmPerInch;            // ... or whatever is right for your robot
        float mmFTCFieldWidth  = (12*12 - 2) * mmPerInch;   // the FTC field is ~11'10" center-to-center of the glass panels
	}
}