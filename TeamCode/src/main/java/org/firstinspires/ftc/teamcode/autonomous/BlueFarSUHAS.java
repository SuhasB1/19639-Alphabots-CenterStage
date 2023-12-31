package org.firstinspires.ftc.teamcode.autonomous;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.teleop.VariableStorage;
import org.firstinspires.ftc.teamcode.vision.BluePropThreshold;
import org.firstinspires.ftc.vision.VisionPortal;

@Autonomous(name="BlueFarSuhas")
@Config
public class BlueFarSUHAS extends LinearOpMode {
    public static double Lp = 0.015, Li = 0, Ld = 0.0003;
    public static double Rp = 0.015, Ri = 0, Rd = 0.0003;
    public static double dumpInitPosition = 0.17;

    public static double dumpPosition = 0.53;
    //1200 ticks

    public static String cameraPos = "MIDDLE";
    public static double f = 0.1;
    public int left_slides_pos;
    public int right_slides_pos;
    private PIDController controllerRight;
    private PIDController controllerLeft;
    private BluePropThreshold bluePropThreshold; //Create an object of the VisionProcessor Class
    private VisionPortal portal;

    public int targetPosition = 0;

    @Override

    public void runOpMode() {
        targetPosition = 0;
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
        LinearSlides linearSlides = new LinearSlides(hardwareMap);
        DropperSystem dropper = new DropperSystem(hardwareMap);
        Pose2d startPose = new Pose2d(-36,62,Math.toRadians(-90));
        // Delcare Trajectory as such
        Action mainTraj = null;

        Action mainTrajLeft = drive.actionBuilder(startPose)
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
                .afterTime(1,  linearSlides.setTargetPosition(1500))
                .splineToSplineHeading(new Pose2d(56,35, Math.toRadians(180)), Math.toRadians(0))
                .endTrajectory()
                .stopAndAdd(dropper.dropDown())
                .build();

        Action mainTrajRight = drive.actionBuilder(startPose)
                .setReversed(true)
                .lineToY(34)
                .splineTo(new Vector2d(-44, 32), Math.toRadians(180))
                .endTrajectory()
                .waitSeconds(0.5)
                .stopAndAdd(intake.idle())
                .setReversed(true)
                .lineToX(-36)
                .setTangent(0)
                .splineToConstantHeading(new Vector2d(-38, 10), Math.toRadians(0))
                .splineTo(new Vector2d(20, 10), Math.toRadians(0))
                .afterTime(1,  linearSlides.setTargetPosition(1500))
                .splineToSplineHeading(new Pose2d(56,30, Math.toRadians(180)), Math.toRadians(0))
                .endTrajectory()
                .stopAndAdd(new SequentialAction(dropper.dropDown(), new SleepAction(1.5), dropper.initPosition(), linearSlides.setTargetPosition(0), new SleepAction(1.5)))
                .build();

        Action mainTrajMiddle = drive.actionBuilder(startPose)
                .setReversed(true)
                .lineToY(45)
                .splineToSplineHeading(new Pose2d(-38,32, Math.toRadians(0)), Math.toRadians(-90))
                .endTrajectory()
                .waitSeconds(0.5)
                .stopAndAdd(intake.idle())
                .setReversed(true)
                .setTangent(0)
//              .lineToY(20)
                .splineToConstantHeading(new Vector2d(-38, 10), Math.toRadians(0))
                .splineTo(new Vector2d(20, 10), Math.toRadians(0))
                .afterTime(0.5,  linearSlides.setTargetPosition(1500))
                .splineToSplineHeading(new Pose2d(56,35, Math.toRadians(180)), Math.toRadians(0))
                .endTrajectory()
                .stopAndAdd(new SequentialAction(dropper.dropDown(), new SleepAction(1.5), dropper.initPosition(), linearSlides.setTargetPosition(0), new SleepAction(1.5)))
                .build();

        while (!isStopRequested() && !opModeIsActive()) {
            telemetry.addData("position", bluePropThreshold.getPropPosition());
            telemetry.update();
        }
        cameraPos = bluePropThreshold.getPropPosition();

        if (cameraPos.equalsIgnoreCase("LEFT")){
            Actions.runBlocking(new RaceParallelCommand(
                    mainTrajLeft,
                    linearSlides.update()
            ));
        }else if(cameraPos.equalsIgnoreCase("MIDDLE")){
            Actions.runBlocking(new RaceParallelCommand(
                    mainTrajMiddle,
                    linearSlides.update()
            ));
        }else{
            Actions.runBlocking(new RaceParallelCommand(
                    mainTrajRight,
                    linearSlides.update()
            ));
        }
        VariableStorage.leftLinearSlidesPos = linearSlides.leftSlides.getCurrentPosition();
        VariableStorage.rightLinearSlidesPos = linearSlides.leftSlides.getCurrentPosition();
        VariableStorage.dumperPos = dropper.dumpServo1.getPosition();
        telemetry.addData("left",  linearSlides.leftSlides.getCurrentPosition());
        telemetry.addData("right",  linearSlides.rightSlides.getCurrentPosition());
        telemetry.addData("dumper pos", dropper.dumpServo1.getPosition());
        if (isStopRequested()) return;

    }
    public class DropperSystem {

        Servo dumpServo1;
        Servo dumpServo2;



        public DropperSystem(HardwareMap hardwareMap) {
            dumpServo1 = hardwareMap.get(Servo.class, "dumpServoLeft");
            dumpServo2 = hardwareMap.get(Servo.class, "dumpServoRight");
            dumpServo1.setDirection(Servo.Direction.REVERSE);

        }

        public Action dropDown() {
            return new Action() {
                @Override
                public boolean run(@NonNull TelemetryPacket packet) {
                    dumpServo1.setPosition(dumpPosition);
                    dumpServo2.setPosition(dumpPosition);
                    return false;
                }
            };
        }
        public Action initPosition() {
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
        public Action intake() {
            return new Action() {
                private boolean initialized = false;
                @Override
                public boolean run(@NonNull TelemetryPacket packet) {
                    motor.setPower(0.75);
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
        private DcMotor leftSlides;
        private DcMotor rightSlides;
        public LinearSlides(HardwareMap hardwareMap) {
            leftSlides = hardwareMap.get(DcMotorEx.class, "leftSlides");
            rightSlides = hardwareMap.get(DcMotorEx.class, "rightSlides");
        }
        public boolean closeEnough() {
            return Math.abs(((leftSlides.getCurrentPosition()+rightSlides.getCurrentPosition())/2) - targetPosition) < 20;
        }
        public Action setTargetPosition(int position) {
            return new Action() {
                @Override
                public boolean run(@NonNull TelemetryPacket t) {
                    targetPosition = position;
                    telemetry.addData("target position", getTargetPosition());
                    telemetry.update();
                    return false;
                }
            };
        }
        public double getTargetPosition() {
            return targetPosition;
        }
        public Action waitUntilFinished() {
            return new Action() {
                @Override
                public boolean run(@NonNull TelemetryPacket t) {
                    return closeEnough();
                }
            };
        }
        public Action update() {
            return new Action() {
                @Override
                public boolean run(@NonNull TelemetryPacket t) {
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
                    return true;
                }
            };
        }
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