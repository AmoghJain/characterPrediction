package com.example.amogh.simplecharacterrecognition;

import android.app.Application;
import android.content.Context;

import org.dmlc.mxnet.Predictor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by harsha on 2/4/16.
 */
public class Recognizer extends Application {

    private static Predictor predictor;
    public static Predictor getPredictor() {return predictor;}

    @Override
    public void onCreate() {
        super.onCreate();
        final byte[] symbol = readRawFile(this, R.raw.symbol);
        final byte[] params = readRawFile(this, R.raw.params);
        final Predictor.Device device = new Predictor.Device(Predictor.Device.Type.CPU, 0);
        final int[] shape = {1, 3, 784, 1};
        final String key = "data";
        final Predictor.InputNode node = new Predictor.InputNode(key, shape);

        predictor = new Predictor(symbol, params, device, new Predictor.InputNode[]{node});

    }

    public static byte[] readRawFile(Context ctx, int resId)
    {
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        int size = 0;
        byte[] buffer = new byte[1024];
        try (InputStream ins = ctx.getResources().openRawResource(resId)) {
            while((size=ins.read(buffer,0,1024))>=0){
                outputStream.write(buffer,0,size);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }



}
