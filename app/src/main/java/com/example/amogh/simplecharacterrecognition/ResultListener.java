package com.example.amogh.simplecharacterrecognition;

import android.graphics.Bitmap;

/**
 * Created by harsha on 2/11/16.
 */
public interface ResultListener
{
    public void setText(String text);
    public void setPreviousChar(Bitmap bmp);
}
