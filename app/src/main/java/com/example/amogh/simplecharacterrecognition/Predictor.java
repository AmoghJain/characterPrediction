package com.example.amogh.simplecharacterrecognition;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

class Predictor {

    public native int[] predict(long nativeObjAddr);

    private Mat bmpMat = new Mat();

    public Predictor(Bitmap bmp) {
        preProcess(bmp);
    }

    private void preProcess(Bitmap bmp) {
        Utils.bitmapToMat(bmp, bmpMat);
        Imgproc.cvtColor(bmpMat, bmpMat, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(bmpMat, bmpMat, 127, 255, Imgproc.THRESH_BINARY);
    }

    public String predict() {
        int[] results = predict(bmpMat.getNativeObjAddr());

        String answer = "" + (char)results[0];
        return answer;
    }

    public Bitmap getProcessedBitmap()
    {
        Bitmap bmp = Bitmap.createBitmap(bmpMat.cols(), bmpMat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(bmpMat, bmp);
        return bmp;
    }


}