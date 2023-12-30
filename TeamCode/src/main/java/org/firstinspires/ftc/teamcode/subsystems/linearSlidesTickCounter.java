package org.firstinspires.ftc.teamcode.subsystems;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Config
@TeleOp(name="linear slides tick counter")
public class linearSlidesTickCounter extends OpMode {
    private DcMotorEx armmotor1;
    private DcMotorEx armmotor2;


    @Override
    public void init(){
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        armmotor1 = hardwareMap.get(DcMotorEx.class, "leftSlides");
        armmotor2 = hardwareMap.get(DcMotorEx.class, "rightSlides");
        armmotor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armmotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);







    }

    @Override
    public void loop(){
        telemetry.addData("left",armmotor1.getCurrentPosition());
        telemetry.addData("right",armmotor2.getCurrentPosition() );
        telemetry.update();



    }
}



