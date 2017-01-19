package com.yue.demo.event;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.yue.demo.R;

/**
 * 自定义View 模拟飞机
 * 
 * @author chengyue
 * 
 */
public class PlantView extends View {

    public float currentX;
    public float currentY;
    Bitmap bitmap;

    public PlantView(Context context) {
        super(context);
        bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.plane);
        setFocusable(true);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        canvas.drawBitmap(bitmap, currentX, currentY, paint);
    }
}
