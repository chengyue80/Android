package com.yue.demo.graphics;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;

import com.yue.demo.R;
import com.yue.demo.MainActivity;
import com.yue.demo.util.LogUtil;

/**
 * 使用SurfaceView实现绘画
 * 
 * @author chengyue
 * 
 */
public class SurfaceViewTest extends Activity {

    private SurfaceHolder holder;
    private Paint paint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(MainActivity.layoutParams_mm);
        ll.setOrientation(LinearLayout.VERTICAL);

        SurfaceView surfaceView = new SurfaceView(this);
        surfaceView.setLayoutParams(MainActivity.layoutParams_mm);
        ll.addView(surfaceView);

        setContentView(ll);

        paint = new Paint();
        holder = surfaceView.getHolder();
        holder.addCallback(new Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                LogUtil.d(MainActivity_Graphics.Tag + "surfaceDestroyed");
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                LogUtil.d(MainActivity_Graphics.Tag + "surfaceCreated");

                Canvas canvas = holder.lockCanvas();
                // 绘制背景
                Bitmap back = BitmapFactory.decodeResource(getResources(),
                        R.drawable.sun);
                canvas.drawBitmap(back, 0, 0, null);
                // 绘制完成，释放画布，提交修改
                holder.unlockCanvasAndPost(canvas);
                // 重新锁定一次，持久化上次所绘的内容
                holder.lockCanvas(new Rect(0, 0, 0, 0));
                holder.unlockCanvasAndPost(canvas);

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                    int width, int height) {
                LogUtil.d(MainActivity_Graphics.Tag + "surfaceChanged");

            }
        });

        surfaceView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int cx = (int) event.getX();
                    int cy = (int) event.getY();

                    // 锁定SurfaceView的局部区域，只更新局部内容
                    Canvas canvas = holder.lockCanvas(new Rect(cx - 50,
                            cy - 50, cx + 50, cy + 50));
                    // 保存canvas的当前状态
                    canvas.save();
                    // 旋转画布
                    canvas.rotate(30, cx, cy);
                    // 绘制红色方块
                    paint.setColor(Color.RED);
                    canvas.drawRect(cx - 40, cy - 40, cx, cy, paint);
                    // 恢复Canvas之前的保存状态
                    canvas.restore();
                    // 绘制绿色方块
                    paint.setColor(Color.GREEN);
                    canvas.drawRect(cx, cy, cx + 40, cy + 40, paint);
                    // 绘制完成，释放画布，提交修改
                    holder.unlockCanvasAndPost(canvas);
                }
                return true;
            }
        });
    }
}
