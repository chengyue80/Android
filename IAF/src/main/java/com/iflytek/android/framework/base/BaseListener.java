/**
 * Copyright (c) 2014 All rights reserved
 * 名称：BaseListener.java
 * 描述：TODO
 * @author wzgao
 * @date：2014-3-25 下午3:13:20
 * @version v1.0
 */
package com.iflytek.android.framework.base;

/**
 * @author wzgao
 * 
 */
public class BaseListener {

	// activity实例
	public Object activity;
	// 注册的方法名
	public String methodName;
	// 注册的方法名
	public String selected;
	// 注册的方法名
	public String noSelected;
	// 多个实现方法
	public String[][] methods;

	public BaseListener(Object activity, String methodName) {
		this.activity = activity;
		this.methodName = methodName;
	}
	
	public BaseListener(Object activity, String[][] methodName) {
		this.activity = activity;
		this.methods = methodName;
		if(methodName != null && methodName.length == 1){
			this.methodName = methodName[0][1];
		}
	}

	public BaseListener(Object activity, String selected, String noSelected) {
		this.activity = activity;
		this.selected = selected;
		this.noSelected = noSelected;
	}

}
