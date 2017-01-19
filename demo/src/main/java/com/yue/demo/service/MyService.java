package com.yue.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.yue.demo.util.LogUtil;

/**
 * 普通的Service
 * 
 * @author chengyue
 * 
 */
public class MyService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 该方法内执行耗时任务可能导致ANR（Application Not Responding）异常
        long endTime = System.currentTimeMillis() + 20 * 1000;
        LogUtil.d(IntentServiceTest.tag, "----MyService onStart----");
        while (endTime > System.currentTimeMillis()) {

            synchronized (this) {

                try {
                    wait(endTime - System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
        LogUtil.d(IntentServiceTest.tag, "----MyService end 耗时任务执行完成----");

        return START_STICKY;

    }

}
