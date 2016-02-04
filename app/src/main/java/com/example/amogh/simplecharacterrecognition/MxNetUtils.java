package com.example.amogh.simplecharacterrecognition;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.Log;

import org.dmlc.mxnet.Predictor;

/**
 * Created by Amogh on 2/4/2016.
 */

public class MxNetUtils
{
private MxNetUtils(){};

public static Bitmap identifyChar(Bitmap bitmap)
{
    String result = "";
    Bitmap gray = toGrayScale(bitmap);

    Predictor predictor = Recognizer.getPredictor();

    float[] image_data = Predictor.inputFromImage(new Bitmap[]{gray}, 0, 0, 0);
    predictor.forward("data", image_data);
    float[] results = predictor.getOutput(0);

    int index = 0;
    for (int i = 0; i < results.length; ++i) {
        if (results[index] < results[i]) index = i;
    }

    Log.d("ANSWER : ", index + "" );

    return gray;


}

    private static Bitmap toGrayScale(Bitmap bitmap)
    {
        final double GS_RED = 0.299;
        final double GS_GREEN = 0.587;
        final double GS_BLUE = 0.114;


        bitmap = Bitmap.createScaledBitmap(bitmap, 28, 28, true);
        bitmap = createInvertedBitmap(bitmap);

        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        // pixel information
        int A, R, G, B;
        int pixel;

        // get image size
        int width = bmOut.getWidth();
        int height = bmOut.getHeight();

        // scan through every single pixel
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get one pixel color
                pixel = bitmap.getPixel(x, y);
                // retrieve color of all channels
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                // take conversion up to one single value
                R = G = B = (int)(GS_RED * R + GS_GREEN * G + GS_BLUE * B);
                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

//    testImageView.setImageBitmap(bmOut);
        return bmOut;
    }
    private static Bitmap createInvertedBitmap(Bitmap src) {
        ColorMatrix colorMatrix_Inverted =
                new ColorMatrix(new float[] {
                        -1,  0,  0,  0, 255,
                        0, -1,  0,  0, 255,
                        0,  0, -1,  0, 255,
                        0,  0,  0,  1,   0});

        ColorFilter ColorFilter_Sepia = new ColorMatrixColorFilter(
                colorMatrix_Inverted);

        Bitmap bitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();

        paint.setColorFilter(ColorFilter_Sepia);
        canvas.drawBitmap(src, 0, 0, paint);

        return bitmap;
    }


}
