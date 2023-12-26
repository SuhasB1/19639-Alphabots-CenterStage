package org.firstinspires.ftc.teamcode.roadrunner;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Trajectory;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;



import org.firstinspires.ftc.teamcode.vision.TeamPropDetection;
import org.firstinspires.ftc.teamcode.vision.TeamPropDetectionClass;
import org.firstinspires.ftc.vision.VisionPortal;
import org.jetbrains.annotations.NotNull;
public final class ActionsTest extends LinearOpMode {


    @Override
    public void runOpMode() throws InterruptedException {
//        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
        IntakeSystem Intake = new IntakeSystem(hardwareMap);

        waitForStart();

        Actions.runBlocking(Intake.intake());


    }


    public class IntakeSystem {
        private DcMotorEx motor;

        public IntakeSystem(HardwareMap hardwareMap) {
            motor = hardwareMap.get(DcMotorEx.class, "intakeMotor");
        }

        public Action intake() {
            return new Action() {
                private boolean initialized = false;

                @Override
                public boolean run(@NonNull TelemetryPacket packet) {
                    if (!initialized) {
                        motor.setPower(0.8);
                        initialized = true;
                    }

                    double vel = motor.getVelocity();
                    packet.put("intake vel", vel);
                    return vel < 10_000.0;
                }
            };
        }
    }





}






