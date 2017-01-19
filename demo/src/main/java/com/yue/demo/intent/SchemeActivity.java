package com.yue.demo.intent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 通过不同的data设置启动的Activity
 * 
 * @author chengyue
 * @version 1.0
 */
public class SchemeActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        Intent intent = getIntent();
        tv.setText("data : " + intent.getData());
        tv.setTextSize(30);
        setContentView(tv);
    }
}
