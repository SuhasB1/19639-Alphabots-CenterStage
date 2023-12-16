package org.firstinspires.ftc.teamcode.actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;

public final class ActionsOpMode extends LinearOpMode {


    @Override
    public void runOpMode() throws InterruptedException {
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
        final IntakeClass Intake = new IntakeClass();

        waitForStart();


        Actions.runBlocking(
                new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .setTangent(0)
                                .splineToConstantHeading(new Vector2d(48, 0), Math.PI / 2)
                                .splineToConstantHeading(new Vector2d(-48, 0), Math.PI / 2)
                                .build()),
                new ParallelAction(
                        //put parallel action in here
                        Intake.intake()




                )
        );
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
                    packet.put("shooterVelocity", vel);
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
                    packet.put("shooterVelocity", vel);
                    return vel < 10_000.0;
                }
            };
        }
    }
    // end sample


}
