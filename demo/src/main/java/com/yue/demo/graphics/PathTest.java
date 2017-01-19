package com.yue.demo.graphics;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.SumPathEffect;
import android.os.Bundle;
import android.view.View;

/**
 * Path 类的使用
 * 
 * @author chengyue
 * 
 */
public class PathTest extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
    }

    class MyView extends View {
        float phase;
        PathEffect[] effects = new PathEffect[8];
        int[] colors;
        private Paint paint;
        Path path;

        public MyView(Context context) {
            super(context);
            paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(1);
            // 创建、并初始化Path
            path = new Path();
            path.moveTo(0, 0);
            for (int i = 1; i <= 15; i++) {
                // 生成15个点，随机生成它们的Y座标。并将它们连成一条Path
                path.lineTo(i * 20, (float) Math.random() * 60);
            }
            // 初始化7个颜色
            colors = new int[] { Color.BLACK, Color.BLUE, Color.CYAN,
                    Color.GREEN, Color.MAGENTA, Color.MAGENTA, Color.RED,
                    Color.YELLOW };
            // -----------下面开始初始化7中路径效果----------
            // 不使用路径效果。
            effects[0] = null;
            // 使用CornerPathEffect路径效果，平滑效果
            effects[1] = new CornerPathEffect(10);
            // 初始化DiscretePathEffect，离散型
            effects[2] = new DiscretePathEffect(3.0f, 5.0f);
        }

        @SuppressLint("DrawAllocation")
        @Override
        protected void onDraw(Canvas canvas) {
            // Mlog.d(MainActivity_Graphics.Tag + "path test onDraw !");
            // 将背景填充成白色
            canvas.drawColor(Color.WHITE);
            // 初始化DashPathEffect，DashPathEffect有动画效果,dash : 虚线
            // 注：只有style 为STROKE_AND_FILL 或 STROKE 有效
            effects[3] = new DashPathEffect(new float[] { 20, 10, 5, 10 },
                    phase);
            // 初始化PathDashPathEffect，PathDashPathEffect有动画效果
            Path p = new Path();
            p.addRect(0, 0, 8, 8, Path.Direction.CCW);
            effects[4] = new PathDashPathEffect(p, 12, phase,
                    PathDashPathEffect.Style.ROTATE);

            Path p1 = new Path();
            p1.addRect(0, 0, 8, 8, Path.Direction.CW);
            effects[5] = new PathDashPathEffect(p1, 12, phase,
                    PathDashPathEffect.Style.ROTATE);

            // 初始化ComposePathEffect
            effects[6] = new ComposePathEffect(effects[2], effects[4]);
            effects[7] = new SumPathEffect(effects[4], effects[3]);
            // 对Canvas执行坐标变换：将画布“整体位移”到8、8处开始绘制
            canvas.translate(8, 8);
            // 依次使用7中不同路径效果、7种不同的颜色来绘制路径
            for (int i = 0; i < effects.length; i++) {
                paint.setPathEffect(effects[i]);
                paint.setColor(colors[i]);
                canvas.drawPath(path, paint);
                canvas.translate(0, 60);
            }
            // 改变phase值，形成动画效果
            phase += 1;
            invalidate();
        }
    }
}