package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name = "DroneLauncher")
@Config
public class DroneLauncher extends LinearOpMode {

    Servo planeServo;

    public static double position = 1.0;


    @Override
    public void runOpMode() {
        planeServo = hardwareMap.get(Servo.class, "planeServo");



        telemetry.setMsTransmissionInterval(50);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());


        waitForStart();
        while(opModeIsActive() && !isStopRequested()) {

            planeServo.setPosition(position);

            telemetry.addData("Plane Servo Pos: " , planeServo.getPosition());
            telemetry.update();

        }
    }
}
