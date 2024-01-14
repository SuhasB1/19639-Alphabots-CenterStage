package com.example.meepmeeptesting;

import static java.lang.Math.toRadians;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import jdk.tools.jlink.internal.plugins.VendorVersionPlugin;

public class BlueFarCycles {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(50, 175, Math.PI, Math.PI, 13.9)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-36, 62, Math.toRadians(270)))
                                .setReversed(true)
                                // GOTO GROUND PIXEL
                                .splineToConstantHeading(new Vector2d(-42,47), Math.toRadians(-90))
                                .splineToSplineHeading(new Pose2d(-33,32, Math.toRadians(0)), Math.toRadians(-90))

                                // GOTO STACK
                                .setReversed(true)
                                .splineToSplineHeading(new Pose2d(-62,23.5,Math.toRadians(180)), Math.toRadians(180))
                                .waitSeconds(0.5)
                                .turn(Math.toRadians(-45))
                                .turn(Math.toRadians(90))
                                .turn(Math.toRadians(-45))





                                // GOTO BACKBOARD
                                .setReversed(true)
                                .setTangent(0)
                                .splineToConstantHeading(new Vector2d(-36, 12), Math.toRadians(0))
                                .splineTo(new Vector2d(20, 12), Math.toRadians(0))
                                .splineToSplineHeading(new Pose2d(53,41, Math.toRadians(180)), Math.toRadians(0))

                                // PARK
                                .splineToLinearHeading(new Pose2d(54,14, Math.toRadians(180)), Math.toRadians(0))
                                .build()



                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
