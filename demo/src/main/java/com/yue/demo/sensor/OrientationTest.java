package com.yue.demo.sensor;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.yue.demo.R;

public class OrientationTest extends Activity implements OnClickListener{
	
	private Button button1,button2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		button1 = (Button) findViewById(R.id.button1);
		button1.setText("横屏");
		button2 = (Button) findViewById(R.id.button2);
		button1.setText("竖屏");
		
//		setRequestedOrientation(ActivityInfo.scr);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.button1:
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			break;
		case R.id.button2:
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			
			break;

		default:
			break;
		}
	}

}
