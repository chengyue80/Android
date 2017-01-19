/**
 * Copyright (c) 2014 All rights reserved
 * 名称：ActivityTack.java
 * 描述：TODO
 * @author zlchen
 * @date：2014年4月1日 下午7:57:43
 * @version v1.0
 */
package com.iflytek.android.framework.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

/**
 * @author zlchen
 * 
 */
public class ActivityTack {

	// activity的list集合
	private List<Activity> activityList = new ArrayList<Activity>();
	// Activity栈
	public static ActivityTack tack = new ActivityTack();

	public static ActivityTack getInstanse() {
		return tack;
	}

	/**
	 * 增加Activity 描述：TODO
	 * 
	 * @@param activity
	 * @throws
	 */
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	/**
	 * 从list中移除Activity 描述：TODO
	 * 
	 * @@param activity
	 * @throws
	 */
	public void removeActivity(Activity activity) {
		activityList.remove(activity);
	}

	public boolean getActivity(Activity activity) {
		boolean flage = false;
		for (Activity ac : activityList) {
			flage = ac.equals(activity);
		}
		return flage;
	}

	/**
	 * 销毁某个activity 描述：TODO
	 * 
	 * @@param activity
	 * @@return
	 * @throws
	 */
	public boolean killActivity(Activity activity) {

		if (getActivity(activity)) {
			removeActivity(activity);
			activity.finish();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 退出程序 描述：TODO @
	 * 
	 * @throws
	 */
	public void exit() {
		for (Activity ac : activityList) {
			ac.finish();
		}
		System.exit(0);
	}
	
	/**
	 * 退出程序 描述：TODO @
	 * 
	 * @throws
	 */
	public void quit() {
		for (Activity ac : activityList) {
			ac.finish();
		}
	}

}
