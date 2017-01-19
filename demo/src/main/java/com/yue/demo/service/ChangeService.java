package com.yue.demo.service;

import java.io.IOException;

import android.app.Service;
import android.app.WallpaperManager;
import android.content.Intent;
import android.os.IBinder;

import com.yue.demo.R;

public class ChangeService extends Service {

    int[] wallpapers = { R.drawable.shuangta, R.drawable.lijiang,
            R.drawable.qiao, R.drawable.shui };

    WallpaperManager wManager;
    int current = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        wManager = WallpaperManager.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (current >= 4) {
            current = 0;
        }

        try {
            wManager.setResource(wallpapers[current++]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }
}
