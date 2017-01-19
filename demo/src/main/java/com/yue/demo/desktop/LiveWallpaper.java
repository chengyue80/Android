package com.yue.demo.desktop;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.yue.demo.R;

public class LiveWallpaper extends WallpaperService {

    // 记录用户触碰点的位图
    private Bitmap heart;

    // 实现WallpaperService必须实现的抽象方法
    @Override
    public Engine onCreateEngine() {
        // TODO Auto-generated method stub

        heart = BitmapFactory.decodeResource(getResources(), R.drawable.heart);
        return new MyEngine();
    }

    class MyEngine extends Engine {

        // 记录程序界面是否可见
        private boolean mVisible;
        // 记录当前当前用户动作事件的发生位置
        private float mTouchX = -1;
        private float mTouchY = -1;
        // 记录当前需要绘制的矩形的数量
        private int count = 1;
        // 记录绘制第一个矩形所需坐标变换的X、Y坐标的偏移
        private int originX = 50, originY = 50;
        // 定义画笔
        private Paint mPaint = new Paint();
        // 定义一个Handler
        Handler mHandler = new Handler();
        // 定义一个周期性执行的任务
        private final Runnable drawTarget = new Runnable() {
            public void run() {
                drawFrame();
            }
        };

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            // TODO Auto-generated method stub

            mPaint.setARGB(76, 0, 0, 255);
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL);
            setTouchEventsEnabled(true);
            super.onCreate(surfaceHolder);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mHandler.removeCallbacks(drawTarget);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            // TODO Auto-generated method stub

            mVisible = visible;
            if (visible)
                drawFrame();
            else
                mHandler.removeCallbacks(drawTarget);
            super.onVisibilityChanged(visible);
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            // TODO Auto-generated method stub
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                mTouchX = event.getX();
                mTouchY = event.getY();
            } else {
                mTouchX = -1;
                mTouchY = -1;

            }
            super.onTouchEvent(event);
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset,
                float xOffsetStep, float yOffsetStep, int xPixelOffset,
                int yPixelOffset) {
            drawFrame();
        }

        /** 定义绘制图形的工具方法 */
        protected void drawFrame() {
            // 获取该壁纸的SurfaceHolder
            final SurfaceHolder holder = getSurfaceHolder();
            Canvas c = null;
            try {
                // 对画布加锁
                c = holder.lockCanvas();
                if (c != null) {
                    // 绘制背景色
                    c.drawColor(0xffffffff);
                    // 在触碰点绘制心形
                    drawTouchPoint(c);
                    // 设置画笔的透明度
                    mPaint.setAlpha(76);
                    c.translate(originX, originY);
                    // 采用循环绘制count个矩形
                    for (int i = 0; i < count; i++) // ①
                    {
                        c.translate(80, 0);
                        c.scale(0.95f, 0.95f);
                        c.rotate(20f);
                        c.drawRect(0, 0, 150, 75, mPaint);
                    }
                }
            } finally {
                if (c != null)
                    holder.unlockCanvasAndPost(c);
            }
            mHandler.removeCallbacks(drawTarget);
            // 调度下一次重绘
            if (mVisible) {
                count++;
                if (count >= 50) {
                    Random rand = new Random();
                    count = 1;
                    originX += (rand.nextInt(60) - 30);
                    originY += (rand.nextInt(60) - 30);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // 指定0.1秒后重新执行mDrawCube一次
                mHandler.postDelayed(drawTarget, 100); // ②
            }
        }

        // 在屏幕触碰点绘制圆圈
        private void drawTouchPoint(Canvas c) {
            if (mTouchX >= 0 && mTouchY >= 0) {
                // 设置画笔的透明度
                mPaint.setAlpha(255);
                c.drawBitmap(heart, mTouchX, mTouchY, mPaint);
            }
        }
    }
}
