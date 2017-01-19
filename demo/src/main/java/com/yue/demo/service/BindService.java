package com.yue.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.yue.demo.util.LogUtil;

/**
 * BindService : 绑定本地Service并与之通信
 * 
 * @author chengyue
 * 
 */
public class BindService extends Service {

    private static final String tag = "BindService";
    private int count;
    private boolean quit;
    // 定义onBinder方法所返回的对象
    private MyBinder binder = new MyBinder();

    // 通过继承Binder来实现IBinder类
    public class MyBinder extends Binder // ①
    {
        public int getCount() {
            // 获取Service的运行状态：count
            return count;
        }
    }

    // 必须实现的方法，绑定该Service时回调该方法
    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.d(tag, "Service is Binded");
        // 返回IBinder对象
        return binder;
    }

    // Service被创建时回调该方法。
    @Override
    public void onCreate() {
        LogUtil.d(tag, "Service is Created");
        super.onCreate();
        // 启动一条线程、动态地修改count状态值
        new Thread() {
            @Override
            public void run() {
                while (!quit) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    count++;
                }
            }
        }.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        LogUtil.d(tag, "Service is onStartCommand");
        return START_STICKY;
    }

    // Service被断开连接时回调该方法
    @Override
    public boolean onUnbind(Intent intent) {
        LogUtil.d(tag, "Service is Unbinded");
        return true;
    }

    // Service被重新绑定时回调该方法
    @Override
    public void onRebind(Intent intent) {
        LogUtil.d(tag, "Service is onRebind");
        super.onRebind(intent);
    }

    // Service被关闭之前回调该方法。
    @Override
    public void onDestroy() {
        LogUtil.d(tag, "Service is Destroyed");
        super.onDestroy();
        this.quit = true;
    }
}