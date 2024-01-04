package org.firstinspires.ftc.teamcode.autonomous;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.vision.BluePropThreshold;
import org.firstinspires.ftc.vision.VisionPortal;

import java.util.List;
import java.util.Vector;

@Autonomous(name="BlueFar")
@Config
public class BlueFar extends LinearOpMode {


    public static double Lp = 0.015, Li = 0, Ld = 0.0003;
    public static double Rp = 0.015, Ri = 0, Rd = 0.0003;
    public static double dumpInitPosition = 0.15;

    public static double dumpOnePosition = 0.535;

    public static double dumpTwoPosition = dumpInitPosition;


    public static String cameraPos = "MIDDLE";
    public static double f = 0.1;
    public int left_slides_pos;
    public int right_slides_pos;
    private PIDController controllerRight;
    private PIDController controllerLeft;
    private BluePropThreshold bluePropThreshold; //Create an object of the VisionProcessor Class
    private VisionPortal portal;

    public static double targetPos = 0;

    @Override

    public void runOpMode() throws IllegalArgumentException {
        targetPos = 0;


        // Telemetry Init
        telemetry.setMsTransmissionInterval(50);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        controllerLeft = new PIDController(Lp, Li, Ld);
        controllerRight = new PIDController(Rp, Ri, Rd);
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(-36, 62, Math.toRadians(270)));
        bluePropThreshold = new BluePropThreshold();
        portal = VisionPortal.easyCreateWithDefaults(
                hardwareMap.get(WebcamName.class, "Webcam 1"), bluePropThreshold);
        FtcDashboard.getInstance().startCameraStream(bluePropThreshold, 0);
        IntakeSystem intake = new IntakeSystem(hardwareMap);

        DropperSystem drop = new DropperSystem(hardwareMap);

        Pose2d startPose = new Pose2d(-36,62,Math.toRadians(270));
        // Delcare Trajectory as such
        Action mainTrajLeft = drive.actionBuilder(startPose)
                .setReversed(true)
                .lineToY(45)
                .splineToSplineHeading(new Pose2d(-38,32, Math.toRadians(0)), Math.toRadians(-90))
                .endTrajectory()
                .waitSeconds(0.5)
                .stopAndAdd(intake.idle())
                .setReversed(true)
                .setTangent(0)
                .splineToConstantHeading(new Vector2d(-38, 10), Math.toRadians(0))
                .splineTo(new Vector2d(20, 10), Math.toRadians(0))
                .splineToSplineHeading(new Pose2d(56,35, Math.toRadians(180)), Math.toRadians(0))
                .endTrajectory()

                .build();

        Action mainTrajRight = drive.actionBuilder(startPose)
                .setReversed(true)
                .strafeTo(new Vector2d(-42 ,45))
                .splineToSplineHeading(new Pose2d(-38,32, Math.toRadians(180)), Math.toRadians(-90))
                .setReversed(true)
                .setTangent(0)
                .splineToConstantHeading(new Vector2d(-38, 10), Math.toRadians(0))
                .splineTo(new Vector2d(20, 10), Math.toRadians(0))
                .splineToSplineHeading(new Pose2d(56,33, Math.toRadians(180)), Math.toRadians(0))
                .build();



        Action mainTrajMiddle = drive.actionBuilder(startPose)
                .setReversed(true)
                .strafeTo(new Vector2d(-36, 30))
                .strafeTo(new Vector2d(-36, 38))
                .splineToSplineHeading(new Pose2d(-36, 34, Math.toRadians(180)), Math.toRadians(270))
                .strafeTo(new Vector2d(58,36))
                .build();








        while (!isStopRequested() && !opModeIsActive()) {

            telemetry.addData("position", bluePropThreshold.getPropPosition());
            telemetry.update();
        }




        waitForStart();

        if (isStopRequested()) return;
        Actions.runBlocking(
                new SequentialAction(
                        mainTrajRight


                )
        );
//        Actions.runBlocking(new RaceParallelCommand(
//                linearSlides.moveSlides(),
//                new SequentialAction(
//                        linearSlides.setTargetPos(1200),
//                        new SleepAction(5000)
//
//                //mainTraj // Example of a drive action
////                        runInitialTrajectory(trajLeft, trajRight, trajCenter),
////                        intake.outtake(),//outtake purple pixel
////                        runBackDropTrajectory(backDropLeft,backDropRight,backDropMiddle)
//                //drop.dropper(),
//                //linearSlides.moveSlides(0)
////                        new ParallelAction(
////                                runBackDropTrajectory(backDropLeft,backDropRight,backDropMiddle)
////
////                        )
//
//
//        )
//        ));






    }



    public Action runInitialTrajectory(Action trajLeft, Action trajRight, Action trajCenter) {
        //get camera position somehow
        //getDetection();

        switch (cameraPos) {

            case "LEFT":
                return trajLeft;


            case "RIGHT":
                return trajRight;


            case "MIDDLE":
                return trajCenter;

        }

        return trajCenter;
    }



    public Action runBackDropTrajectory(Action backDropLeft, Action backDropRight, Action backDropCenter) {
//        cameraPos = "MIDDLE";
        switch (cameraPos) {
            case "LEFT":
                return backDropLeft;


            case "RIGHT":
                return backDropRight;


            case "MIDDLE":
                return backDropCenter;
        }
        return backDropCenter;
    }



    public void getDetection() {
        if(bluePropThreshold.getPropPosition().equals("left")){
            cameraPos = "LEFT";
        } else if (bluePropThreshold.getPropPosition().equals("center")) {
            cameraPos = "CENTER";
        }else{
            cameraPos = "right";
        }
    }



    public class DropperSystem {

        Servo dumpServo1;
        Servo dumpServo2;



        public DropperSystem(HardwareMap hardwareMap) {
            dumpServo1 = hardwareMap.get(Servo.class, "dumpServoLeft");
            dumpServo2 = hardwareMap.get(Servo.class, "dumpServoRight");
            dumpServo1.setDirection(Servo.Direction.REVERSE);

        }

        public Action dropper() {
            return new Action() {
                @Override
                public boolean run(@NonNull TelemetryPacket packet) {
                    dumpServo1.setPosition(0.53);
                    dumpServo2.setPosition(0.53);
                    return false;
                }
            };
        }


    }




    public class IntakeSystem {
        private DcMotorEx motor;

        public IntakeSystem(HardwareMap hardwareMap) {
            motor = hardwareMap.get(DcMotorEx.class, "intakeMotor");
        }


        public Action outtake() {
            return new Action() {
                private boolean initialized = false;
                @Override
                public boolean run(@NonNull TelemetryPacket packet) {
                    motor.setPower(-0.75);
                    return false;
                }
            };
        }
        public Action idle() {
            return new Action() {
                private boolean initialized = false;
                @Override
                public boolean run(@NonNull TelemetryPacket packet) {
                    motor.setPower(0);
                    return false;
                }
            };
        }
    }

}





