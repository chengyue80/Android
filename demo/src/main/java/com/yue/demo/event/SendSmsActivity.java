package com.yue.demo.event;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.yue.demo.util.LogUtil;

/**
 * 使用OnLongClickListener监听器实现短信的发送功能
 * 
 * @author chengyue
 * 
 */
public class SendSmsActivity extends Activity {

    private EditText address;
    private EditText content;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        ll.setGravity(Gravity.FILL_HORIZONTAL);
        ll.setOrientation(LinearLayout.VERTICAL);

        address = new EditText(this);
        address.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        address.setHint("请填写收信号码");
        ll.addView(address);

        content = new EditText(this);
        content.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        content.setHint("请填写短信内容");
        ll.addView(content);

        send = new Button(this);
        send.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        send.setText("发送");
        ll.addView(send);
        setContentView(ll);

        send.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                String addressStr = address.getText().toString();
                String contentStr = content.getText().toString();
                LogUtil.d(MainActivity_Event.Tag + "addressStr : " + addressStr
                        + "  contentStr : " + contentStr);
                // 获取短信管理器
                SmsManager sm = SmsManager.getDefault();
                // 创建发送短信的PendingIntent
                PendingIntent pi = PendingIntent.getBroadcast(
                        SendSmsActivity.this, 0, new Intent(), 0);
                // 发送文本短信
                sm.sendTextMessage(addressStr, null, contentStr, pi, null);
                Toast.makeText(SendSmsActivity.this, "短信发送完成",
                        Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }
}
