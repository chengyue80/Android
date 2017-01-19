package com.yue.demo.io;

import java.util.HashSet;
import java.util.LinkedHashSet;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.yue.demo.other.MainActivity_other;
import com.yue.demo.util.LogUtil;

/**
 * 跨应用程序读取SharePreference信息
 * 
 * @author chengyue
 * 
 */
public class OtherSharePreferencetest extends Activity {

    private Button btn_share_read, btn_share_write, btn_linkset;
    private TextView tv_share;
    private EditText ed_share;

    private static Context otherAppContext;

    private SharedPreferences preferences, linkPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(Gravity.CENTER);

        btn_share_read = new Button(this);
        btn_share_read.setLayoutParams(new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        btn_share_read.setText("跨程序读取");
        ll.addView(btn_share_read);

        btn_share_write = new Button(this);
        btn_share_write.setLayoutParams(new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        btn_share_write.setText("跨程序写入");
        ll.addView(btn_share_write);

        tv_share = new TextView(this);
        tv_share.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        tv_share.setText("跨程序读取的信息");
        ll.addView(tv_share);

        ed_share = new EditText(this);
        ed_share.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        ed_share.setHint("跨程序写入的信息");
        ll.addView(ed_share);

        btn_linkset = new Button(this);
        btn_linkset.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        btn_linkset.setText("LinkedHashSet存储");
        ll.addView(btn_linkset);
        setContentView(ll);

        // 获取其他应用的Context

        try {
            otherAppContext = createPackageContext("org.yue.test",
                    CONTEXT_IGNORE_SECURITY);

            if (otherAppContext != null) {

                preferences = otherAppContext.getSharedPreferences("shareTest",
                        Context.MODE_WORLD_READABLE
                                | Context.MODE_WORLD_WRITEABLE);
            } else {
                LogUtil.d(MainActivity_IO.Tag + "otherAppContext is null");
                Toast.makeText(this, "将要读取的文件程序部存在", Toast.LENGTH_LONG).show();
            }

        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        linkPreferences = getSharedPreferences("LINK", 2 | 1);
        Editor editor = linkPreferences.edit();
        LinkedHashSet<String> set = new LinkedHashSet<String>();
        set.add("aaa");
        set.add("bbb");
        set.add("ccc");
        set.add("ddd");
        editor.putStringSet("set", (LinkedHashSet<String>) set);
        editor.commit();

        btn_share_read.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                tv_share.setText(preferences.getString("test", "读取失败！"));
            }
        });

        btn_share_write.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Editor editor = preferences.edit();
                editor.putString("test", ed_share.getText().toString());
                editor.commit();
            }
        });

        btn_linkset.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                SharedPreferences preferences = getSharedPreferences("LINK",
                        2 | 1);
                HashSet<String> hashSet = (HashSet<String>) preferences
                        .getStringSet("set", null);
                if (hashSet != null) {

                    for (String string : hashSet) {
                        LogUtil.d(MainActivity_other.Tag + string);
                    }
                } else {
                    LogUtil.d(MainActivity_other.Tag + "hashSet is  null");

                }
            }
        });

    }
}
