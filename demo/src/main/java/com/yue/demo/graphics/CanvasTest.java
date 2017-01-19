package com.yue.demo.graphics;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.yue.demo.R;

/**
 * Android 绘图基础 ：Canvas 和Paint的使用
 * 
 * @author chengyue
 * 
 */
public class CanvasTest extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DrawView view = new DrawView(this);
        setContentView(view);
    }

    class DrawView extends View {

        public DrawView(Context context) {
            super(context);
        }

        public DrawView(Context context, AttributeSet attrs) {
            super(context, attrs);

        }

        // 重写该方法，进行绘图

        @SuppressLint("DrawAllocation")
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            // 把整张画布绘制成白色
            canvas.drawColor(Color.WHITE);

            Paint paint = new Paint();
            // 去锯齿
            paint.setAntiAlias(true);
            // 设置颜色
            paint.setColor(Color.BLUE);

            // 设置画笔风格
            paint.setStyle(Paint.Style.STROKE);
            // 设置画笔填充
            paint.setStrokeWidth(3f);

            // 绘制圆形drawCircle(圆心坐标：x,圆心坐标： y,半径：r， paint);
            canvas.drawCircle(40, 40, 30, paint);

            // 绘制正方形drawRect(左上角顶点坐标X：10, 左上角顶点坐标y：80, 右上角顶点坐标x：70,
            // 左下角顶点坐标y：140,
            // paint);
            // 70-10=140-80=60（边长）
            canvas.drawRect(10, 80, 70, 140, paint);

            // 绘制矩形：(左上角顶点坐标X：10, 左上角顶点坐标y：150, 右上角顶点坐标x：70,
            // 左下角顶点坐标y：190,paint);
            // 宽：70-10
            // 高：190-150
            canvas.drawRect(10, 150, 70, 190, paint);

            // 绘制圆角矩形：RectF(左上角顶点坐标X：10, 左上角顶点坐标y：200, 右上角顶点坐标x：70,
            // 左下角顶点坐标y：230,paint);
            // 宽：70-10
            // 高：230-200

            RectF re1 = new RectF(10, 200, 70, 230);
            /**
             * drawRoundRect的参数说明
             * 
             * rect：RectF对象。
             * 
             * rx：x方向上的圆角半径。
             * 
             * ry：y方向上的圆角半径。
             * 
             * paint：绘制时所使用的画笔。
             */
            canvas.drawRoundRect(re1, 15, 15, paint);

            // 绘制椭圆
            // 70 - 10 ： x轴轴长
            // 270 - 240 ： y轴轴长
            RectF re11 = new RectF(10, 240, 70, 270);
            canvas.drawOval(re11, paint);

            /** 定义一个Path对象，封闭成一个三角形 */
            Path path1 = new Path();
            path1.moveTo(40, 280);// 起点
            path1.lineTo(10, 340);
            path1.lineTo(70, 340);
            path1.close();
            // 根据Path进行绘制，绘制三角形
            canvas.drawPath(path1, paint);

            /** 定义一个Path对象，封闭成一个五角形 */
            Path path2 = new Path();
            path2.moveTo(40, 360);
            path2.lineTo(10, 392);
            path2.lineTo(26, 420);
            path2.lineTo(54, 420);
            path2.lineTo(70, 392);
            path2.close();
            // 根据Path进行绘制，绘制五角形
            canvas.drawPath(path2, paint);

            /** 设置填充风格后绘制 */
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.RED);
            canvas.drawCircle(120, 40, 30, paint);
            // 绘制正方形
            canvas.drawRect(90, 80, 150, 140, paint);
            // 绘制矩形
            canvas.drawRect(90, 150, 150, 190, paint);
            RectF re2 = new RectF(90, 200, 150, 230);
            // 绘制圆角矩形
            canvas.drawRoundRect(re2, 15, 15, paint);
            RectF re21 = new RectF(90, 240, 150, 270);
            // 绘制椭圆
            canvas.drawOval(re21, paint);
            Path path3 = new Path();
            path3.moveTo(90, 340);
            path3.lineTo(150, 340);
            path3.lineTo(120, 290);
            path3.close();
            // 绘制三角形
            canvas.drawPath(path3, paint);
            Path path4 = new Path();
            path4.moveTo(106, 360);
            path4.lineTo(134, 360);
            path4.lineTo(150, 392);
            path4.lineTo(120, 420);
            path4.lineTo(90, 392);
            path4.close();
            // 绘制五角形
            canvas.drawPath(path4, paint);

            /**
             * -设置渐变器后绘制 为Paint设置渐变器
             * 
             * 创建LinearGradient并设置渐变颜色数组
             * 
             * 第一个,第二个参数表示渐变起点, 可以设置起点终点在对角等任意位置
             * 
             * 第三个,第四个参数表示渐变终点
             * 
             * 第五个参数表示渐变颜色
             * 
             * 第六个参数可以为空,表示坐标,值为0-1 new float[] {0.25f, 0.5f, 0.75f, 1 },
             * 如果这是空的，颜色均匀分布，沿梯度线。
             * 
             * 第七个表示平铺方式
             * 
             * CLAMP重复最后一个颜色至最后
             * 
             * MIRROR重复着色的图像水平或垂直方向已镜像方式填充会有翻转效果
             * 
             * REPEAT重复着色的图像水平或垂直方向
             */
            Shader mShader = new LinearGradient(0, 0, 40, 60, new int[] {
                    Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW }, null,
                    Shader.TileMode.REPEAT);
            paint.setShader(mShader);
            // 设置阴影
            paint.setShadowLayer(45, 10, 10, Color.GRAY);
            // 绘制圆形
            canvas.drawCircle(200, 40, 30, paint);
            // 绘制正方形
            canvas.drawRect(170, 80, 230, 140, paint);
            // 绘制矩形
            canvas.drawRect(170, 150, 230, 190, paint);
            RectF re3 = new RectF(170, 200, 230, 230);
            // 绘制圆角矩形
            canvas.drawRoundRect(re3, 15, 15, paint);
            RectF re31 = new RectF(170, 240, 230, 270);
            // 绘制椭圆
            canvas.drawOval(re31, paint);
            Path path5 = new Path();
            path5.moveTo(170, 340);
            path5.lineTo(230, 340);
            path5.lineTo(200, 290);
            path5.close();
            // 根据Path进行绘制，绘制三角形
            canvas.drawPath(path5, paint);
            Path path6 = new Path();
            path6.moveTo(186, 360);
            path6.lineTo(214, 360);
            path6.lineTo(230, 392);
            path6.lineTo(200, 420);
            path6.lineTo(170, 392);
            path6.close();
            // 根据Path进行绘制，绘制五角形
            canvas.drawPath(path6, paint);

            /** 设置字符大小后绘制-- */
            paint = new Paint();
            paint.setColor(Color.RED);
            paint.setAntiAlias(true);
            paint.setTextSize(40);
            paint.setStyle(Paint.Style.FILL);
            paint.setShader(mShader);
            paint.setStrokeWidth(5f);
            // 绘制7个字符串
            canvas.drawText("圆形", 260, 50, paint);
            canvas.drawText(getResources().getString(R.string.square), 260,
                    120, paint);
            canvas.drawText(getResources().getString(R.string.rect), 260, 175,
                    paint);
            canvas.drawText(getResources().getString(R.string.round_rect), 260,
                    220, paint);
            canvas.drawText(getResources().getString(R.string.oval), 260, 260,
                    paint);
            canvas.drawText(getResources().getString(R.string.triangle), 260,
                    325, paint);
            canvas.drawText(getResources().getString(R.string.pentagon), 260,
                    390, paint);
        }
    }

}
