package com.yue.demo.event;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 通过回调实现跟随手指运动的小球
 * 
 * @author chengyue
 * 
 */
public class DrawView extends View {

    private float currentX = 40;
    private float currentY = 50;
    private Paint p = new Paint();

    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        currentX = event.getX();
        currentY = event.getY();

        invalidate();
        // 返回false 则继续传递事件
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        p.setColor(Color.RED);
        canvas.drawCircle(currentX, currentY, 15, p);
    }

}
