package com.yue.demo.service;

import android.app.IntentService;
import android.content.Intent;

import com.yue.demo.util.LogUtil;

/**
 * IntentService
 * 
 * @author chengyue
 * 
 */
public class MyIntentService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MyIntentService(String name) {
        super(name);
    }

    // IntentService会使用单独的线程来执行该方法的代码
    @Override
    protected void onHandleIntent(Intent intent) {

        long endTime = System.currentTimeMillis() + 20 * 1000;
        LogUtil.d(IntentServiceTest.tag, "----MyIntentService onStart----");
        while (endTime > System.currentTimeMillis()) {

            synchronized (this) {

                try {
                    wait(endTime - System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
        LogUtil.d(IntentServiceTest.tag, "----MyIntentService end 耗时任务执行完成----");
    }

}
