package com.yue.demo.graphics;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.yue.demo.R;
import com.yue.demo.util.LogUtil;

/**
 * 采用双缓冲实现画图板
 * 
 * @author chengyue
 * 
 */
public class HandDraw extends Activity {
	private static final String TAG = HandDraw.class.getSimpleName();
    EmbossMaskFilter emboss;
    BlurMaskFilter blur;
    private DrawView dv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dv = new DrawView(this);
        setContentView(dv);
        emboss = new EmbossMaskFilter(new float[] { 1.5f, 1.5f, 1.5f }, 0.6f,
                6, 4.2f);
        blur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
    }

    @Override
    // 负责创建选项菜单
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator = new MenuInflater(this);
        // 装载R.menu.my_menu对应的菜单，并添加到menu中
        inflator.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    // 菜单项被单击后的回调方法
    public boolean onOptionsItemSelected(MenuItem mi) {
        // 判断单击的是哪个菜单项，并针对性的作出响应。
        switch (mi.getItemId()) {
        case R.id.red:
            dv.paint.setColor(Color.RED);
            mi.setChecked(true);
            break;
        case R.id.green:
            dv.paint.setColor(Color.GREEN);
            mi.setChecked(true);
            break;
        case R.id.blue:
            dv.paint.setColor(Color.BLUE);
            mi.setChecked(true);
            break;
        case R.id.width_1:
            dv.paint.setStrokeWidth(1);
            break;
        case R.id.width_3:
            dv.paint.setStrokeWidth(3);
            break;
        case R.id.width_5:
            dv.paint.setStrokeWidth(5);
            break;
        case R.id.blur:
            dv.paint.setMaskFilter(blur);
            break;
        case R.id.emboss:
            dv.paint.setMaskFilter(emboss);
            break;
        }
        return true;
    }

    class DrawView extends View {

        float preX;
        float preY;
        private Path path;
        public Paint paint = null;
        int VIEW_WIDTH = 320;
        int VIEW_HEIGHT = 480;
        // 定义一个内存中的图片，该图片将作为缓冲区
        Bitmap cacheBitmap = null;
        // 定义cacheBitmap上的Canvas对象
        Canvas cacheCanvas = null;

        public DrawView(Context context) {
            super(context);
            WindowManager windowManager = getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            // 获得屏幕宽和高
            VIEW_WIDTH = metrics.widthPixels;
            VIEW_HEIGHT = metrics.heightPixels;
            // 创建一个与该View相同大小的缓存区
            cacheBitmap = Bitmap.createBitmap(VIEW_WIDTH, VIEW_HEIGHT,
                    Config.ARGB_8888);
            cacheCanvas = new Canvas();
            path = new Path();
            // 设置cacheCanvas将会绘制到内存中的cacheBitmap上
            cacheCanvas.setBitmap(cacheBitmap);
            // 设置画笔的颜色
            paint = new Paint(Paint.DITHER_FLAG);
            paint.setColor(Color.RED);
            // 设置画笔风格
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(4);
            // 反锯齿
            paint.setAntiAlias(true);
            paint.setDither(true);
        }

        public DrawView(Context context, AttributeSet set) {
            super(context, set);

        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            // 获取拖动事件的发生位置
            float x = event.getX();
            float y = event.getY();
            switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                preX = x;
                preY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtil.i(TAG, MainActivity_Graphics.Tag + "x ： " + x + "    y : " + y);
                path.quadTo(preX, preY, x, y);
                preX = x;
                preY = y;
                // cacheCanvas.drawPath(path, paint); // ①
                // invalidate();
                break;
            case MotionEvent.ACTION_UP:
                cacheCanvas.drawPath(path, paint); // ①
                path.reset();
                break;
            }
            invalidate();
            // 返回true表明处理方法已经处理该事件
            return true;
        }

        @SuppressLint("DrawAllocation")
        @Override
        public void onDraw(Canvas canvas) {
            Paint bmpPaint = new Paint();
            // 将cacheBitmap绘制到该View组件上
            canvas.drawBitmap(cacheBitmap, 0, 0, bmpPaint); // ②
            // 沿着path绘制
            canvas.drawPath(path, paint);
        }
    }
}
