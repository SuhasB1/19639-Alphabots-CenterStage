package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;


@TeleOp(name = "SetServo")
@Config
public class SetServo extends LinearOpMode {
    public static double dumpInitPosition = 0.17;
    public static double dumpPosition = 0.64;

    Servo dumpServo1;
    Servo dumpServo2;

    final Gamepad currentGamepad1 = new Gamepad();
    final Gamepad previousGamepad1 = new Gamepad();

    @Override
    public void runOpMode() {
        dumpServo1 = hardwareMap.get(Servo.class, "dumpServoLeft");
        dumpServo2 = hardwareMap.get(Servo.class, "dumpServoRight");


        dumpServo1.setDirection(Servo.Direction.REVERSE);

        telemetry.setMsTransmissionInterval(50);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        waitForStart();
        while(opModeIsActive() && !isStopRequested()) {


//            dumpServo1.setPosition(dumpInitPosition);
//            dumpServo2.setPosition(dumpInitPosition);
//
//
//            telemetry.addData("dump servo 1" , dumpServo1.getPosition());
//            telemetry.addData("dump servo 2" , dumpServo2.getPosition());
//            telemetry.update();
//
//            sleep(1000);

            dumpServo1.setPosition(dumpPosition);
            dumpServo2.setPosition(dumpPosition);

            telemetry.addData("dump servo 1" , dumpServo1.getPosition());
            telemetry.addData("dump servo 2" , dumpServo2.getPosition());
            telemetry.update();


        }
    }
}
