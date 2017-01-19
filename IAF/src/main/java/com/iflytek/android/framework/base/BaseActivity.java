/**
 * Copyright (c) 2014 All rights reserved
 * 名称：BaseActivity.java
 * 描述：TODO
 * @author wzgao
 * @date：2014-3-25 下午2:06:40
 * @version v1.0
 */
package com.iflytek.android.framework.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;

import com.iflytek.android.framework.activity.ActivityTack;
import com.iflytek.android.framework.annotation.BaseInject;

/**
 * @author wzgao
 * 
 */
public abstract class BaseActivity extends Activity {

	private ActivityTack at;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		onCreate(savedInstanceState, true);
	}

	/**
	 * @param savedInstanceState
	 * @param translucent
	 *            是否使用沉浸式状态栏
	 * @since JDK 1.6
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	protected void onCreate(Bundle savedInstanceState, boolean translucent) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d("BaseActivity", "translucent : " + translucent);
		if (translucent && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明导航栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		at = ActivityTack.getInstanse();
		if (!at.getActivity(this)) {
			at.addActivity(this);

		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		BaseInject.getInstance().eventInject(this);
	}

	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		initBaseInject(this);
	}

	public void setContentView(View view, LayoutParams params) {
		super.setContentView(view, params);
		initBaseInject(this);
	}

	public void setContentView(View view) {
		super.setContentView(view);
		initBaseInject(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// 事件解绑
		BaseInject.getInstance().unEventInject(this);
		at.removeActivity(this);
	}

	public void initBaseInject(Activity activity) {
		initBaseInject(activity, activity.getWindow().getDecorView());
	}

	public void initBaseInject(Activity activity, View sourceView) {
		BaseInject.getInstance().initInject(activity);
	}

}
