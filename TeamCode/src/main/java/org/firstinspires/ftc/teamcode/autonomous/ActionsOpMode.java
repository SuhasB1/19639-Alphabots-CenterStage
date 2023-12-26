package org.firstinspires.ftc.teamcode.autonomous;

import android.util.Size;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.vision.BluePropThreshold;
import  org.firstinspires.ftc.teamcode.vision.TeamPropDetection;
import  org.firstinspires.ftc.teamcode.vision.TeamPropDetectionClass;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive.FollowTrajectoryAction;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.vision.TeamPropDetection;
import org.firstinspires.ftc.teamcode.vision.TeamPropDetectionClass;
import org.firstinspires.ftc.vision.VisionPortal;

@Autonomous(name="actions test")
public class ActionsOpMode extends LinearOpMode {

    public static double Lp = 0.015, Li=0, Ld = 0.0003;
    public static double Rp = 0.015, Ri=0, Rd = 0.0003;

    public static String cameraPos = "";
    public static double f =0.1;
    public int left_slides_pos;
    public int right_slides_pos;
    private PIDController controllerRight;
    private PIDController controllerLeft;
    private BluePropThreshold bluePropThreshold; //Create an object of the VisionProcessor Class
    private VisionPortal portal;
    @Override
    public void runOpMode() {
        controllerLeft = new PIDController(Lp, Li, Ld);
        controllerRight = new PIDController(Rp, Ri, Rd);
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0,0,0));
        bluePropThreshold = new BluePropThreshold();
        portal = VisionPortal.easyCreateWithDefaults(
                hardwareMap.get(WebcamName.class, "Webcam 1"), bluePropThreshold);
        FtcDashboard.getInstance().startCameraStream(bluePropThreshold, 0);
        // Delcare Trajectory as such
        Action TrajectoryAction1 = drive.actionBuilder(drive.pose)
                .lineToX(10)
                .build();

        Action TrajectoryAction2 = drive.actionBuilder(new Pose2d(15,20,0))
                .splineTo(new Vector2d(5,5), Math.toRadians(90))
                .build();

        Action trajLeft = drive.actionBuilder(new Pose2d(15,20,0))
                .splineTo(new Vector2d(5,5), Math.toRadians(90)) // random coordinates for now
                .build();

        Action trajCenter = drive.actionBuilder(new Pose2d(15,20,0))
                .splineTo(new Vector2d(5,5), Math.toRadians(90)) // random coordinates for now
                .build();

        Action trajRight = drive.actionBuilder(new Pose2d(15,20,0))
                .splineTo(new Vector2d(5,5), Math.toRadians(90)) // random coordinates for now
                .build();

        Action backDropLeft = drive.actionBuilder(new Pose2d(15,20,0))
                .splineTo(new Vector2d(5,5), Math.toRadians(90)) // random coordinates for now
                .build();

        Action backDropRight = drive.actionBuilder(new Pose2d(15,20,0))
                .splineTo(new Vector2d(5,5), Math.toRadians(90)) // random coordinates for now
                .build();

        Action backDropMiddle = drive.actionBuilder(new Pose2d(15,20,0))
                .splineTo(new Vector2d(5,5), Math.toRadians(90)) // random coordinates for now
                .build();


        while(!isStopRequested() && !opModeIsActive()) {
                telemetry.addData("position", bluePropThreshold.getPropPosition());
                telemetry.update();
        }
        IntakeSystem intake = new IntakeSystem(hardwareMap);
        LinearSlides linearSlides = new LinearSlides(hardwareMap);


        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        //TrajectoryAction1, // Example of a drive action
                        //objectDetection.detect(), //figure out what pxel to place on
                        //runInitialTrajectory(trajLeft, trajRight, trajCenter),
                        TrajectoryAction1,
                        intake.outtake()//outtake purple pixel
                        //linearSlides.moveSlides(1000)
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
        getDetection();
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
        switch(cameraPos) {
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





    public class IntakeSystem {
        private DcMotorEx motor;

        public IntakeSystem(HardwareMap hardwareMap) {
            motor = hardwareMap.get(DcMotorEx.class, "intakeMotor");
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
            leftSlides = hardwareMap.get(DcMotorEx.class, "leftSlides");
            rightSlides = hardwareMap.get(DcMotorEx.class, "rightSlides");


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