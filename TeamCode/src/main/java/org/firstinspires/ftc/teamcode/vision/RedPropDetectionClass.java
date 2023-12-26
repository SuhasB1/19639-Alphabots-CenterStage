package org.firstinspires.ftc.teamcode.vision;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;

@Autonomous(name="Vision Test - Red")
public class RedPropDetectionClass extends LinearOpMode {
    private RedPropThreshold redPropThreshold; //Create an object of the VisionProcessor Class
    android.util.Size resolution = new Size(640, 480);
    private VisionPortal portal;

    @Override
    public void runOpMode() throws InterruptedException {
        redPropThreshold = new RedPropThreshold();
        portal = VisionPortal.easyCreateWithDefaults(
                hardwareMap.get(WebcamName.class, "Webcam 1"), redPropThreshold);


        waitForStart();
        while (opModeIsActive()){
            telemetry.addData("position", redPropThreshold.getPropPosition());
            telemetry.addData("left red val", redPropThreshold.getAveragedLeftBox());
            telemetry.addData("center red val", redPropThreshold.getAveragedMiddleBox());
            telemetry.update();
            sleep(50);
        }
    }}

