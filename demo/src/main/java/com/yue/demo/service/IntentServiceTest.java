package com.yue.demo.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.yue.demo.MainActivity;

/**
 * IntentService测试
 * 
 * @author chengyue
 * 
 */
public class IntentServiceTest extends Activity {

    static final String tag = "IntentServiceTest";

    private Button button, button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 创建需要启动的Service的Intent
                Intent intent = new Intent(IntentServiceTest.this,
                        MyService.class);
                // 启动Service
                startService(intent);
            }
        });
        button2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntentServiceTest.this,
                        MyIntentService.class);
                // 启动Service
                startService(intent);
            }
        });
    }

    private void initView() {
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(MainActivity.layoutParams_mm);
        ll.setGravity(Gravity.CENTER_HORIZONTAL);

        button = new Button(this);
        button.setLayoutParams(MainActivity.layoutParams_mw);
        button.setText("启动普通Service");
        ll.addView(button);

        button2 = new Button(this);
        button2.setLayoutParams(MainActivity.layoutParams_mw);
        button2.setText("启动IntentService");
        ll.addView(button2);

        setContentView(ll);
    }

}
