package com.example.amogh.simplecharacterrecognition;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.ImageView;

import java.nio.ByteBuffer;

/**
 * Created by Amogh on 2/4/2016.
 */

public class MxNetUtils
{
private MxNetUtils(){};

public static String identifyChar(final Bitmap bitmap,ImageView testImageView)
{
    final double GS_RED = 0.299;
    final double GS_GREEN = 0.587;
    final double GS_BLUE = 0.114;

    // create output bitmap
    Bitmap bmOut = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
    // pixel information
    int A, R, G, B;
    int pixel;

    // get image size
    int width = bitmap.getWidth();
    int height = bitmap.getHeight();

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

    testImageView.setImageBitmap(bmOut);
    return "0";
}

}