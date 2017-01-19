package com.yue.demo.net;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.yue.demo.net.multidown.MultiThreadDown;
import com.yue.demo.net.weather.MyWeather;
import com.yue.demo.net.weather.WeatherActivity;
import com.yue.demo.util.LogUtil;
import com.yue.demo.util.NetWorkUtil;

public class MainActivity_Net extends LauncherActivity {

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		String[] mActivity = { "SimpleClient", "MultiThreadClient", "URLTest",
				"GetPostMain", "HttpClientTest", "MultiThreadDown", "ViewHtml",
				"JsCallAndroid", "CallWs", "MyWeather", "systemDownload",
				"MyWeatherView" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mActivity);
		setListAdapter(adapter);

		int type = NetWorkUtil.getAPNType(getApplicationContext());
		LogUtil.d("===", "type : " + type);
	}

	@Override
	protected Intent intentForPosition(int position) {

		switch (position) {
		case 0:
			return new Intent(this, SimpleClient.class);

		case 1:
			return new Intent(this, MultiThreadClient.class);

		case 2:
			return new Intent(this, URLTest.class);

		case 3:
			return new Intent(this, GetPostMain.class);

		case 4:
			return new Intent(this, HttpClientTest.class);

		case 5:
			return new Intent(this, MultiThreadDown.class);

		case 6:
			return new Intent(this, ViewHtml.class);

		case 7:
			return new Intent(this, JsCallAndroid.class);

		case 8:
			return new Intent(this, CallWs.class);

		case 9:
			return new Intent(this, MyWeather.class);

		case 10:
			return new Intent(this, SyetemDownlaod.class);
		case 11:
			return new Intent(this, WeatherActivity.class);

		default:
			return super.intentForPosition(position);
		}
	}
}
