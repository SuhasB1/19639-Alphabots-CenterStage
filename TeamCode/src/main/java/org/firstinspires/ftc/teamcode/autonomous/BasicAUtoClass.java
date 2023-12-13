package org.firstinspires.ftc.teamcode.autonomous;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.vision.BluePropThreshold;
import org.firstinspires.ftc.vision.VisionPortal;

@Autonomous(name="basic fsm")
public class BasicAUtoClass extends LinearOpMode {
    private BluePropThreshold bluePropThreshold; //Create an object of the VisionProcessor Class
    Size resolution = new Size(640, 480);
    private VisionPortal portal;

    int propPosition = 0;

    public enum basicFSM{
        goToTape,
        outtakePurple,
        gotoBackdrop1,
        gotoBackdrop2,
        gotoBackdrop3,
        gotoBackdrop4,
        slidesExtend,
        gotoBackdrop5,
        dumperDown,
        gotoBackdrop6,
        gateRelease,
        dumperUp,
        gateClose,
        slidesRetract,
        goToParkingPos1,
        goToParkingPos2,
        finished
    }

    @Override
    public void runOpMode() throws InterruptedException {
        bluePropThreshold = new BluePropThreshold();
        portal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .setCameraResolution(resolution)
                .setCamera(BuiltinCameraDirection.BACK)
                .addProcessor(bluePropThreshold)
                .enableLiveView(true)
                .setAutoStopLiveView(true)
                .build();


        waitForStart();
        while (opModeIsActive()){
            telemetry.addData("position", bluePropThreshold.getPropPosition());
            telemetry.addData("left blue val", bluePropThreshold.getAveragedLeftBox());
            telemetry.addData("center blue val", bluePropThreshold.getAveragedMiddleBox());
            telemetry.update();
            sleep(50);
        }


    }}

