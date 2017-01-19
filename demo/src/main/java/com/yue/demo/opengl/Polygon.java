package com.yue.demo.opengl;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.yue.demo.MainActivity;

public class Polygon extends Activity {

    private Button button1, button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        button1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 创建一个GLSurfaceView，用于显示OpenGL绘制的图形
                GLSurfaceView glView = new GLSurfaceView(Polygon.this);
                // 创建GLSurfaceView的内容绘制器
                MyRenderer myRender = new MyRenderer();
                // 为GLSurfaceView设置绘制器
                glView.setRenderer(myRender);
                setContentView(glView);
            }
        });

        button2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
             // 创建一个GLSurfaceView，用于显示OpenGL绘制的图形
                GLSurfaceView glView = new GLSurfaceView(Polygon.this);
                // 创建GLSurfaceView的内容绘制器
                MyRotateRenderer myRender = new MyRotateRenderer();
                // 为GLSurfaceView设置绘制器
                glView.setRenderer(myRender);
                setContentView(glView);

            }
        });
    }

    private void initView() {
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(MainActivity.layoutParams_mm);
        ll.setOrientation(LinearLayout.VERTICAL);

        button1 = new Button(this);
        button1.setLayoutParams(MainActivity.layoutParams_mw);
        button1.setText("绘制平面上的多边形");
        ll.addView(button1);

        button2 = new Button(this);
        button2.setLayoutParams(MainActivity.layoutParams_mw);
        button2.setText("旋转");
        ll.addView(button2);

        setContentView(ll);
    }

}
