package org.firstinspires.ftc.teamcode.vision;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.util.concurrent.atomic.AtomicReference;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.android.Utils;
import org.opencv.core.Mat;


public class BluePropThreshold implements VisionProcessor, CameraStreamSource {
    Mat testMat = new Mat();
    Mat highMat = new Mat();
    Mat lowMat = new Mat();
    Mat finalMat = new Mat();
    double blueThreshold = 0.4;

    String outStr = "right"; //Set a default value in case vision does not work
    double averagedleftBox1 = 0;
    double averagedMiddleBox1 = 0;


    static final Rect MIDDLE_RECTANGLE = new Rect(175, 145, 80, 80);

    static final Rect left_RECTANGLE = new Rect(450, 215, 80, 80);
    private final AtomicReference<Bitmap> lastFrame =
            new AtomicReference<>(Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565));

    @Override
    public void init(int width, int height, CameraCalibration calibration) {
        lastFrame.set(Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565));
    }
    @Override
    public void getFrameBitmap(Continuation<? extends Consumer<Bitmap>> continuation) {
        continuation.dispatch(bitmapConsumer -> bitmapConsumer.accept(lastFrame.get()));
    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        Imgproc.cvtColor(frame, testMat, Imgproc.COLOR_RGB2HSV);


        Scalar lowHSVBlueLower = new Scalar(100, 100, 20);  //Beginning of Color Wheel
        Scalar lowHSVBlueUpper = new Scalar(130, 255, 255);

        Scalar highHSVBlueLower = new Scalar(90, 100, 20); //Wraps around Color Wheel
        Scalar highHSVBlueUpper = new Scalar(100, 255, 255);

        Core.inRange(testMat, lowHSVBlueLower, lowHSVBlueUpper, lowMat);
        Core.inRange(testMat, highHSVBlueLower, highHSVBlueUpper, highMat);

        testMat.release();

        Core.bitwise_or(lowMat, highMat, finalMat);

        lowMat.release();
        highMat.release();

        double leftBox = Core.sumElems(finalMat.submat(left_RECTANGLE)).val[0];
        double middleBox = Core.sumElems(finalMat.submat(MIDDLE_RECTANGLE)).val[0];

        double averagedleftBox = leftBox / left_RECTANGLE.area() / 255;
        double averagedMiddleBox = middleBox / MIDDLE_RECTANGLE.area() / 255; //Makes value [0,1]


        if (averagedleftBox > blueThreshold) {        //Must Tune blue Threshold
            outStr = "left";
            averagedleftBox1 = averagedleftBox;
        } else if (averagedMiddleBox > blueThreshold) {
            outStr = "center";
            averagedMiddleBox1 = averagedMiddleBox;
        } else {
            outStr = "right";
        }

        finalMat.copyTo(frame); /*This line should only be added in when you want to see your custom pipeline
                                  on the driver station stream, do not use this permanently in your code as
                                  you use the "frame" mat for all of your pipelines, such as April Tags*/
        Bitmap b = Bitmap.createBitmap(frame.width(), frame.height(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(frame, b);
        lastFrame.set(b);

        return null;            //You do not return the original mat anymore, instead return null



    }


    private android.graphics.Rect makeGraphicsRect(Rect rect, float scaleBmpPxToCanvasPx) {
        int left = Math.round(rect.x * scaleBmpPxToCanvasPx);
        int top = Math.round(rect.y * scaleBmpPxToCanvasPx);
        int right = left + Math.round(rect.width * scaleBmpPxToCanvasPx);
        int bottom = top + Math.round(rect.height * scaleBmpPxToCanvasPx);

        return new android.graphics.Rect(left, top, right, bottom);
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {
        Paint selectedPaint = new Paint();
        selectedPaint.setColor(Color.GREEN);
        selectedPaint.setStyle(Paint.Style.STROKE);
        selectedPaint.setStrokeWidth(scaleCanvasDensity * 4);

        Paint nonSelectedPaint = new Paint(selectedPaint);
        nonSelectedPaint.setColor(Color.RED);

        android.graphics.Rect drawRectangleLeft = makeGraphicsRect(left_RECTANGLE, scaleBmpPxToCanvasPx);
        android.graphics.Rect drawRectangleMiddle = makeGraphicsRect(MIDDLE_RECTANGLE, scaleBmpPxToCanvasPx);

        switch (outStr) {
            case "right":
                canvas.drawRect(drawRectangleLeft, nonSelectedPaint);
                canvas.drawRect(drawRectangleMiddle, nonSelectedPaint);
                break;
            case "center":
                canvas.drawRect(drawRectangleLeft, nonSelectedPaint);
                canvas.drawRect(drawRectangleMiddle, selectedPaint);
                break;
            case "left":
                canvas.drawRect(drawRectangleLeft, selectedPaint);
                canvas.drawRect(drawRectangleMiddle, nonSelectedPaint);
                break;
        }
    }

    public String getPropPosition() {  //Returns position of the prop in a String
        return outStr;
    }

    public double getAveragedleftBox() {
        return averagedleftBox1;
    }

    public double getAveragedMiddleBox() {
        return averagedMiddleBox1;
    }
}
