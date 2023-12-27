package org.firstinspires.ftc.teamcode.autonomous;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.vision.BluePropThreshold;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.lynx.LynxModule;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
import org.firstinspires.ftc.vision.VisionPortal;

import java.util.List;

@Autonomous(name="BlueFar")
public class BlueFar extends LinearOpMode {

    LynxModule CONTROL_HUB;
    LynxModule EXPANSION_HUB;
    public static double Lp = 0.015, Li = 0, Ld = 0.0003;
    public static double Rp = 0.015, Ri = 0, Rd = 0.0003;
    public static double dumpInitPosition = 0.15;

    public static double dumpOnePosition = 0.535;

    public static double dumpTwoPosition = dumpInitPosition;

    public static String cameraPos = "";
    public static double f = 0.1;
    public int left_slides_pos;
    public int right_slides_pos;
    private PIDController controllerRight;
    private PIDController controllerLeft;
    private BluePropThreshold bluePropThreshold; //Create an object of the VisionProcessor Class
    private VisionPortal portal;

    @Override
    public void runOpMode() {

        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);

        for (LynxModule module : allHubs) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
        CONTROL_HUB = allHubs.get(0);
        EXPANSION_HUB = allHubs.get(1);

        // Telemetry Init
        telemetry.setMsTransmissionInterval(50);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        controllerLeft = new PIDController(Lp, Li, Ld);
        controllerRight = new PIDController(Rp, Ri, Rd);
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(-36, 59, Math.toRadians(270)));
        bluePropThreshold = new BluePropThreshold();
        portal = VisionPortal.easyCreateWithDefaults(
                hardwareMap.get(WebcamName.class, "Webcam 1"), bluePropThreshold);
        FtcDashboard.getInstance().startCameraStream(bluePropThreshold, 0);
        // Delcare Trajectory as such
        Action mainTraj = drive.actionBuilder(drive.pose)
                .lineToYConstantHeading(-36)
                .build();

        Action trajLeft = drive.actionBuilder(new Pose2d(-36, 35, 90))
                .turnTo(0)
                .build();

        Action trajCenter = drive.actionBuilder(new Pose2d(-36, 35, 90))
                .splineToConstantHeading(new Vector2d(-36,14), Math.toRadians(90))
                .build();

        Action trajRight = drive.actionBuilder(new Pose2d(-36, 35, 90))
                .turnTo(180)
                .build();

        Action backDropLeft = drive.actionBuilder(new Pose2d(-36, 35, Math.toRadians(0)))
                .splineToConstantHeading(new Vector2d(-23.4, 7.4), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(30, 10), Math.toRadians(0))
                .splineToLinearHeading(new Pose2d(39, 35.8, Math.toRadians(180)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(56, 40), Math.toRadians(180))
                .build();

        Action backDropMiddle = drive.actionBuilder(new Pose2d(-36, 35, Math.toRadians(270)))
                .splineToConstantHeading(new Vector2d(-23.4, 7.4), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(30, 10), Math.toRadians(0))
                .splineToLinearHeading(new Pose2d(39, 35.8, Math.toRadians(180)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(56, 35), Math.toRadians(180))
                .build();

        Action backDropRight = drive.actionBuilder(new Pose2d(-36, 35, Math.toRadians(180)))
                .splineToConstantHeading(new Vector2d(-23.4, 7.4), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(30, 10), Math.toRadians(0))
                .splineToLinearHeading(new Pose2d(39, 35.8, Math.toRadians(180)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(56, 28), Math.toRadians(180))
                .build();



//
//        IntakeSystem intake = new IntakeSystem(hardwareMap);
//        LinearSlides linearSlides = new LinearSlides(hardwareMap);
//        DropperSystem drop = new DropperSystem(hardwareMap);

        while (!isStopRequested() && !opModeIsActive()) {
//            allHubs.forEach(LynxModule::clearBulkCache);

            telemetry.addData("position", bluePropThreshold.getPropPosition());
            telemetry.update();
        }


        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        mainTraj, // Example of a drive action
                        runInitialTrajectory(trajLeft, trajRight, trajCenter),
                        //intake.outtake(),//outtake purple pixel
                        runBackDropTrajectory(backDropLeft,backDropRight,backDropMiddle)
                        //linearSlides.moveSlides(1000),
                        //drop.dropper(),
                        //linearSlides.moveSlides(0)
//                        new ParallelAction(
//                                runBackDropTrajectory(backDropLeft,backDropRight,backDropMiddle)
//
//                        )

                        // This action and the following action do the same thing

                        // Only that this action uses a Lambda expression to reduce complexity

//                        new ParallelAction( // several actions being run in parallel
//                                TrajectoryAction2, // Run second trajectory
//                                (telemetryPacket) -> { // Run some action
//                                    telemetry.addLine("tetsstst");
//                                    return false;
//                                }
//                        ),


                )
        );


    }



    public Action runInitialTrajectory(Action trajLeft, Action trajRight, Action trajCenter) {
        //get camera position somehow
        //getDetection();
        cameraPos = "MIDDLE";
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
        cameraPos = "MIDDLE";
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

    public void backDropSlides(LinearSlides linearSlides) {
        sleep(2000);
        linearSlides.moveSlides(1000);
    }

    public class DropperSystem {

        Servo dumpServo1;
        Servo dumpServo2;



        public DropperSystem(HardwareMap hardwareMap) {
//            dumpServo1 = hardwareMap.get(Servo.class, "dumpServoLeft");
//            dumpServo2 = hardwareMap.get(Servo.class, "dumpServoRight");
//            dumpServo1.setDirection(Servo.Direction.REVERSE);

        }

        public Action dropper() {
            return new Action() {
                @Override
                public boolean run(@NonNull TelemetryPacket packet) {
                    dumpServo1.setPosition(dumpInitPosition);
                    dumpServo2.setPosition(dumpInitPosition);
                    return false;
                }
            };
        }


    }




    public class IntakeSystem {
        private DcMotorEx motor;

        public IntakeSystem(HardwareMap hardwareMap) {
//            motor = hardwareMap.get(DcMotorEx.class, "intakeMotor");
        }


        public Action outtake() {
            return new Action() {
                private boolean initialized = false;
                ElapsedTime timer = new ElapsedTime();

                @Override
                public boolean run(@NonNull TelemetryPacket packet) {
                    timer.startTime();
                    motor.setPower(-0.75);
                    sleep(2000);
                    return false;
                }
            };
        }
    }

    public class LinearSlides {
        private DcMotorEx leftSlides;
        private DcMotorEx rightSlides;


        public LinearSlides(HardwareMap hardwareMap) {
//            leftSlides = hardwareMap.get(DcMotorEx.class, "leftSlides");
//            rightSlides = hardwareMap.get(DcMotorEx.class, "rightSlides");


        }
        public void vertPIDController(double targetPos){
            controllerLeft.setPID(Lp, Li, Ld);
            controllerRight.setPID(Rp, Ri, Rd);
            left_slides_pos = (leftSlides.getCurrentPosition());
            right_slides_pos = (rightSlides.getCurrentPosition());
            double leftPID = controllerLeft.calculate(left_slides_pos, targetPos);
            double rightPID = controllerRight.calculate(right_slides_pos, targetPos);
            double leftPower = leftPID + f;
            double rightPower = rightPID + f;
            leftSlides.setPower(leftPower);
            rightSlides.setPower(rightPower);
            telemetry.addData("Left Position",leftSlides.getCurrentPosition());
            telemetry.addData("Right Position",rightSlides.getCurrentPosition());
            telemetry.update();

        }


        public Action moveSlides(double targetPos) {

            return new Action() {

                @Override
                public boolean run(@NonNull TelemetryPacket packet) {
                    vertPIDController(targetPos);
                    return Math.abs((controllerLeft.getPositionError() + controllerRight.getPositionError()) / 2)  > 15;
                }
            };
        }
    }



}