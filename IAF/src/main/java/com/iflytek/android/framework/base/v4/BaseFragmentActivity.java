package com.iflytek.android.framework.base.v4;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import com.iflytek.android.framework.activity.ActivityTack;
import com.iflytek.android.framework.annotation.BaseInject;
import com.iflytek.android.framework.base.SingleRequestQueen;

public class BaseFragmentActivity extends FragmentActivity {
	private ActivityTack at;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.at = ActivityTack.getInstanse();
		if (!this.at.getActivity(this))
			this.at.addActivity(this);
	}

	protected void onResume() {
		super.onResume();
		BaseInject.getInstance().eventInject(this);
	}

	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		initBaseInject(this);
	}

	public void setContentView(View view, ViewGroup.LayoutParams params) {
		super.setContentView(view, params);
		initBaseInject(this);
	}

	public void setContentView(View view) {
		super.setContentView(view);
		initBaseInject(this);
	}

	protected void onStop() {
		super.onStop();
	}

	protected void onDestroy() {
		super.onDestroy();
		BaseInject.getInstance().unEventInject(this);
		SingleRequestQueen.getInstance(this).quitRequest(this);
		this.at.removeActivity(this);
	}

	public void initBaseInject(Activity activity) {
		initBaseInject(activity, activity.getWindow().getDecorView());
	}

	public void initBaseInject(Activity activity, View sourceView) {
		BaseInject.getInstance().initInject(activity);
	}
}