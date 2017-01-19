package com.yue.demo.desktop;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yue.demo.R;
import com.yue.demo.MainActivity;
import com.yue.demo.util.LogUtil;

public class AddShortCut extends Activity {

	private ImageView flower;
	Animation anim, reverse;

	private Button shortCut;

	private final Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {

			if (msg.what == 0x123) {
				flower.startAnimation(reverse);
			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		// 加载动画资源
		anim = AnimationUtils.loadAnimation(this, R.anim.anim);
		anim.setFillAfter(true);
		reverse = AnimationUtils.loadAnimation(this, R.anim.reverse);
		reverse.setFillAfter(true);

		shortCut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
				String title = getResources().getString(R.string.app_name);
				Toast.makeText(AddShortCut.this, "tilte : " + title,
						Toast.LENGTH_LONG).show();
				LogUtil.d("AddShortCut", "title : " + title);
				// 加载快捷方式图标
				Parcelable icon = Intent.ShortcutIconResource.fromContext(
						AddShortCut.this, R.drawable.aaa);

				Intent myIntent = new Intent(AddShortCut.this,
						MainActivity.class);
				// 设置快捷方式标题
				intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
				// 设置快捷方式图标
				intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
				// 设置快捷方式对应的intent
				intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, myIntent);
				sendBroadcast(intent);
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		flower.startAnimation(anim);

		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				mHandler.sendEmptyMessage(0x123);
			}
		}, 3500);
	}

	private void initView() {
		LinearLayout ll = new LinearLayout(this);
		ll.setLayoutParams(MainActivity.layoutParams_mm);
		ll.setOrientation(LinearLayout.VERTICAL);

		shortCut = new Button(this);
		shortCut.setLayoutParams(MainActivity.layoutParams_weight);
		shortCut.setText("添加快捷键");
		ll.addView(shortCut);

		flower = new ImageView(this);
		flower.setLayoutParams(MainActivity.layoutParams_ww);
		flower.setScaleType(ScaleType.FIT_CENTER);
		flower.setImageResource(R.drawable.flower);
		ll.addView(flower);

		setContentView(ll);
	}
}
