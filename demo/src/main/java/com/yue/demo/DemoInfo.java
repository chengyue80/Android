package com.yue.demo;

import android.app.Activity;

public class DemoInfo {
	public int title;
	public int desc;
	public Class<? extends Activity> demoClass;

	public DemoInfo(int title, int desc, Class<? extends Activity> demoClass) {
		super();
		this.title = title;
		this.desc = desc;
		this.demoClass = demoClass;
	}
}
