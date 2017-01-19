package com.yue.demo.graphics;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.yue.demo.MainActivity;
import com.yue.demo.graphics.view.MyAnimation;

/**
 * 自定义补间动画
 * 
 * @author chengyue
 * 
 */
public class ListViewTween extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(MainActivity.layoutParams_mm);
        ll.setOrientation(LinearLayout.VERTICAL);

        ListView list = new ListView(this);
        list.setLayoutParams(MainActivity.layoutParams_mm);
        ll.addView(list);

        setContentView(ll);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, new String[] { "疯狂Java讲义",
                        "轻量级Java EE企业应用实战", "经典Java EE企业应用实战", "疯狂Ajax讲义",
                        "疯狂Android讲义" });
        list.setAdapter(adapter);
        // 获取ListView组件
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrice = new DisplayMetrics();
        // 获取屏幕的宽和高
        display.getMetrics(metrice);
        // 设置对ListView组件应用动画
        list.setAnimation(new MyAnimation(metrice.xdpi / 2, metrice.ydpi / 2,
                3500));
    }
}