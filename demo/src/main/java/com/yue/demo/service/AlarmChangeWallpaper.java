package com.yue.demo.service;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yue.demo.MainActivity;

/**
 * 定时更换壁纸
 * 
 * @author chengyue
 * 
 */
public class AlarmChangeWallpaper extends Activity {

    private AlarmManager aManager;
    private Button start, stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        aManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);

        Intent intent = new Intent(AlarmChangeWallpaper.this,
                ChangeService.class);

        final PendingIntent pi = PendingIntent.getService(
                AlarmChangeWallpaper.this, 0, intent, 0);
        start.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                aManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0,
                        5000, pi);
                start.setEnabled(false);
                stop.setEnabled(true);
                Toast.makeText(AlarmChangeWallpaper.this, "壁纸定时更换启动成功！",
                        Toast.LENGTH_SHORT).show();
            }
        });
        stop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                start.setEnabled(true);
                stop.setEnabled(false);
                aManager.cancel(pi);
            }
        });
    }

    private void initView() {
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(MainActivity.layoutParams_mm);
        ll.setOrientation(LinearLayout.VERTICAL);

        start = new Button(this);
        start.setLayoutParams(MainActivity.layoutParams_mw);
        start.setText("开始");
        ll.addView(start);

        stop = new Button(this);
        stop.setLayoutParams(MainActivity.layoutParams_mw);
        stop.setText("停止");
        ll.addView(stop);

        setContentView(ll);
    }
}
