package com.yue.demo.service;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.yue.demo.R;

public class AllService extends Service {

	private Handler hanlder = new Handler();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {

		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("提示");
		builder.setMessage("该下车了");
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		builder.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		View view = LayoutInflater.from(this).inflate(R.layout.clock, null);
		final AlertDialog dialog = builder.create();
		// 在dialog show方法之前添加如下代码，表示该dialog是一个系统的dialog
		dialog.setView(view);
		dialog.getWindow().setType(
				(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
		new Thread() {

			public void run() {
				SystemClock.sleep(4000);
				hanlder.post(new Runnable() {
					@Override
					public void run() {
						dialog.show();
					}
				});
			};
		}.start();
		return super.onStartCommand(intent, flags, startId);
	}

}
