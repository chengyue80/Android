package com.yue.demo.other;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.yue.demo.MainActivity;

public class ClearDataDemo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout ll = new LinearLayout(this);
		ll.setLayoutParams(MainActivity.layoutParams_mm);
		ll.setOrientation(LinearLayout.VERTICAL);

		setContentView(ll);

		Button button = new Button(this);
		button.setLayoutParams(MainActivity.layoutParams_mw);
		button.setText("清除数据");
		ll.addView(button);

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				RuntimeCmdManager.clearAppUserData("com.yue.android.demo");
			}
		});
	}
}
