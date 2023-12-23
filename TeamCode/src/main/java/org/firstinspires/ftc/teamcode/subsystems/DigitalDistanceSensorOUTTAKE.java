package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;

@Config
@TeleOp(name="digital sensor OUTTAKE")
public class DigitalDistanceSensorOUTTAKE extends LinearOpMode {
    DcMotor intakeMotor;
    public static double intakePower = 0;
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        DigitalChannel leftDistanceSensor = hardwareMap.get(DigitalChannel.class, "leftDistanceSensor");
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");


        waitForStart();
        while (opModeIsActive()){
            boolean detectedLeft = leftDistanceSensor.getState();
            detectedLeft = !detectedLeft;
            if (detectedLeft){
                intakeMotor.setPower(-intakePower);
            }
            else{
                intakeMotor.setPower(intakePower);
            }
            telemetry.addData("left detected? " , detectedLeft);
            telemetry.addLine("testing if tle is working");
            telemetry.update();
        }
    }


}




