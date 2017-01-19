package com.yue.demo.graphics;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.yue.demo.R;
import com.yue.demo.graphics.view.ShaderView;

/**
 * 使用Shader填充图形
 * 
 * @author chengyue
 * 
 */
public class ShaderTest extends Activity implements OnClickListener {
    // 声明位图渲染对象
    private Shader[] shaders = new Shader[5];
    // 声明颜色数组
    private int[] colors;
    ShaderView myView;

    // 自定义视图类
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shader);
        // 获得Bitmap实例
        Bitmap bm = BitmapFactory.decodeResource(getResources(),
                R.drawable.water);
        myView = (ShaderView) findViewById(R.id.shaderView);
        // 设置渐变的颜色组，也就是按红、绿、蓝的方式渐变
        colors = new int[] { Color.RED, Color.GREEN, Color.BLUE };
        // 实例化BitmapShader,x坐标方向重复图形，y坐标方向镜像图形
        shaders[0] = new BitmapShader(bm, TileMode.REPEAT, TileMode.MIRROR);
        // 实例化LinearGradient
        shaders[1] = new LinearGradient(0, 0, 100, 100, colors, null,
                TileMode.REPEAT);
        // 实例化RadialGradient
        shaders[2] = new RadialGradient(100, 100, 80, colors, null,
                TileMode.REPEAT);
        // 实例化SweepGradient
        shaders[3] = new SweepGradient(160, 160, colors, null);
        // 实例化ComposeShader
        shaders[4] = new ComposeShader(shaders[1], shaders[2],
                PorterDuff.Mode.DARKEN);
    }

    @Override
    public void onClick(View source) {
        switch (source.getId()) {
        case R.id.bn1:
            myView.paint.setShader(shaders[0]);
            break;
        case R.id.bn2:
            myView.paint.setShader(shaders[1]);
            break;
        case R.id.bn3:
            myView.paint.setShader(shaders[2]);
            break;
        case R.id.bn4:
            myView.paint.setShader(shaders[3]);
            break;
        case R.id.bn5:
            myView.paint.setShader(shaders[4]);
            break;
        }
        // 重绘界面
        myView.invalidate();
    }

    // class ShaderView extends View {
    // // 声明画笔
    // public Paint paint;
    //
    // public ShaderView(Context context, AttributeSet set) {
    // super(context, set);
    // paint = new Paint();
    // paint.setColor(Color.RED);
    // }
    //
    // @Override
    // protected void onDraw(Canvas canvas) {
    // super.onDraw(canvas);
    // // 使用指定Paint对象画矩形
    // canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
    // }
    // }

}