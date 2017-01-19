package com.yue.demo.service.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.yue.demo.util.LogUtil;

/**
 * 短信提醒
 * 
 * @author chengyue
 * 
 */
public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction()
                .equals("android.provider.Telephony.SMS_RECEIVED")) {
            Toast.makeText(context, "你有新短消息，请注意查收...", Toast.LENGTH_SHORT)
                    .show();

            StringBuffer sb = new StringBuffer();
            Bundle bundle = intent.getExtras();

            if (bundle != null) {
                // 获取所有接收到的短信

                Object[] pdus = (Object[]) bundle.get("pdus");
                // 构建短信对象array,并依据收到的对象长度来创建array的大小

                SmsMessage[] messages = new SmsMessage[pdus.length];

                for (int i = 0; i < messages.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                // 将送来的短信合并自定义信息于StringBuilder当中
                for (SmsMessage smsMessage : messages) {

                    sb.append("短信来源:");
                    // 获得接收短信的电话号码
                    sb.append(smsMessage.getDisplayOriginatingAddress());
                    sb.append("\n------短信内容------\n");
                    sb.append(smsMessage.getDisplayMessageBody());
                }

                Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG)
                        .show();

                LogUtil.d("SmsReceiver", sb.toString());
            }
        }

    }
}
