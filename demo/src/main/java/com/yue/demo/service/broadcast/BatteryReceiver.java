package com.yue.demo.service.broadcast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.yue.demo.util.LogUtil;

public class BatteryReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        LogUtil.d("BatteryReceiver", "intent : " + Uri.decode(intent.toUri(0)));

        Bundle bundle = intent.getExtras();

        int current = bundle.getInt("level");

        int total = bundle.getInt("scale");

        Toast.makeText(context, "剩余电量 : " + current + "\n总电量：" + total,
                Toast.LENGTH_SHORT).show();
        if (current * 1.0 / total < 0.95) {

            Toast.makeText(context, "电量过低，请尽快充电！", Toast.LENGTH_SHORT).show();

            // Intent newIntent = new Intent(Intent.ACTION_SHUTDOWN);
            // newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // PendingIntent pi = PendingIntent.getActivity(context, 0,
            // newIntent,
            // 0);

            // 获得ServiceManager类
            try {
                Class<?> ServiceManager = Class
                        .forName("android.os.ServiceManager");
                // 获得ServiceManager的getService方法
                Method getService = ServiceManager.getMethod("getService",
                        java.lang.String.class);
                // 调用getService获取RemoteService
                Object oRemoteService = getService.invoke(null,
                        Context.POWER_SERVICE);

                // 获得IPowerManager.Stub类
                Class<?> cStub = Class.forName("android.os.IPowerManager$Stub");
                // 获得asInterface方法
                Method asInterface = cStub.getMethod("asInterface",
                        android.os.IBinder.class);
                // 调用asInterface方法获取IPowerManager对象
                Object oIPowerManager = asInterface
                        .invoke(null, oRemoteService);
                // 获得shutdown()方法
                Method shutdown = oIPowerManager.getClass().getMethod(
                        "shutdown", boolean.class, boolean.class);
                // 调用shutdown()方法
                // shutdown.invoke(oIPowerManager, false, true);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }
    }

}
