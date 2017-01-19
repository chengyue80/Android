package com.yue.demo.service.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.yue.demo.aidl.ICat;

import java.util.Timer;
import java.util.TimerTask;

public class AidlService extends Service {

    private final String tag = "AidlService";

    private CatIBinder catIBinder;
    private Timer timer = new Timer();

    private String[] colors = { "黑色", "黄色", "白色" };
    private double[] weights = { 1.5, 2.0, 2.5 };

    private String color;
    private double weight;

    // 继承Stub，也就是实现额ICat接口，并实现了IBinder接口

    public class CatIBinder extends ICat.Stub {

        @Override
        public String getColor() throws RemoteException {
            return color;
        }

        @Override
        public double getWeight() throws RemoteException {
            return weight;
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        /*
         * 返回catBinder对象 在绑定本地Service的情况下，该catBinder对象会直接
         * 传给客户端的ServiceConnection对象 的onServiceConnected方法的第二个参数；
         * 在绑定远程Service的情况下，只将catBinder对象的代理 传给客户端的ServiceConnection对象
         * 的onServiceConnected方法的第二个参数；
         */
        return catIBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        catIBinder = new CatIBinder();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                // 随机地改变Service组件内color、weight属性的值。

                int rand = (int) (Math.random() * 3);
                color = colors[rand];
                weight = weights[rand];

            }
        }, 0, 800);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
 }
