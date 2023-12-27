package org.firstinspires.ftc.teamcode.vision;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.vision.TeamPropDetection;
import org.firstinspires.ftc.vision.VisionPortal;
@Autonomous()
public class TeamPropDetectionClass extends OpMode {
    private TeamPropDetection visionProcessor;
    private VisionPortal visionPortal;

    @Override
    public void init() {
        visionProcessor = new TeamPropDetection();
        visionPortal = VisionPortal.easyCreateWithDefaults(
                hardwareMap.get(WebcamName.class, "Webcam 1"), visionProcessor);
    }


    @Override
    public void start() {
        visionPortal.stopStreaming();
    }

    @Override
    public void loop() {
        telemetry.addData("Identified", visionProcessor.getSelection());
    }
}