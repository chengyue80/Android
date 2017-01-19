package com.yue.demo.service.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.yue.demo.util.LogUtil;

public class MyReceive extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String msg = intent.getStringExtra("msg");
        Toast.makeText(context, "action : " + action + "\nmsg : " + msg,
                Toast.LENGTH_SHORT).show();

        LogUtil.d("Broadcast", "action : " + action + "\nmsg : " + msg);
        // 创建一个Bundle对象，并存入数据
        Bundle bundle = new Bundle();
        bundle.putString("first", "第一个receive存入的消息");
        setResultExtras(bundle);
        // 取消广播的传递
        // abortBroadcast();
    }

}
