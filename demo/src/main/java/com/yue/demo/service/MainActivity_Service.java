package com.yue.demo.service;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.yue.demo.service.aidl.AidlClient;
import com.yue.demo.service.aidl.ComplexClient;
import com.yue.demo.service.broadcast.BroadcastTest;

/**
 * Service 与 broadcastreceive
 * 
 * @author chengyue
 * 
 */
public class MainActivity_Service extends LauncherActivity {

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        String[] mActivity = { "BindServiceActivity", "IntentServiceTest",
                "AidlClient", "ComplexClient", "TelehonyStatus",
                "SmsManagerTest", "音频管理器、振动器", "手机闹钟服务",
                "AlarmChangeWallpaper", "广播测试", "播放器" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mActivity);
        setListAdapter(adapter);

    }

    @Override
    protected Intent intentForPosition(int position) {

        switch (position) {

        case 0:
            return new Intent(this, BindServiceActivity.class);
        case 1:
            return new Intent(this, IntentServiceTest.class);
        case 2:
            return new Intent(this, AidlClient.class);
        case 3:
            return new Intent(this, ComplexClient.class);
        case 4:
            return new Intent(this, TelehonyStatus.class);
        case 5:
            return new Intent(this, SmsManagerTest.class);
        case 6:
            return new Intent(this, AudioManagerTest.class);
        case 7:
            return new Intent(this, AlarmManagerTest.class);
        case 8:
            return new Intent(this, AlarmChangeWallpaper.class);
        case 9:
            return new Intent(this, BroadcastTest.class);
        case 10:
            return new Intent(this, MusicList.class);

        default:
            return super.intentForPosition(position);
        }

    }

}
