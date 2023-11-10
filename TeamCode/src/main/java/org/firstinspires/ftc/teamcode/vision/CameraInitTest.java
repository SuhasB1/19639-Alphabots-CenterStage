package org.firstinspires.ftc.teamcode.vision;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;

@Autonomous(name="Camera Init Test")
public class CameraInitTest extends LinearOpMode {
    //private RedPropThreshold redPropThreshold; //Create an object of the VisionProcessor Class
    Size resolution = new Size(640, 480);
    private VisionPortal portal;

    @Override
    public void runOpMode() throws InterruptedException {
        //redPropThreshold = new RedPropThreshold();
        portal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .setCameraResolution(resolution)
                .setCamera(BuiltinCameraDirection.BACK)
                .enableLiveView(true)
                .setAutoStopLiveView(true)
                //.addProcessor(redPropThreshold)
                .build();


        waitForStart();
        while (opModeIsActive()){
            sleep(50);
        }
    }}

