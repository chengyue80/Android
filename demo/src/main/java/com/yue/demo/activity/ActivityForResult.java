package com.yue.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

/**
 * 测试带返回码的Activity，从该Activity返回数据
 * 
 * @author chengyue
 * 
 */
public class ActivityForResult extends Activity {
    private Button startActivity, code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(Gravity.CENTER);

        startActivity = new Button(this);
        startActivity.setLayoutParams(new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        startActivity.setText("启动对话框风格的Activity");
        ll.addView(startActivity);

        code = new Button(this);
        code.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        code.setText("启动对话框风格的Activity");
        ll.addView(startActivity);

        setContentView(ll);
        code.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 获取启动该Activity之前的Activity对应的Intent
                Intent intent = getIntent();
                intent.putExtra("city", "Activity返回结果");
                // 设置该Activity的结果码，并设置结束之后退回的Activity
                setResult(0, intent);
                // 结束Activity。
                finish();
            }
        });
    }
}
