package com.example.amogh.simplecharacterrecognition;

/**
 * Created by Amogh on 2/3/2016.
 */
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class WipeView extends View {

    private Path drawPath;
    private Paint drawPaint, canvasPaint;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;
    private Context mContext;
    private float totalPathLength = 0;
    private long timestamp = System.nanoTime();

    public WipeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setupDrawing();
    }


    public void setupDrawing() {
        drawPath = new Path();
        drawPaint = new Paint();
        int paintColor = 0xFF0000FF;
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        int strokeWidth = 40;
        drawPaint.setStrokeWidth(strokeWidth);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
        drawCanvas.drawColor(Color.WHITE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                PathMeasure pm = new PathMeasure();
                pm.setPath(drawPath, false);
                totalPathLength += pm.getLength();
                drawPath.reset();
//                startNew();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void startNew(TextView tv) {
        drawCanvas.drawColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.setBackground(null);
        }
        totalPathLength = 0;
        tv.setText("");
        invalidate();
    }

    public Bitmap returnValidBitmap()
    {
        if(totalPathLength > 0) return canvasBitmap;
        else
        {
            Toast.makeText(mContext, "Empty page :(", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
    public void changeStrokeWidth(int seekProgress)
    {
        drawPaint.setStrokeWidth(10 + seekProgress);
    }

}
