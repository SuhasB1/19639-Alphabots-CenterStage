package org.firstinspires.ftc.teamcode.subsystems;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
@TeleOp(name="basic cycle")
public class basicCycle extends OpMode {
    private DcMotorEx intakeMotor;
    private Servo dumpServo1;
    private Servo dumpServo2;
    public static double dumpPosition = 0.65;
    //0.16 for starting
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
    public int left_arm_pos;
    public int right_arm_pos;
    private DcMotorEx armmotor1;
    private DcMotorEx armmotor2;


    @Override
    public void init(){
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");
        dumpServo1 = hardwareMap.get(Servo.class, "dumpServoLeft");
        dumpServo2 = hardwareMap.get(Servo.class, "dumpServoRight");

        controllerLeft = new PIDController(Lp,Li,Ld);
        controllerRight = new PIDController(Rp,Ri,Rd);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        armmotor1 = hardwareMap.get(DcMotorEx.class, "leftSlides");
        armmotor2 = hardwareMap.get(DcMotorEx.class, "rightSlides");
        armmotor2.setDirection(DcMotorSimple.Direction.REVERSE);







    }

    @Override    public void loop(){
        intakeMotor.setPower(intakePower);
        dumpServo1.setPosition(dumpPosition);
        dumpServo2.setPosition(dumpPosition);

        controllerLeft.setPID(Lp, Li, Ld);
        controllerRight.setPID(Rp, Ri, Rd);
        left_arm_pos = (armmotor1.getCurrentPosition());
        right_arm_pos = (armmotor2.getCurrentPosition());
        double leftPID = controllerLeft.calculate(left_arm_pos, targetPos);
        double rightPID = controllerRight.calculate(right_arm_pos, (targetPos - 30));
        double leftPower = leftPID + f;
        double rightPower = rightPID + f;
        armmotor1.setPower(leftPower);
        armmotor2.setPower(rightPower);
        telemetry.addData("target", targetPos);
        telemetry.addData("currentLeft", left_arm_pos);
        telemetry.addData("currentRight", right_arm_pos);
        telemetry.addData("left power", leftPower);
        telemetry.addData("right power", rightPower);

    }
}



