package com.yue.demo.graphics;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.yue.demo.MainActivity;
import com.yue.demo.util.LogUtil;

public class ShowWaVe extends Activity {

    private SurfaceHolder holder;
    private Paint paint;
    private int width = 320;
    private int height = 320;
    private int X_OFFSET = 5;
    private int cx = X_OFFSET;

    // 实际Y轴的坐标
    private int curY;

    private Timer timer = new Timer();
    private TimerTask task = null;

    private final int SIN_ID = 0x10;
    private final int CON_ID = 0x11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(MainActivity.layoutParams_mm);
        ll.setOrientation(LinearLayout.VERTICAL);

        Button sin = new Button(this);
        sin.setLayoutParams(MainActivity.layoutParams_ww);
        sin.setText("sin");
        sin.setId(SIN_ID);
        ll.addView(sin);

        Button cos = new Button(this);
        cos.setLayoutParams(MainActivity.layoutParams_ww);
        cos.setText("cos");
        cos.setId(CON_ID);
        ll.addView(cos);

        SurfaceView surfaceView = new SurfaceView(this);
        surfaceView.setLayoutParams(MainActivity.layoutParams_mm);
        ll.addView(surfaceView);

        setContentView(ll);

        holder = surfaceView.getHolder();
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3);

        OnClickListener listener = new OnClickListener() {

            @Override
            public void onClick(final View v) {
                LogUtil.d(MainActivity_Graphics.Tag + "OnClickListener");
                drawBack(holder);

                cx = X_OFFSET;
                if (task != null) {
                    task.cancel();
                }
                task = new TimerTask() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        curY = height / 2;
                        int cy = (int) (v.getId() == SIN_ID ? curY - 100
                                * Math.sin((cx - 5) * 2 * Math.PI / 150) : curY
                                - 100 * Math.cos((cx - 5) * 2 * Math.PI / 150));
                        Canvas canvas = holder.lockCanvas(new Rect(cx, cy - 2,
                                cx + 2, cy + 2));
                        canvas.drawPoint(cx, cy, paint);
                        LogUtil.d(MainActivity_Graphics.Tag + "cx : " + cx
                                + "   cy : " + cy);
                        cx++;
                        if (cx > width) {
                            task.cancel();
                            task = null;
                        }
                        holder.unlockCanvasAndPost(canvas);
                    }
                };

                timer.schedule(task, 0, 30);
            }
        };

        sin.setOnClickListener(listener);
        cos.setOnClickListener(listener);
        holder.addCallback(new Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                LogUtil.d(MainActivity_Graphics.Tag + "surfaceDestroyed");
                timer.cancel();
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                LogUtil.d(MainActivity_Graphics.Tag + "surfaceCreated");
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                    int width, int height) {
                LogUtil.d(MainActivity_Graphics.Tag + "surfaceChanged");
                drawBack(holder);

            }
        });

    }

    private void drawBack(SurfaceHolder holder) {
        Canvas canvas = holder.lockCanvas();
        canvas.drawColor(Color.WHITE);
        height = canvas.getHeight();
        width = canvas.getWidth();
        LogUtil.d(MainActivity_Graphics.Tag + "height : " + height + "   width : "
                + width);
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStrokeWidth(2);
        // 绘制坐标轴
        canvas.drawLine(X_OFFSET, height / 2, width, height / 2, p);// X
        canvas.drawLine(X_OFFSET, 40, X_OFFSET, height, p);// Y
        holder.unlockCanvasAndPost(canvas);
        holder.lockCanvas(new Rect(0, 0, 0, 0));
        holder.unlockCanvasAndPost(canvas);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (task != null)
                task.cancel();
            task = null;
        }
        return super.onKeyDown(keyCode, event);
    }
}
