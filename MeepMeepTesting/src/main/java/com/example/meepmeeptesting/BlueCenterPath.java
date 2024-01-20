package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;

import jdk.tools.jlink.internal.plugins.VendorVersionPlugin;

public class BlueCenterPath {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(50, 175, Math.PI, Math.PI, 13.9)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(-36, 62, Math.toRadians(270)))
//                        .splineToConstantHeading(new Vector2d(-36, 35), Math.toRadians(270))
//                        .build()

                        /* Blue Far Center */
                                .setReversed(true)
                                .strafeTo(new Vector2d(-36, 28.5))
                                .strafeTo(new Vector2d(-36, 38))
                                .splineToSplineHeading(new Pose2d(-36, 37, Math.toRadians(180)), Math.toRadians(270))
                                .strafeTo(new Vector2d(58,35))
                                .build()

                        /* Blue Far Right */
//

//                                .setReversed(true)
//                                .strafeTo(new Vector2d(-42 ,45))
//                                .splineToSplineHeading(new Pose2d(-38,32, Math.toRadians(180)), Math.toRadians(-90))
//                                .setReversed(true)
//                                .setTangent(0)
//                                .splineToConstantHeading(new Vector2d(-38, 10), Math.toRadians(0))
//                                .splineTo(new Vector2d(20, 10), Math.toRadians(0))
//                                .splineToSplineHeading(new Pose2d(56,35, Math.toRadians(180)), Math.toRadians(0))
//                                .build()


//                        .setReversed(true)
//                        .strafeTo(new Vector2d(-36, 45))
//                        .splineToSplineHeading(new Pose2d(-41,32, Math.toRadians(180)), Math.toRadians(-90))
//                        .setReversed(true)
//                        .setTangent(0)
//                        .splineToConstantHeading(new Vector2d(-38, 10), Math.toRadians(0))
//                        .splineTo(new Vector2d(20, 10), Math.toRadians(0))
//                        .splineToSplineHeading(new Pose2d(56,30, Math.toRadians(180)), Math.toRadians(0))
//
//
//                        .build()

                );
        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }

}
