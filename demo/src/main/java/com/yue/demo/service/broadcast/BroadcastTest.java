package com.yue.demo.service.broadcast;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yue.demo.MainActivity;
import com.yue.demo.util.LogUtil;

public class BroadcastTest extends Activity {

    private Button send, sendOrder;

    BroadcastReceiver myReceive2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle = getResultExtras(true);
            String first = bundle.getString("first");
            Toast.makeText(context, "第一个接受者存入的消息 ： " + first,
                    Toast.LENGTH_SHORT).show();
            LogUtil.d("", "第一个接受者存入的消息 ： " + first);
        }
    };

    IntentFilter filter = new IntentFilter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        filter.addAction("org.yue.test.broadcast.action.MYRECEIVE");
        filter.setPriority(30);
        registerReceiver(myReceive2, filter);

        send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setAction("org.yue.test.broadcast.action.MYRECEIVE");
                intent.putExtra("msg", "简单的消息。。。");
                sendBroadcast(intent);
            }
        });

        sendOrder.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("org.yue.test.broadcast.action.MYRECEIVE");
                intent.putExtra("msg", "简单的消息。。。");
                sendOrderedBroadcast(intent, null);
            }
        });

    }

    private void initView() {
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(MainActivity.layoutParams_mm);
        ll.setOrientation(LinearLayout.VERTICAL);

        send = new Button(this);
        send.setLayoutParams(MainActivity.layoutParams_mw);
        send.setText("发送广播");
        ll.addView(send);

        sendOrder = new Button(this);
        sendOrder.setLayoutParams(MainActivity.layoutParams_mw);
        sendOrder.setText("发送有序广播");
        ll.addView(sendOrder);

        setContentView(ll);
    }

    @Override
    protected void onDestroy() {

        unregisterReceiver(myReceive2);
        super.onDestroy();
    }

}
