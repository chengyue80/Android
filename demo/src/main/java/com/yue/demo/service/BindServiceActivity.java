package com.yue.demo.service;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yue.demo.MainActivity;
import com.yue.demo.util.LogUtil;

public class BindServiceActivity extends Activity {
    private static final String tag = "BindServiceActivity";

    private Button bind, unbind, getServiceStatus, start;
    // 保持所启动的Service的IBinder对象
    private BindService.MyBinder binder;
    // 定义一个ServiceConnection对象
    private ServiceConnection conn = new ServiceConnection() {
        // 当该Activity与Service连接成功时回调该方法
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtil.d(tag, "--Service Connected--");
            // 获取Service的onBind方法所返回的MyBinder对象
            binder = (BindService.MyBinder) service; // ①
        }

        // 当该Activity与Service断开连接时回调该方法
        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.d(tag, "--Service Disconnected--");
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        // 创建启动Service的Intent
        final Intent intent = new Intent();
        // 为Intent设置Action属性
        intent.setAction("org.yue.mytest.service.BIND_SERVICE");
        bind.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View source) {
                // 绑定指定Serivce
                bindService(intent, conn, Service.BIND_AUTO_CREATE);
                unbind.setEnabled(true);
            }
        });
        unbind.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View source) {
                // 解除绑定Serivce
                unbindService(conn);
                unbind.setEnabled(false);
            }
        });
        getServiceStatus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View source) {
                // 获取、并显示Service的count值
                Toast.makeText(BindServiceActivity.this,
                        "Serivce的count值为：" + binder.getCount(),
                        Toast.LENGTH_SHORT).show(); // ②
            }
        });

        start.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startService(intent);
            }
        });
    }

    private void initView() {
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(MainActivity.layoutParams_mm);
        ll.setGravity(Gravity.CENTER_HORIZONTAL);
        ll.setOrientation(LinearLayout.VERTICAL);

        // 程序界面中的start、stop、getServiceStatus按钮
        bind = new Button(this);
        bind.setLayoutParams(MainActivity.layoutParams_mw);
        bind.setText("绑定");
        ll.addView(bind);

        unbind = new Button(this);
        unbind.setLayoutParams(MainActivity.layoutParams_mw);
        unbind.setText("解除绑定");
        unbind.setEnabled(false);
        ll.addView(unbind);

        getServiceStatus = new Button(this);
        getServiceStatus.setLayoutParams(MainActivity.layoutParams_mw);
        getServiceStatus.setText("获取Service状态");
        ll.addView(getServiceStatus);

        start = new Button(this);
        start.setLayoutParams(MainActivity.layoutParams_mw);
        start.setText("通过start启动service");
        ll.addView(start);

        setContentView(ll);
    }
}