package com.baidu.demo;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.baidu.map.MainActivity_Map;

public class MainActivity_Baidu extends LauncherActivity {

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		String[] mActivity = { "百度地图", "百度定位" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mActivity);
		setListAdapter(adapter);
	}

	@Override
	protected Intent intentForPosition(int position) {

		switch (position) {
		case 0:
			return new Intent(this, MainActivity_Map.class);

		default:
			return super.intentForPosition(position);
		}
	}
}
