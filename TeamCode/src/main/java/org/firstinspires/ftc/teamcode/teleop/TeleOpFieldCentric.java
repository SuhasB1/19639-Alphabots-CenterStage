package org.firstinspires.ftc.teamcode.teleop;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.teleop.PIDFController;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

import java.util.List;

@TeleOp(name = "Teleop Robo-Centric (full buttons)")
@Config
public class TeleOpFieldCentric extends LinearOpMode {

    // TODO: PhotonFTC

    double speed;
    LynxModule CONTROL_HUB;
    LynxModule EXPANSION_HUB;
    final ElapsedTime loopTime = new ElapsedTime();
    boolean pixelInOuttake; //need to use later
    final Gamepad currentGamepad1 = new Gamepad();
    final Gamepad currentGamepad2 = new Gamepad();
    final Gamepad previousGamepad1 = new Gamepad();
    final Gamepad previousGamepad2 = new Gamepad();

    boolean showMotorTelemetry = true;
    boolean showLoopTimes = true;
    boolean showPoseTelemetry = true;
    DcMotorEx intakeMotor;
    Servo dumpServo1;
    Servo dumpServo2;
    public static double dumpInitPosition = 0.15;

    public static double dumpOnePosition = 0.575;

    public static double dumpTwoPosition = dumpInitPosition;
    //0.15 for starting
    //0.575 for one pixel
    //0.58 for second pixel
    public static double intakePower = 0;
    //power is 0.75
    ElapsedTime timer = new ElapsedTime();

    private PIDController controllerRight;
    private PIDController controllerLeft;
    public static double Lp = 0.015, Li=0, Ld = 0.0003;
    public static double Rp = 0.015, Ri=0, Rd = 0.0003;
    public static double f =0.1;
    public static int targetPos = 0;
    public int left_slides_pos;
    public int right_slides_pos;
    DcMotorEx leftSlides;
    DcMotorEx rightSlides;


    @Override
    public void runOpMode() {
        //PhotonCore.enable();

        //  Initialization Period

        // Enable Performance Optimization

        //PhotonCore.start(hardwareMap); // TODO: if somethings' wrong THIS IS WHY
        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);

        for (LynxModule module : allHubs) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }
        CONTROL_HUB = allHubs.get(0);
        EXPANSION_HUB = allHubs.get(1);

        // Telemetry Init
        telemetry.setMsTransmissionInterval(50);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        //TODO: REMEMBER TO COMMENT OUT BEFORE COMP

        // Motor Init
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");
        dumpServo1 = hardwareMap.get(Servo.class, "dumpServoLeft");
        dumpServo2 = hardwareMap.get(Servo.class, "dumpServoRight");
        dumpServo1.setDirection(Servo.Direction.REVERSE);
        controllerLeft = new PIDController(Lp,Li,Ld);
        controllerRight = new PIDController(Rp,Ri,Rd);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        leftSlides = hardwareMap.get(DcMotorEx.class, "leftSlides");
        rightSlides = hardwareMap.get(DcMotorEx.class, "rightSlides");
        rightSlides.setDirection(DcMotorSimple.Direction.REVERSE);
        DigitalChannel leftDistanceSensor = hardwareMap.get(DigitalChannel.class, "leftDistanceSensor");

        dumpServo1.setPosition(dumpInitPosition);
        dumpServo2.setPosition(dumpInitPosition);

        targetPos = 0;
        intakePower = 0;

        MecanumDrive drive = new MecanumDrive(hardwareMap, PoseStorage.currentPose);

        waitForStart();

        if (isStopRequested()) return;


        // Run Period

        while (opModeIsActive() && !isStopRequested()) {
            loopTime.reset();
            allHubs.forEach(LynxModule::clearBulkCache);

            previousGamepad1.copy(currentGamepad1);
            previousGamepad2.copy(currentGamepad2);

            currentGamepad1.copy(gamepad1);
            currentGamepad2.copy(gamepad2);

            // CONTROLS

            // Gamepad 1
            // Driving Modifiers
            boolean padSlowMode = gamepad1.left_bumper;
            boolean padFastMode = gamepad1.right_bumper;
            boolean padResetPose = gamepad1.dpad_left && !previousGamepad1.dpad_left;

            //PID
            vertPIDController(targetPos);
            //start intake motor
            intakeMotor.setPower(intakePower);

            //slides positions
            if(gamepad2.triangle){
                targetPos = 2500;
            }
            if(gamepad2.square){
                targetPos = 2000;
            }
            if(gamepad2.circle){
                targetPos = 1500;
            }
            if(gamepad2.cross){
                dumpServo1.setPosition(0.16);
                dumpServo2.setPosition(0.16);
                targetPos = 0;

            }
            if(gamepad2.dpad_left){
                intakePower = -0.7;
            }
            if(gamepad2.dpad_up){
                intakePower = 0;
            }
            if(gamepad2.dpad_right){
                intakePower = 0.7;
            }
            if (gamepad2.left_bumper){
                dumpServo1.setPosition(dumpOnePosition);
                dumpServo2.setPosition(dumpOnePosition);
            }
            if(gamepad2.right_bumper){
                dumpServo1.setPosition(dumpTwoPosition);
                dumpServo2.setPosition(dumpTwoPosition);
            }
            boolean detectedLeft = leftDistanceSensor.getState();
            detectedLeft = !detectedLeft;
            if (detectedLeft){
                intakePower = -0.7;
                timer.startTime();
                if(timer.seconds() >= 2){
                    intakePower = 0;
                }
            }else{
                timer.reset();
            }
            // Update the speed
            if (padSlowMode) {
                speed = .35;
            } else if (padFastMode) {
                speed = 1;
            } else {
                speed = .8;
            }
            drive.setDrivePowers(new PoseVelocity2d(
                    new Vector2d(
                            gamepad1.left_stick_y * speed,
                            gamepad1.left_stick_x * speed
                    ),
                    -gamepad1.right_stick_x * speed
            ));

            drive.updatePoseEstimate();

            TelemetryPacket packet = new TelemetryPacket();
            MecanumDrive.drawRobot(packet.fieldOverlay(), drive.pose); //new Pose2d(new Vector2d(IN_PER_TICK * drive.pose.trans.x,IN_PER_TICK * drive.pose.trans.y), drive.pose.rot)
            FtcDashboard.getInstance().sendTelemetryPacket(packet);

            double loopTimeMs = loopTime.milliseconds();

            if (showPoseTelemetry) {
                telemetry.addLine("--- Pose ---");
                telemetry.addData("x", drive.pose.position.x);
                telemetry.addData("y", drive.pose.position.y);
                telemetry.addData("heading", drive.pose.heading.toDouble());
            }
            if (showLoopTimes) {
                telemetry.addLine("--- Loop Times ---");
                telemetry.addData("loopTimeMs", loopTimeMs);
                telemetry.addData("loopTimeHz", 1000.0 / loopTimeMs);
            }
            telemetry.update();
        }

    }

    public void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void vertPIDController(int targetPosition){
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
    }

    //intake toggle on/off button.
    //linear slides high
    //linear slides mid
    //linear slides low
    //linear slides down
    //dumper release
}