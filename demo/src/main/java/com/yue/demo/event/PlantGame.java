package com.yue.demo.event;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;

import com.yue.demo.R;

/**
 * 控制飞机移动的activity
 * 
 * @author chengyue
 * 
 */
public class PlantGame extends Activity {
    // 定义飞机移动的速度
    private final int speed = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去掉窗口标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 创建PlaneView组件
        final PlantView plant = new PlantView(this);
        setContentView(plant);
        plant.setBackgroundResource(R.drawable.gradienter_back);
        // 获取窗口管理器
        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        // 获得屏幕宽和高
        display.getMetrics(metrics);
        // 设置飞机的初始位置
        plant.currentX = metrics.widthPixels / 2;
        plant.currentY = metrics.heightPixels - 50;
        // 为draw组件键盘事件绑定监听器
        plant.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                // 获取由哪个键触发的事件
                switch (event.getKeyCode()) {
                // 控制飞机下移
                case KeyEvent.KEYCODE_S:
                    plant.currentY += speed;
                    break;
                // 控制飞机上移
                case KeyEvent.KEYCODE_W:
                    plant.currentY -= speed;
                    break;
                // 控制飞机左移
                case KeyEvent.KEYCODE_A:
                    plant.currentX -= speed;
                    break;
                // 控制飞机右移
                case KeyEvent.KEYCODE_D:
                    plant.currentX += speed;
                    break;
                }
                // 通知planeView组件重绘
                plant.invalidate();
                return true;
            }
        });

        // InputMethodManager imm = (InputMethodManager)
        // getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.
    }
}
