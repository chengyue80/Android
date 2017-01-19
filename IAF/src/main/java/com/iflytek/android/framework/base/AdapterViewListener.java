/**
 * Copyright (c) 2014 All rights reserved
 * ��ƣ�AdapterViewListener.java
 * ������TODO
 * @author wzgao
 * @date��2014-3-26 ����2:29:48
 * @version v1.0
 */
package com.iflytek.android.framework.base;

import com.iflytek.android.framework.util.ClassUtils;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

/**
 * @author wzgao
 * 
 */
public class AdapterViewListener extends BaseListener implements
		AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener,
		AdapterView.OnItemSelectedListener, AbsListView.OnScrollListener {

	/**
	 * @param activity
	 * @param methodName
	 */
	public AdapterViewListener(Object activity, String methodName) {
		super(activity, methodName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param activity
	 * @param methodName
	 */
	public AdapterViewListener(Object activity, String[][] methodName) {
		super(activity, methodName);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param activity
	 * @param selected
	 * @param noSelected
	 */
	public AdapterViewListener(Object activity, String selected,
			String noSelected) {
		super(activity, selected, noSelected);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android
	 * .widget.AdapterView, android.view.View, int, long)
	 */
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		if(methods != null && methods.length > 0){
			for(int i = 0; i < methods.length; i++){
				if("onItemSelected".equals(methods[i][0])){
					// TODO Auto-generated method stub
					ClassUtils.invokeClickMethod(activity, methods[i][1], ClassUtils.getClass(
							AdapterView.class, View.class, int.class, long.class), arg0,
							arg1, arg2, arg3);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android
	 * .widget.AdapterView)
	 */
	public void onNothingSelected(AdapterView<?> arg0) {
		if(methods != null && methods.length > 0){
			for(int i = 0; i < methods.length; i++){
				if("onNothingSelected".equals(methods[i][0])){
					// TODO Auto-generated method stub
					ClassUtils.invokeClickMethod(activity, methods[i][1],
							ClassUtils.getClass(AdapterView.class), arg0);
				}
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemLongClickListener#onItemLongClick(android
	 * .widget.AdapterView, android.view.View, int, long)
	 */
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		return ClassUtils.invokeClickMethod(activity, methodName,
				ClassUtils.getClass(AdapterView.class, View.class, int.class,
						long.class), parent, view, position, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
	 * .AdapterView, android.view.View, int, long)
	 */
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		ClassUtils.invokeClickMethod(activity, methodName, ClassUtils.getClass(
				AdapterView.class, View.class, int.class, long.class), parent,
				view, position, id);
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(methods != null && methods.length > 0){
			for(int i = 0; i < methods.length; i++){
				if("onScrollStateChanged".equals(methods[i][0])){
					// TODO Auto-generated method stub
					ClassUtils.invokeClickMethod(activity,
							methods[i][1],
							ClassUtils.getClass(AbsListView.class, int.class),
							view, scrollState);
				}
			}
		}
	}

	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if(methods != null && methods.length > 0){
			for(int i = 0; i < methods.length; i++){
				if("onScroll".equals(methods[i][0])){
					ClassUtils.invokeClickMethod(activity, methods[i][1], ClassUtils.getClass(
							AbsListView.class, int.class, int.class, int.class), view,
							firstVisibleItem, visibleItemCount, totalItemCount);
				}
			}
		}
	}

}
