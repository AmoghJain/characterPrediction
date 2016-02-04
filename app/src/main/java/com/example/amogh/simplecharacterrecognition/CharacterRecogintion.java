package com.example.amogh.simplecharacterrecognition;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class CharacterRecogintion extends AppCompatActivity {

    private Button mPredict, mClear;
    private WipeView mWipe;
    private TextView mChar;
    private SeekBar mStrokeWidth;
    private Bitmap btmap;
    private Bitmap processedBitmap;
    private ImageView testImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_recogintion);

        mWipe        = (WipeView) findViewById(R.id.mainCanvas);
        mPredict     = (Button) findViewById(R.id.predict);
        mClear       = (Button) findViewById(R.id.clear);
        mChar        = (TextView) findViewById(R.id.oChar);
        mStrokeWidth = (SeekBar) findViewById(R.id.seekBar);
        testImage    = (ImageView) findViewById(R.id.testImage);

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

//            if(processedBitmap==null)
//            {
//                return;
//            }


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


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_character_recogintion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class PredictionTask extends AsyncTask<Bitmap, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Bitmap... bmps) {
            return MxNetUtils.identifyChar(bmps[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            Toast.makeText(getApplicationContext(), "Done predicting", Toast.LENGTH_SHORT).show();
            testImage.setImageBitmap(result);
//            mChar.setText(result);
        }
    }


}
