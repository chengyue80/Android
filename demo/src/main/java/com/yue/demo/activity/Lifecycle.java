package com.yue.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.yue.demo.util.LogUtil;

/**
 * ActivityForResult的启动<br/>
 * Activity的生命周期
 * 
 * @author chengyue
 * 
 */
public class Lifecycle extends Activity {
	Button finish, startActivity, code, button;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 输出日志
		LogUtil.d(MainActivity_Activity.Tag + "Lifecycle-------onCreate------");
		Toast.makeText(getApplicationContext(), "onCreate", Toast.LENGTH_SHORT)
				.show();
		LinearLayout ll = new LinearLayout(this);
		ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setGravity(Gravity.CENTER);

		startActivity = new Button(this);
		startActivity.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		startActivity.setText("启动对话框风格的Activity");
		ll.addView(startActivity);

		code = new Button(this);
		code.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		code.setText("带返回码");
		ll.addView(code);

		finish = new Button(this);
		finish.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		finish.setText("退出");
		
		button = new Button(this);
		button.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		button.setText("agin");
		ll.addView(button);
		
		setContentView(ll);

		// 为startActivity按钮绑定事件监听器
		startActivity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View source) {
				Intent intent = new Intent(Lifecycle.this,
						PreferenceActivityTest.class);
				startActivity(intent);
			}
		});
		// 为finish按钮绑定事件监听器
		finish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View source) {
				// 结束该Activity
				Lifecycle.this.finish();
			}
		});

		code.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(getApplicationContext(),
						ActivityForResult.class), 1);
			}
		});

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(Lifecycle.this, Lifecycle.class));
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();
		// 输出日志
		LogUtil.d(MainActivity_Activity.Tag + "Lifecycle-------onStart------");
		Toast.makeText(getApplicationContext(), "onStart", Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void onRestart() {
		super.onRestart();
		// 输出日志
		LogUtil.d(MainActivity_Activity.Tag + "Lifecycle-------onRestart------");
		Toast.makeText(getApplicationContext(), "onRestart", Toast.LENGTH_SHORT)
				.show();

	}

	@Override
	public void onResume() {
		super.onResume();
		// 输出日志
		LogUtil.d(MainActivity_Activity.Tag + "Lifecycle-------onResume------");
		Toast.makeText(getApplicationContext(), "onResume", Toast.LENGTH_SHORT)
				.show();

	}

	@Override
	public void onPause() {
		super.onPause();
		// 输出日志
		LogUtil.d(MainActivity_Activity.Tag + "Lifecycle-------onPause------");
		Toast.makeText(getApplicationContext(), "onPause", Toast.LENGTH_SHORT)
				.show();

	}

	@Override
	public void onStop() {
		super.onStop();
		// 输出日志
		LogUtil.d(MainActivity_Activity.Tag + "Lifecycle-------onStop------");
		Toast.makeText(getApplicationContext(), "onStop", Toast.LENGTH_SHORT)
				.show();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 输出日志
		LogUtil.d(MainActivity_Activity.Tag + "Lifecycle-------onDestroy------");
		Toast.makeText(getApplicationContext(), "onDestroy", Toast.LENGTH_SHORT)
				.show();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		// 输出日志
		LogUtil.d(MainActivity_Activity.Tag
				+ "Lifecycle-------onActivityResult------");

		LogUtil.d(MainActivity_Activity.Tag + "\nrequestCode: " + requestCode
				+ "\nresultCode:" + resultCode);
		if (requestCode == 1 && resultCode == 1) {
			Bundle data = intent.getExtras();
			// 取出Bundle中的数据
			String resultCity = data.getString("info");
			Toast.makeText(getApplicationContext(), "返回的结果信息为：" + resultCity,
					Toast.LENGTH_SHORT).show();
		}
	}
}