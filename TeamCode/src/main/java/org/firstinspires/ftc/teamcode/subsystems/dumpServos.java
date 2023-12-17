package org.firstinspires.ftc.teamcode.subsystems;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@TeleOp(name="outtake function")
public class dumpServos extends OpMode {
    private Servo dumpServo1;
    private Servo dumpServo2;
    public static double dumpPosition = 0;


    @Override
    public void init(){
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        dumpServo1 = hardwareMap.get(Servo.class, "dumpServoLeft");
        dumpServo2 = hardwareMap.get(Servo.class, "dumpServoRight");
        dumpServo1.setDirection(Servo.Direction.REVERSE);
    }

    @Override
    public void loop(){

        dumpServo1.setPosition(dumpPosition);
        dumpServo2.setPosition(dumpPosition);
    }
}



