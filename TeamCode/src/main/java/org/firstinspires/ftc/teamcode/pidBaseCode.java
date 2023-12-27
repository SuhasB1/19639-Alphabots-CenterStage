package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
public class pidBaseCode extends OpMode{
    private PIDController controller;

    public static double p = 0, i = 0, d =0;

    public static double f = 0;

    public static int target = 0;

    private final double ticks_in_degree = 537.7/180;

    private DcMotorEx arm_motor1;
    private DcMotorEx arm_motor2;




    @Override
    public void init() {
        controller = new PIDController(p ,i , d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        arm_motor1 = hardwareMap.get(DcMotorEx.class, "arm_motor1");
        arm_motor2 = hardwareMap.get(DcMotorEx.class, "arm_motor2");

    }

    @Override
    public void loop() {

        controller.setPID(p, i, d);
        int armPos1 = arm_motor1.getCurrentPosition();
        //int armPos2 = arm_motor2.getCurrentPosition();

        double pid = controller.calculate(armPos1, target);
        double ff = Math.cos(Math.toRadians(target / ticks_in_degree)) * f;

        double power = pid + ff;

        arm_motor1.setPower(power);
        arm_motor2.setPower(power);

        /* or calculate pid separately for each motor
        double pid1 = controller.calculate(armPos1, target);
        double pid2 = controller.calculate(armPos2, target);

        double ff = Math.cos(Math.toRadians(target / ticks_in_degree)) * f;

        double power1 = pid1 + ff;
        double power2 = pid2 + ff;

        arm_motor1.setPower(power1);
        arm_motor2.setPower(power2);

         */

        telemetry.addData("pos ", armPos1);
        telemetry.addData("target ", target);
        telemetry.update();


    }
}
