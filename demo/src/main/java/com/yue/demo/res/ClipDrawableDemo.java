package com.yue.demo.res;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yue.demo.R;

public class ClipDrawableDemo extends Activity {
	/** Called when the activity is first created. */

	private ImageView imageView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ImageView imageview = (ImageView) findViewById(R.id.imageView1);
		// 获取图片所显示的ClipDrawble对象

		final ClipDrawable drawable = new ClipDrawable(imageview.getDrawable(),
				Gravity.TOP, ClipDrawable.VERTICAL);
		// final ClipDrawable drawable = (ClipDrawable) imageview.getDrawable();
		imageview.setImageDrawable(drawable);
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 0x1233) {
					// 修改ClipDrawable的level值
					drawable.setLevel(drawable.getLevel() + 1000);
				}
			}
		};
		final Timer timer = new Timer();

		timer.schedule(new TimerTask() {
			public void run() {
				Message msg = new Message();
				msg.what = 0x1233;
				// 发送消息,通知应用修改ClipDrawable对象的level值
				handler.sendMessage(msg);
				// 取消定时器
				if (drawable.getLevel() >= 10000) {
					timer.cancel();
				}
			}
		}, 0, 300);
	}
}
