package org.firstinspires.ftc.teamcode.autonomous;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;


@Autonomous(name="BlueFarTestRight")
public class BlueFarTestRight extends LinearOpMode {

    @Override
    public void runOpMode() {
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(-36, 62, Math.toRadians(270)));
        Pose2d startPose = new Pose2d(-36,62,Math.toRadians(270));
        Action mainTrajRight = drive.actionBuilder(startPose)
                .setReversed(true)
                .strafeTo(new Vector2d(-36, 34))
                .splineTo(new Vector2d(-39, 32), Math.toRadians(180)) //-38

                .waitSeconds(0.5)
                .strafeTo(new Vector2d(-35, 32))
                .setReversed(true)
                .setTangent(0)


                .splineToConstantHeading(new Vector2d(-38, 10), Math.toRadians(0))
                .splineTo(new Vector2d(20, 10), Math.toRadians(0))
                .splineToSplineHeading(new Pose2d(57,26, Math.toRadians(180)), Math.toRadians(0)) //34.5
//                .endTrajectory()
                .build();

        while (!isStopRequested() && !opModeIsActive()) {
        }




        waitForStart();

        if (isStopRequested()) return;
        Actions.runBlocking(
                new SequentialAction(
                        mainTrajRight


                )
        );

    }

}
