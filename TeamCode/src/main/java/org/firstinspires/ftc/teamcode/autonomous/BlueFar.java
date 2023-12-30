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

@Autonomous(name="BlueFar")
@Config
public class BlueFar extends LinearOpMode {

    LynxModule CONTROL_HUB;
    LynxModule EXPANSION_HUB;
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

    public void runOpMode() {
        targetPos = 0;
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
        IntakeSystem intake = new IntakeSystem(hardwareMap);
        Slide slides = new Slide(hardwareMap);
        LinearSlides linearSlides = new LinearSlides(hardwareMap);
        DropperSystem drop = new DropperSystem(hardwareMap);

        Pose2d startPose = new Pose2d(-36,62,Math.toRadians(-90));
        // Delcare Trajectory as such
        Action mainTraj = drive.actionBuilder(startPose)
                .setReversed(true)
                .lineToY(45)
                .splineToSplineHeading(new Pose2d(-38,32, Math.toRadians(0)), Math.toRadians(-90))
                .endTrajectory()
                .waitSeconds(0.5)
                .stopAndAdd(intake.idle())
                .setReversed(true)
                .setTangent(0)
//                .lineToY(20)


                .splineToConstantHeading(new Vector2d(-38, 10), Math.toRadians(0))
                .splineTo(new Vector2d(20, 10), Math.toRadians(0))
                .splineToSplineHeading(new Pose2d(53,38, Math.toRadians(180)), Math.toRadians(0))
//                .stopAndAdd(slides.setTargetPosition(1200))


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






        while (!isStopRequested() && !opModeIsActive()) {
            allHubs.forEach(LynxModule::clearBulkCache);

            telemetry.addData("position", bluePropThreshold.getPropPosition());
            telemetry.update();
        }




        waitForStart();

        if (isStopRequested()) return;
        Actions.runBlocking(
                new ParallelAction(
                        linearSlides.moveSlides(),
                        new SequentialAction(
                                linearSlides.setTargetPos(0),
                                linearSlides.setTargetPos(1200)
                        )
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


    public class LinearSlides {
        private DcMotorEx leftSlides;
        private DcMotorEx rightSlides;


        public LinearSlides(HardwareMap hardwareMap) {
            leftSlides = hardwareMap.get(DcMotorEx.class, "leftSlides");
            rightSlides = hardwareMap.get(DcMotorEx.class, "rightSlides");


        }
        public void vertPIDController(){
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

        public Action moveSlides() {

            return new Action() {
                @Override
                public boolean run(@NonNull TelemetryPacket packet) {
                    vertPIDController();
                    return Math.abs(leftSlides.getCurrentPosition() - targetPos) > 15 || Math.abs(rightSlides.getCurrentPosition() - targetPos) > 15;
                }
            };
        }

        public Action setTargetPos(int target) {
            return new Action() {
                @Override
                public boolean run(@NonNull TelemetryPacket packet) {
                    targetPos = target;
                    return false;
                }
            };
        }



    }

    public static class Slide extends ControlledMotor {



        /**
         * This initializes the slide motor. This should be run before any other methods.
         *
         * @param hardwareMap The hardware map to use to get the motors.
         */
        public Slide(HardwareMap hardwareMap) {
            leftSlides = hardwareMap.get(DcMotorEx.class, "leftSlides");
            rightSlides = hardwareMap.get(DcMotorEx.class, "rightSlides");
            leftSlides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            leftSlides.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightSlides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightSlides.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        }

        /**
         * This stops the slide, sets the state to down, sets the target to 0, and resets the encoder.
         */
        public void reset() {
            targetPos = 0;
            leftSlides.setPower(0);
            leftSlides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftSlides.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightSlides.setPower(0);
            rightSlides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }


        /**
         * This updates the slide motor to match the current state. This should be run in a loop.
         */
        public void update() {
            // overly complex slide code
            // obtain the encoder position and calculate the error
            controllerLeft.setPID(Lp, Li, Ld);
            controllerRight.setPID(Rp, Ri, Rd);
            left_slides_pos = (leftSlides.getCurrentPosition());
            right_slides_pos = (rightSlides.getCurrentPosition());
            double leftPID = controllerLeft.calculate(left_slides_pos, targetPosition);
            double rightPID = controllerRight.calculate(right_slides_pos, targetPosition);
            double leftPower = leftPID + f;
            double rightPower = rightPID + f;
            leftSlides.setPower(leftPower);
            rightSlides.setPower(rightPower);
            telemetry.addData("Left Position",leftSlides.getCurrentPosition());
            telemetry.addData("Right Position",rightSlides.getCurrentPosition());
            telemetry.update();
        }


        public boolean closeEnough() {
            return Math.abs(leftSlide.getCurrentPosition() - targetPosition) < 20;
        }

        public Action setTargetPosition(double position) {
            return new Action() {
                @Override
                public boolean run(@NonNull TelemetryPacket t) {
                    setTarget(position);
                    return false;
                }
            };
        }
        public Action waitUntilFinished() {
            return new Action() {
                @Override
                public boolean run(@NonNull TelemetryPacket t) {
                    return closeEnough();
                }
            };
        }



    }


    /**
     * This class controls the claw.
     */

    public abstract static class ControlledMotor {
        public DcMotorEx leftSlides;
        public DcMotorEx rightSlides;
        double targetPosition;
        public double getTargetPosition() {
            return targetPosition;
        }
        public void setTarget(double targetPosition) {
            this.targetPosition = targetPosition;
        }
        public abstract void update();
        public abstract void reset();
        public abstract boolean closeEnough();
    }

    public static class RaceParallelCommand implements Action {
        private final Action[] actions;

        public RaceParallelCommand(Action... actions) {
            this.actions = actions;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket t) {
            boolean finished = true;
            for (Action action : actions) finished = finished && action.run(t);
            return finished;
        }

        @Override
        public void preview(@NonNull Canvas canvas) {
            for (Action action : actions) action.preview(canvas);
        }


    }







}