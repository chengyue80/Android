package com.yue.demo.base;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;

import cn.sharesdk.framework.ShareSDK;

import com.iflytek.android.framework.base.BaseApplication;
import com.iflytek.cloud.SpeechUtility;
import com.yue.demo.R;
import com.yue.demo.util.LogUtil;

public class MyApplication extends BaseApplication {
	private static Context mContext;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this.getApplicationContext();
		// 初始化日志开关
		LogUtil.setDebugMode(true);

		// 初始化ShareSDK
		ShareSDK.initSDK(this);

		// 初始化语音 每个应用对应一个msc文件及appid
		SpeechUtility
				.createUtility(this, "appid=" + getString(R.string.app_id));
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	public static Context getAppContext() {
		return mContext;
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	/**
	 * 应用是否处于debug模式
	 * 
	 * @return
	 */
	public static boolean isDebugMode() {
		ApplicationInfo info = getAppContext().getApplicationInfo();
		return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
	}
	
	
}
