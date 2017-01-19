package com.yue.demo.other;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class TopActvityTest extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(Gravity.CENTER_HORIZONTAL);

        Button sent = new Button(this);
        sent.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        sent.setText("top");
        ll.addView(sent);

        TextView textView = new TextView(this);
        textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        textView.setPadding(36, 0, 0, 0);
        textView.setText("top:");
        textView.setTextSize(20);
        ll.addView(textView);
        setContentView(ll);

        sent.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // ActivityManager am = (ActivityManager)
                // getSystemService(Context.ACTIVITY_SERVICE);
                // ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
                //
                // Mlog.d(MainActivity_other.Tag + "packageName : "
                // + cn.getPackageName() + "  classname : "
                // + cn.getClassName());
                // Intent intent = new Intent();
                // intent.setAction("com.booslink.xiri.adapter.CALL");
                // intent.putExtra("action", "CHANGECHANNELBYNUM");
                // intent.putExtra("num", String.valueOf(15));// 传入将要切换的频道号
                // sendBroadcast(intent);
                Intent intent = new Intent();
                intent.setAction("com.starcor.hunan.mgtv");
                intent.putExtra("cmd_ex", "show_search");
                sendBroadcast(intent);
            }
        });
    }

}
