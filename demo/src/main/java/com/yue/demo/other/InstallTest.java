package com.yue.demo.other;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.yue.demo.util.LogUtil;

public class InstallTest extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        ll.setGravity(Gravity.CENTER);
        ll.setOrientation(LinearLayout.VERTICAL);

        Button install = new Button(this);
        install.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        install.setText("安装");
        ll.addView(install);

        Button unInstall = new Button(this);
        unInstall.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        unInstall.setText("卸载");
        ll.addView(unInstall);

        setContentView(ll);
        install.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                install();
            }
        });

        unInstall.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                uninstall();
            }
        });
    }

    /**
     * 安装应用
     */
    public void install() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);

        LogUtil.d(MainActivity_other.Tag + "dir : "
                + Environment.getExternalStorageDirectory());
        File file = new File(Environment.getExternalStorageDirectory(),
                "dididache_43.apk");

        if (!file.exists()) {
            LogUtil.d(MainActivity_other.Tag + "apk file is not exist ！");
            return;
        }
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }

    /**
     * 卸载应用
     */
    public void uninstall() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:com.su.didi.psger"));
        startActivity(intent);
    }

}
