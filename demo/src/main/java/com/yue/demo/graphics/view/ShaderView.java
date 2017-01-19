package com.yue.demo.graphics.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.yue.demo.R;

public class ShaderView extends View {
    // 声明画笔
    public Paint paint;

    public ShaderView(Context context, AttributeSet set) {
        super(context, set);
        paint = new Paint();
        paint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 使用指定Paint对象画矩形
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        canvas.drawBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.water),
                0, 0, paint);
    }
}
