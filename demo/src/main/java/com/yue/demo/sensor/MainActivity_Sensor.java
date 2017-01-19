package com.yue.demo.sensor;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

/**
 * 传感器应用开发
 * 
 * @author chengyue
 * 
 */
public class MainActivity_Sensor extends LauncherActivity {

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		String[] mActivity = { "AccelerometerTest", "传感器模拟器", "指南针", "水平仪" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mActivity);
		setListAdapter(adapter);
	}

	@Override
	protected Intent intentForPosition(int position) {

		switch (position) {
		case 0:
			return new Intent(this, AccelerometerTest.class);

		case 1:
			return new Intent(this, SensorTest.class);

		case 2:
			return new Intent(this, Compass.class);

		case 3:
			return new Intent(this, Gradienter.class);

		default:
			return super.intentForPosition(position);
		}
	}
}
