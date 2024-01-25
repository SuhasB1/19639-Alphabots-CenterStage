package org.firstinspires.ftc.teamcode.teleop;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@TeleOp(name = "Teleop Robo-Centric (full buttons)")
@Config
public class TeleOpRoboCentric extends LinearOpMode {

    double speed;
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
    Servo planeServo;
    public static double dumpInitPosition = 0.9; //0.5

    public static double dumpPosition = 0.52; //0.1
    //0.16 for first
    //0.15 for second (2700)
    //0.6 for init
    public static double intakePower = 0;

    public static double planeInit = 0.1;

    public static double planeLaunch = 0.5;
    //power is 0.75
    ElapsedTime timer = new ElapsedTime();
    ElapsedTime retractionTimer = new ElapsedTime();


    private PIDController controllerRight;
    private PIDController controllerLeft;
    public static double Lp = 0.015, Li=0, Ld = 0.0003;
    public static double Rp = 0.015, Ri=0, Rd = 0.0003;
    public static double f =0.1;
    public static int targetPos;
    public int left_slides_pos;
    public int right_slides_pos = 0;
    DcMotorEx leftSlides;
    DcMotorEx rightSlides;

    DcMotorEx leftFront;
    DcMotorEx rightFront;
    DcMotorEx leftBack;
    DcMotorEx rightBack;




    @Override
    public void runOpMode() {


        leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        leftBack = hardwareMap.get(DcMotorEx.class, "leftBack");
        rightBack = hardwareMap.get(DcMotorEx.class, "rightBack");
        rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");


        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        // Telemetry Init
        telemetry.setMsTransmissionInterval(50);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        //TODO: REMEMBER TO COMMENT OUT BEFORE COMP

        // Motor Init
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");
        dumpServo1 = hardwareMap.get(Servo.class, "dumpServoLeft");
        dumpServo2 = hardwareMap.get(Servo.class, "dumpServoRight");
        planeServo = hardwareMap.get(Servo.class, "planeServo");



        controllerLeft = new PIDController(Lp,Li,Ld);
        controllerRight = new PIDController(Rp,Ri,Rd);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        leftSlides = hardwareMap.get(DcMotorEx.class, "leftSlides");
        rightSlides = hardwareMap.get(DcMotorEx.class, "rightSlides");

        rightSlides.setDirection(DcMotorSimple.Direction.REVERSE);

        leftSlides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightSlides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftSlides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightSlides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftSlides.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightSlides.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        VoltageSensor voltageSensor = hardwareMap.voltageSensor.iterator().next();


        dumpServo1.setPosition(dumpInitPosition);
        dumpServo2.setPosition(dumpInitPosition);


        left_slides_pos = 0;
        right_slides_pos = 0;

       // if(Math.abs(VariableStorage.leftLinearSlidesPos) > 20 || Math.abs(VariableStorage.rightLinearSlidesPos) > 20){
            //vertPIDController(0);
       // }
       // if(VariableStorage.dumperPos != dumpInitPosition){
          //  dumpServo1.setPosition(dumpInitPosition);
         //   dumpServo2.setPosition(dumpInitPosition);
       // }

        targetPos = 0;
        intakePower = 0;

        MecanumDrive drive = new MecanumDrive(hardwareMap, VariableStorage.currentPose);

        waitForStart();

        if (isStopRequested()) return;


        // Run Period


        while (opModeIsActive() && !isStopRequested()) {
            loopTime.reset();
//            allHubs.forEach(LynxModule::clearBulkCache);

            previousGamepad1.copy(currentGamepad1);
            previousGamepad2.copy(currentGamepad2);

            currentGamepad1.copy(gamepad1);
            currentGamepad2.copy(gamepad2);
            currentGamepad1.reset();
            currentGamepad2.reset();

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
                targetPos = 2600;
                dumpPosition = 0.52;
            }
            if(gamepad2.square){
                targetPos = 2000;
                dumpPosition = 0.53;
            }
            if(gamepad2.cross){
                    dumpServo1.setPosition(dumpInitPosition);
                    dumpServo2.setPosition(dumpInitPosition);
                    targetPos = 0;
            }

            if(gamepad2.dpad_right){
                intakePower = -0.9;
            }
            if(gamepad2.dpad_up){
                intakePower = 0;
            }
            if(gamepad1.dpad_up) {
                planeServo.setPosition(planeLaunch);
            }
            if(gamepad2.dpad_left){
                intakePower = 0.9;
            }
            if(gamepad1.triangle) {
                targetPos = 2100;

            }
            if(gamepad1.circle) {
                dumpServo1.setPosition(0);
                dumpServo2.setPosition(0);
            }
            if(gamepad1.square) {
                targetPos = 1650;
            }
            if(gamepad1.cross) {
                targetPos = 650;
            }
            if (gamepad2.left_bumper){
                dumpServo1.setPosition(dumpInitPosition);
                dumpServo2.setPosition(dumpInitPosition);
            }
            if(gamepad2.right_bumper){
                dumpServo1.setPosition(dumpPosition);
                dumpServo2.setPosition(dumpPosition);
            }

            if(gamepad1.dpad_up) {
                planeServo.setPosition(0.5);
            }

            if(gamepad1.left_trigger > 0) {
                leftFront.setPower(-0.3);
                leftBack.setPower(0.3);
                rightFront.setPower(0.3);
                rightBack.setPower(-0.3);
            }

            if(gamepad1.right_trigger > 0) {
                leftFront.setPower(0.3);
                leftBack.setPower(-0.3);
                rightFront.setPower(-0.3);
                rightBack.setPower(0.3);
            }
            // Update the speed
            if (padSlowMode) {
                speed = .25;
            } else if (padFastMode) {
                speed = 1;
            } else {
                speed = .9;
            }
            drive.setDrivePowers(new PoseVelocity2d(
                    new Vector2d(
                            -gamepad1.left_stick_y * speed,
                            -gamepad1.left_stick_x * speed
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
            telemetry.addData("voltage", voltageSensor.getVoltage());
            telemetry.addData("left slides pos", left_slides_pos);
            telemetry.addData("right slides pos", right_slides_pos);
            telemetry.addData("Servo 1 Pos", dumpServo1.getPosition());
            telemetry.addData("Servo 2 Pos", dumpServo2.getPosition());
            telemetry.update();
        }

    }
    public void vertPIDController(int targetPos){
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