package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;

@Config
@TeleOp(name="digital sensor DOUBLE")
public class DigitalDistanceSensorTestDOUBLE extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        DigitalChannel leftDistanceSensor = hardwareMap.get(DigitalChannel.class, "leftDistanceSensor");
        DigitalChannel rightDistanceSensor = hardwareMap.get(DigitalChannel.class, "rightDistanceSensor");


        waitForStart();
        while (opModeIsActive()){
            boolean detectedLeft = leftDistanceSensor.getState();
            detectedLeft = !detectedLeft;
            boolean detectedRight = rightDistanceSensor.getState();
            detectedRight = !detectedRight;
            telemetry.addData("left detected? " , detectedLeft);
            telemetry.addData("right detected? " , detectedRight);
            telemetry.addLine("testing if tle is working");
            telemetry.update();
        }
    }


}




