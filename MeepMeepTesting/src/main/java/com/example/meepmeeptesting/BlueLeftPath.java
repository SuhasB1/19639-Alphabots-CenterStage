
package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;

import jdk.tools.jlink.internal.plugins.VendorVersionPlugin;

public class BlueLeftPath {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setColorScheme(new ColorSchemeBlueDark())
                .setConstraints(50, 175, Math.PI, Math.PI, 13.9)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-35.3, 59, Math.toRadians(270)))
                                .splineToLinearHeading(new Pose2d(-35.1, 31, Math.toRadians(0)), Math.toRadians(270))
                                .splineToConstantHeading(new Vector2d(-23.4, 7.4), Math.toRadians(0))
                                .splineToConstantHeading(new Vector2d(30, 10), Math.toRadians(0))
                                .splineToLinearHeading(new Pose2d(39, 35.8, Math.toRadians(180)), Math.toRadians(0))

                                .build()

                );

        RoadRunnerBotEntity my2Bot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setColorScheme(new ColorSchemeRedDark())
                .setConstraints(60, 175, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-33, -62.5, Math.toRadians(90)))
                                .splineToLinearHeading(new Pose2d(-46, -30, Math.toRadians(180)), Math.toRadians(180))
                                .splineToLinearHeading(new Pose2d(-39.3, -31, Math.toRadians(180)), Math.toRadians(0))
                                .splineToLinearHeading(new Pose2d(-32.8, -12, Math.toRadians(180)), Math.toRadians(0))
                                .splineToConstantHeading(new Vector2d(0, -11), Math.toRadians(0))
                                .splineToConstantHeading(new Vector2d(36.5, -16), Math.toRadians(0))
                                .splineToConstantHeading(new Vector2d(35.5, -24), Math.toRadians(270))
                                .build()

                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .addEntity(my2Bot)
                .start();
    }
}

