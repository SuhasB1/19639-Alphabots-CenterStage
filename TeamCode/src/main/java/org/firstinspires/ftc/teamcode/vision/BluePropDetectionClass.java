package org.firstinspires.ftc.teamcode.vision;

import android.util.Size;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;

@Autonomous(name="Vision Test - blue")
public class BluePropDetectionClass extends LinearOpMode {
    private BluePropThreshold bluePropThreshold; //Create an object of the VisionProcessor Class
    android.util.Size resolution = new Size(640, 480);
    private VisionPortal portal;

    @Override
    public void runOpMode() throws InterruptedException {
        bluePropThreshold = new BluePropThreshold();
        portal = VisionPortal.easyCreateWithDefaults(
                hardwareMap.get(WebcamName.class, "Webcam 1"), bluePropThreshold);
        FtcDashboard.getInstance().startCameraStream(bluePropThreshold, 0);
        waitForStart();
        while (opModeIsActive()){
            telemetry.addData("position", bluePropThreshold.getPropPosition());
            telemetry.addData("right blue val", bluePropThreshold.getAveragedRightBox());
            telemetry.addData("center blue val", bluePropThreshold.getAveragedMiddleBox());
            telemetry.update();
            sleep(50);
        }
    }}

