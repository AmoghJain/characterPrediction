package com.example.amogh.simplecharacterrecognition;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CharacterRecogintion extends AppCompatActivity {

    private Button mPredict, mClear;
    private WipeView mWipe;
    private TextView mChar;
    private SeekBar mStrokeWidth;
    private Bitmap btmap;
    private Bitmap processedBitmap;
    private ImageView testImage;
    private AssetManager assetManager;
    private final String TAG = "CharacaterRecognition";

    static {
        if (!OpenCVLoader.initDebug()) {
            //Handle error
        }
        else {
            System.loadLibrary("predictor");
        }
    }

    public native void neuralInit(String storageLocation);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_recogintion);

        mWipe        = (WipeView) findViewById(R.id.mainCanvas);
        mPredict     = (Button) findViewById(R.id.predict);
        mClear       = (Button) findViewById(R.id.clear);
        mChar        = (TextView) findViewById(R.id.oChar);
        mStrokeWidth = (SeekBar) findViewById(R.id.seekBar);

        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWipe.startNew(mChar);


            }
        });

        mPredict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            btmap=mWipe.returnValidBitmap();
            PredictionTask ptask = new PredictionTask();
            ptask.execute(new Bitmap[]{btmap});
            }
        });

        mStrokeWidth.setProgress(30);

        mStrokeWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mWipe.changeStrokeWidth(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        assetManager = getAssets();
        String path = getStorageDirectory(getApplicationContext()).toString();
        File file= new File(path + "/hindi-weights");
        if(file.exists())
            Toast.makeText(this, "Good to go!", Toast.LENGTH_SHORT);//.show();
        else{
            Toast.makeText(this, "First time init. Please wait...", Toast.LENGTH_LONG).show();
            unpackDataTask unzipTask = new unpackDataTask();
            unzipTask.execute(new String[]{"hindi-model.zip", path +"/"});
        }
        neuralInit(path + "/hindi-weights");


    }

    private class PredictionTask extends AsyncTask<Bitmap, Void, String> {
        @Override
        protected String doInBackground(Bitmap... bmps) {
            Predictor predictor = new Predictor(bmps[0]);
            return predictor.predict();
        }

        @Override
        protected void onPostExecute(String result) {
//            Toast.makeText(getApplicationContext(), "Done predicting", Toast.LENGTH_SHORT).show();
//            testImage.setImageBitmap(result);
            mChar.setText(result);
        }
    }

    /** Finds the proper location on the SD card where we can save files. */
    public File getStorageDirectory(Context ctx) {
        //Log.d(TAG, "getStorageDirectory(): API level is " + Integer.valueOf(android.os.Build.VERSION.SDK_INT));

        String state = null;
        try {
            state = Environment.getExternalStorageState();
        } catch (RuntimeException e) {
            Log.e(TAG, "Is the SD card visible?", e);
            //showErrorMessage("Error", "Required external storage (such as an SD card) is unavailable.");
        }

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            try {
                return ctx.getExternalFilesDir(Environment.MEDIA_MOUNTED);
            } catch (NullPointerException e) {
                // We get an error here if the SD card is visible, but full
                Log.e(TAG, "External storage is unavailable");
                //showErrorMessage("Error", "Required external storage (such as an SD card) is full or unavailable.");
            }

        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            Log.e(TAG, "External storage is read-only");
            //showErrorMessage("Error", "Required external storage (such as an SD card) is unavailable for data storage.");
        } else {
            // Something else is wrong. It may be one of many other states, but all we need
            // to know is we can neither read nor write
            Log.e(TAG, "External storage is unavailable");
            //showErrorMessage("Error", "Required external storage (such as an SD card) is unavailable or corrupted.");
        }
        return null;
    }

    private class unpackDataTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            unpackZip(params[0], params[1]);
            return null;
        }
    }

    private boolean unpackZip(String zipfilename, String path)
    {
        InputStream is;
        ZipInputStream zis;
        try
        {

            String filename;
            is = assetManager.open(zipfilename);

            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null)
            {
                // zapis do souboru
                filename = ze.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(path + filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(path + filename);

                // cteni zipu a zapis
                while ((count = zis.read(buffer)) != -1)
                {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
//            Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show();
            return false;
        }

//        Toast.makeText(this, "All good", Toast.LENGTH_LONG).show();
        return true;
    }



}
