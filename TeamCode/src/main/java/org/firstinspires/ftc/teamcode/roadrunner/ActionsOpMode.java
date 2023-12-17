/*package org.firstinspires.ftc.teamcode.roadrunner;

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
import org.firstinspires.ftc.teamcode.roadrunner.tuning.TuningOpModes;


import org.firstinspires.ftc.teamcode.vision.TeamPropDetection;
import org.firstinspires.ftc.teamcode.vision.TeamPropDetectionClass;
import org.firstinspires.ftc.vision.VisionPortal;
import org.jetbrains.annotations.NotNull;
public final class ActionsOpMode extends LinearOpMode {


    @Override
    public void runOpMode() throws InterruptedException {
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
        final IntakeClass Intake = new IntakeClass();
        final TeamPropDetectionClass detect = new TeamPropDetectionClass();

        waitForStart();


        Actions.runBlocking(
                new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .setTangent(0)
                                .splineToConstantHeading(new Vector2d(48, 0), Math.PI / 2) // go to prop
                                .build()),
                                Intake.outtake(), //outtake pixel
                                detect.detection(), //get detection

                                //put parallel action in here
                new ParallelAction(
                        drive.actionBuilder(drive.pose)
                                .splineToConstantHeading(new Vector2d(48, 0), Math.PI / 2).build()// path to board
                                //raise linear slides






                ));
    }

    public class IntakeClass {
        private DcMotorEx intakeMotor;

        public IntakeClass() {
            intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");


        }

        public Action intake() {
            return new Action() {
                private boolean initialized = false;

                @Override
                public boolean run(@NonNull TelemetryPacket packet) {
                    if (!initialized) {
                        intakeMotor.setPower(0.8);
                        initialized = true;
                    }

                    double vel = intakeMotor.getVelocity();
                    packet.put("intake vel ", vel);
                    return vel < 10_000.0;
                }
            };
        }

        public Action outtake() {
            return new Action() {
                private boolean initialized = false;

                @Override
                public boolean run(@NonNull TelemetryPacket packet) {
                    if (!initialized) {
                        intakeMotor.setPower(-0.8);
                        initialized = true;
                    }

                    double vel = intakeMotor.getVelocity();
                    packet.put("outtake vel ", vel);
                    return vel < 10_000.0;
                }
            };
        }
    }
    // end sample


    public class LinearSlides {
        private DcMotorEx m1;
        private DcMotorEx m2;

        private double ticks;

        public LinearSlides(double ticks) {
            m1 = hardwareMap.get(DcMotorEx.class, "motor1");
            m2 = hardwareMap.get(DcMotorEx.class, "motor2");
            this.ticks = ticks;


        }

        public Action linearSlides() {
            return new Action() {
                private boolean initialized = false;

                @Override
                public boolean run(@NonNull TelemetryPacket packet) {
                    if (!initialized) {
                        m1.setPower(0);
                        m2.setPower(0);
                        initialized = true;
                    }

                    double vel = m1.getVelocity();
                    packet.put("linear slides motor vel ", vel);
                    return vel < 10_000.0;
                }
            };
        }


    }

    public class TeamPropDetectionClass extends OpMode {
        private TeamPropDetection visionProcessor;
        private VisionPortal visionPortal;

        public TeamPropDetectionClass() {
            visionProcessor = new TeamPropDetection();
            visionPortal = VisionPortal.easyCreateWithDefaults(
                    hardwareMap.get(WebcamName.class, "Webcam 1"), visionProcessor);
        }


        public Action detection() {
            return new Action() {
                private boolean initialized = false;

                @Override
                public boolean run(@NonNull TelemetryPacket packet) {
                    if (!initialized) {
                        visionPortal.stopStreaming();
                        initialized = true;
                    }

                    packet.put("selection ", visionProcessor.getSelection() );
                    return true;
                }
            };
        }
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





}*/






