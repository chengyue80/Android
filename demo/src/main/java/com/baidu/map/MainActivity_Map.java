package com.baidu.map;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.yue.demo.R;

public class MainActivity_Map extends Activity {

	MapView mMapView = null;
	BaiduMap mBaiduMap = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_main_map);

		mMapView = (MapView) findViewById(R.id.bmapView);

		mBaiduMap = mMapView.getMap();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.addSubMenu(0, BaiduMap.MAP_TYPE_NORMAL, 0, "普通地图");
		menu.addSubMenu(0, BaiduMap.MAP_TYPE_SATELLITE, 0, "卫星地图");
		// SubMenu mapType = menu.add(0, 1, 0, "普通地图");
		// SubMenu mapType = menu.add(0, 1, 0, "卫星地图");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch (item.getItemId()) {
		case BaiduMap.MAP_TYPE_NORMAL:
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			break;
		case BaiduMap.MAP_TYPE_SATELLITE:
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			break;

		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

}
