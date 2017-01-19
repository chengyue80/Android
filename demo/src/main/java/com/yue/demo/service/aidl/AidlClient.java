package com.yue.demo.service.aidl;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yue.demo.MainActivity;
import com.yue.demo.aidl.ICat;
import com.yue.demo.util.LogUtil;

public class AidlClient extends Activity {

    private final String tag = "AidlClient";
    private ICat catService;

    private Button button;
    private TextView color, weight;

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.d(tag, "AidlClient onServiceDisconnected");
            catService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtil.d(tag, "AidlClient onServiceConnected");
            // 获取远程Service的onBind方法返回的对象的代理
            catService = ICat.Stub.asInterface(service);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        Intent intent = new Intent("com.yue.demo.action.AIDL_SERVICE");
        bindService(intent, conn, Service.BIND_AUTO_CREATE);

        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 获取、并显示远程Service的状态
                try {
                    color.setText("color : " + catService.getColor());
                    weight.setText("weight : " + catService.getWeight() + "");
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);

    }

    private void initView() {
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(MainActivity.layoutParams_mm);
        ll.setOrientation(LinearLayout.VERTICAL);

        button = new Button(this);
        button.setLayoutParams(MainActivity.layoutParams_mw);
        button.setText("获取aidlservice的状态");
        ll.addView(button);

        color = new TextView(this);
        color.setLayoutParams(MainActivity.layoutParams_mw);
        color.setText("color");
        ll.addView(color);

        weight = new TextView(this);
        weight.setLayoutParams(MainActivity.layoutParams_mw);
        weight.setText("weight");
        ll.addView(weight);

        setContentView(ll);
    }
}
