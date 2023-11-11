package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Trajectory;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.TuningOpModes;
import org.firstinspires.ftc.teamcode.roadrunner.TuningOpModes;


public final class PathTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
            MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));

            waitForStart();


            Actions.runBlocking(
                    drive.actionBuilder(drive.pose)
                            .setTangent(0)
                            .splineToConstantHeading(new Vector2d(48, 0), Math.PI / 2)
                            .splineToConstantHeading(new Vector2d(-48, 0), Math.PI / 2)
                            .build());
    }
}
