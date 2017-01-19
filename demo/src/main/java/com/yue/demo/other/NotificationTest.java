package com.yue.demo.other;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.yue.demo.R;

public class NotificationTest extends Activity {

	private final int NOTIFICATION_ID = 0x123;
	NotificationManager nm;
	TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout ll = new LinearLayout(this);
		ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setGravity(Gravity.CENTER_HORIZONTAL);

		Button sent = new Button(this);
		sent.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		sent.setText("发送Notification");
		ll.addView(sent);

		Button delete = new Button(this);
		delete.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		delete.setText("删除Notification");
		ll.addView(delete);
		textView = new TextView(this);
		textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		textView.setPadding(36, 0, 0, 0);
		textView.setText("通知消息：");
		textView.setTextSize(20);
		ll.addView(textView);
		setContentView(ll);

		getIntent();

		// 获取系统服务
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		sent.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), NotificationTest.class);
				intent.putExtra("info", "通知消息启动的Activity");
				PendingIntent pi = PendingIntent.getActivity(
						getApplicationContext(), 0, intent, 0);

				Notification.Builder builder = new Builder(
						getApplicationContext());
				// 设置打开该通知，该通知自动消失
				builder.setAutoCancel(false);
				// 设置显示在状态栏的通知提示信息
				builder.setTicker("有新消息");
				// 常驻通知栏
				builder.setOngoing(true);
				// 设置通知的图标
				builder.setSmallIcon(R.drawable.notify);
				// 设置通知内容的标题
				builder.setContentTitle("一条新通知");
				// 设置通知内容
				builder.setContentText("恭喜你，您加薪了，工资增加20%!");
				// 设置使用系统默认的声音、默认LED灯
				// .setDefaults(Notification.DEFAULT_SOUND
				// |Notification.DEFAULT_LIGHTS)
				// 设置通知的自定义声音
				builder.setSound(Uri.parse("android.resource://org.yue.mytest/"
						+ R.raw.msg));
				builder.setWhen(System.currentTimeMillis());
				// 设改通知将要启动程序的Intent
				builder.setContentIntent(pi);
				Notification notification = builder.build();
				// 发送通知
				nm.notify(NOTIFICATION_ID, notification);
				// 自定义声音
				// setSound(Uri.parse("file:///sdcard/click.mp3"));
				// 自定义震动
				// builder.setVibrate(new long[] {0,50,100,150});
			}
		});

		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				nm.cancel(NOTIFICATION_ID);
			}
		});
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent.getStringExtra("info") != null) {
			textView.setText(intent.getStringExtra("info").toString());
		}
	}

}
