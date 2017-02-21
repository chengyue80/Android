/**
 * Copyright (c) 2014 All rights reserved
 * 名称：BaseActivity.java
 * 描述：TODO
 * @author wzgao
 * @date：2014-3-25 下午2:06:40
 * @version v1.0
 */
package com.iflytek.android.framework.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.iflytek.android.framework.activity.ActivityTack;
import com.iflytek.android.framework.annotation.BaseInject;

/**
 * @author wzgao
 * 
 */
public abstract class BaseActivity extends Activity {

	private ActivityTack at;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.at = ActivityTack.getInstanse();
		if (!this.at.getActivity(this))
			this.at.addActivity(this);
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
		super.onDestroy();
		// 事件解绑
		BaseInject.getInstance().unEventInject(this);
		SingleRequestQueen.getInstance(this).quitRequest(this);
		at.removeActivity(this);
	}

	public void initBaseInject(Activity activity) {
		initBaseInject(activity, activity.getWindow().getDecorView());
	}

	public void initBaseInject(Activity activity, View sourceView) {
		BaseInject.getInstance().initInject(activity);
	}

}
