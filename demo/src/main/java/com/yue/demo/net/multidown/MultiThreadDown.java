package com.yue.demo.net.multidown;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.yue.demo.MainActivity;
import com.yue.demo.util.LogUtil;

/**
 * 多线程下载
 * 
 * @author chengyue
 * 
 */
public class MultiThreadDown extends Activity {

	final static String tag = MultiThreadDown.class.getSimpleName();

	private EditText address, target;
	private Button down;
	private ProgressBar bar;
	private String path = "http://www.crazyit.org/attachment.php?aid=MTA5M3w5OTUyNGEzNHwxMzUyODIzNDM0fDU0MDQ2MHNzd1p5Y25KVkFDYXYzUS9uMEpOUnBKRjFCbFpkTldleWpRYzZUbzVV";

	DownLoadUtil downLoadUtil;
	int mDownStatus = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0x123) {
					bar.setProgress(mDownStatus);
				}
			}
		};
		down.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 初始化DownUtil对象（最后一个参数指定线程数）
				downLoadUtil = new DownLoadUtil(address.getText().toString(),
						target.getText().toString(), 6);
				new Thread() {
					@Override
					public void run() {
						LogUtil.d(tag, "====thread start====");
						try {
							// 开始下载
							downLoadUtil.download();
						} catch (Exception e) {
							e.printStackTrace();
						}
						// 定义每秒调度获取一次系统的完成进度
						final Timer timer = new Timer();
						timer.schedule(new TimerTask() {
							@Override
							public void run() {
								// 获取下载任务的完成比率
								double completeRate = downLoadUtil
										.getCompleteRate();
								mDownStatus = (int) (completeRate * 100);
								// 发送消息通知界面更新进度条
								handler.sendEmptyMessage(0x123);
								// 下载完全后取消任务调度
								if (mDownStatus >= 100) {
									timer.cancel();
								}
							}
						}, 0, 100);
					}
				}.start();

			}
		});
	}

	@SuppressLint("NewApi")
	private void initView() {
		LinearLayout ll = new LinearLayout(this);
		ll.setLayoutParams(MainActivity.layoutParams_mm);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setGravity(Gravity.CENTER_HORIZONTAL);

		address = new EditText(this);
		address.setLayoutParams(MainActivity.layoutParams_mw);
		address.setHint("请输入网络资源的地址...");
		address.setText(path);
		ll.addView(address);

		target = new EditText(this);
		target.setLayoutParams(MainActivity.layoutParams_ww);
		target.setText("/mnt/sdcard/a.rar");
		ll.addView(target);

		down = new Button(this);
		down.setLayoutParams(MainActivity.layoutParams_ww);
		down.setText("下载");
		ll.addView(down);

		bar = new ProgressBar(this, null,
				android.R.attr.progressBarStyleHorizontal);
		bar.setLayoutParams(MainActivity.layoutParams_mw);
		bar.setMax(100);
		ll.addView(bar);

		setContentView(ll);
	}
}
