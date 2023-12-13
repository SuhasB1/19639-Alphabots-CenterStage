package org.firstinspires.ftc.teamcode.autonomous;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Config
@TeleOp(name="basic intake test")
public class intake_test extends OpMode {
    private DcMotorEx intakeMotor;
    public static double intakePower = 0.5;


    @Override
    public void init(){
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        intakeMotor = hardwareMap.get(DcMotorEx.class, "left");






    }

    @Override
    public void loop(){
        intakeMotor.setPower(intakePower);
    }
}



