package com.yue.demo.graphics;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.yue.demo.R;
import com.yue.demo.util.LogUtil;

/**
 * 移动游戏背景
 * 
 * @author chengyue
 * 
 */
public class MoveBack extends Activity {

    private int BACK_HEIGHT = 1700;
    private int WIDTH = 320;
    private int HEIGHT = 800;
    private MoveView moveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        moveView = new MoveView(this);
        setContentView(moveView);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        // BACK_HEIGHT = metrics.heightPixels * 2;
        // WIDTH = metrics.widthPixels;
        HEIGHT = metrics.heightPixels;

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            moveView.handler.removeMessages(0x123);
        }
        return super.onKeyDown(keyCode, event);
    }

    class MoveView extends View {

        private Bitmap back = null;
        private Bitmap plane = null;

        Handler handler;
        private int startY = BACK_HEIGHT - HEIGHT;

        public MoveView(Context context) {
            super(context);

            back = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.back_img);
            plane = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.plane);
            handler = new Handler() {

                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    if (msg.what == 0x123) {
                        // 重新开始移动
                        // if (startY <= (0 - HEIGHT)) {
                        // startY = BACK_HEIGHT - HEIGHT;
                        // } else {
                        // startY -= 5;
                        // }
                        invalidate();
                    }
                }
            };

            new Timer().schedule(new TimerTask() {

                @Override
                public void run() {
                    handler.sendEmptyMessage(0x123);
                }
            }, 0, 100);

        }

        @SuppressLint("DrawAllocation")
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            // 根据原始位图和Matrix创建新图片
            LogUtil.d(MainActivity_Graphics.Tag + "startY : " + startY
                    + "  HEIGHT : " + HEIGHT);
            //
            if (startY <= (0 - HEIGHT)) {
                startY = BACK_HEIGHT - HEIGHT;
            } else {
                LogUtil.d("11111111111");
                if (startY <= 0) {
                    LogUtil.d("222222222");
                    Bitmap up = Bitmap.createBitmap(back, 0, BACK_HEIGHT
                            + startY, WIDTH, startY == 0 ? 1 : (0 - startY));
                    Bitmap newBitmap = Bitmap.createBitmap(back, 0, 0, WIDTH,
                            HEIGHT);
                    canvas.drawBitmap(up, 0, 0, null);
                    canvas.drawBitmap(newBitmap, 0, 0 - startY, null);
                } else {

                    LogUtil.d("33333");
                    Bitmap newBitmap = Bitmap.createBitmap(back, 0, startY,
                            WIDTH, HEIGHT);
                    canvas.drawBitmap(newBitmap, 0, 0, null);
                }
                startY -= 20;
            }
            canvas.drawBitmap(plane, 160, 380, null);
        }
    }
}
