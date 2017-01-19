/**
 * Copyright (c) 2014 All rights reserved
 * ��ƣ�ViewListener.java
 * ������TODO
 * @author wzgao
 * @date��2014-3-26 ����2:27:16
 * @version v1.0
 */
package com.iflytek.android.framework.base;

import android.annotation.SuppressLint;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.iflytek.android.framework.util.ClassUtils;

/**
 * @author wzgao
 * 
 */
@SuppressLint("NewApi")
public class ViewListener extends BaseListener implements View.OnClickListener,
		View.OnAttachStateChangeListener, View.OnCreateContextMenuListener,
		View.OnDragListener, View.OnFocusChangeListener,
		View.OnGenericMotionListener, View.OnHoverListener, View.OnKeyListener,
		View.OnLongClickListener, View.OnLayoutChangeListener,
		View.OnSystemUiVisibilityChangeListener, View.OnTouchListener {

	/**
	 * @param activity
	 * @param methodName
	 */
	public ViewListener(Object activity, String methodName) {
		super(activity, methodName);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param activity
	 * @param methodName
	 */
	public ViewListener(Object activity, String[][] methodName) {
		super(activity, methodName);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View,
	 * android.view.MotionEvent)
	 */
	public boolean onTouch(View v, MotionEvent event) {
		return ClassUtils.invokeClickMethod(super.activity, super.methodName,
				ClassUtils.getClass(View.class, MotionEvent.class), v, event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnSystemUiVisibilityChangeListener#
	 * onSystemUiVisibilityChange(int)
	 */
	
	public void onSystemUiVisibilityChange(int visibility) {
		// TODO Auto-generated method stub
		ClassUtils.invokeClickMethod(super.activity, super.methodName,
				ClassUtils.getClass(int.class), visibility);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnLongClickListener#onLongClick(android.view.View)
	 */
	
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		return ClassUtils.invokeClickMethod(super.activity, super.methodName,
				ClassUtils.getClass(View.class), v);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnKeyListener#onKey(android.view.View, int,
	 * android.view.KeyEvent)
	 */
	
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return ClassUtils.invokeClickMethod(super.activity, super.methodName,
				ClassUtils.getClass(View.class, int.class, KeyEvent.class), v,
				keyCode, event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnHoverListener#onHover(android.view.View,
	 * android.view.MotionEvent)
	 */
	
	public boolean onHover(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return ClassUtils.invokeClickMethod(super.activity, super.methodName,
				ClassUtils.getClass(View.class, MotionEvent.class), v, event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.View.OnGenericMotionListener#onGenericMotion(android.view
	 * .View, android.view.MotionEvent)
	 */
	
	public boolean onGenericMotion(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return ClassUtils.invokeClickMethod(super.activity, super.methodName,
				ClassUtils.getClass(View.class, MotionEvent.class), v, event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.View.OnFocusChangeListener#onFocusChange(android.view.View,
	 * boolean)
	 */
	
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		ClassUtils.invokeClickMethod(super.activity, super.methodName,
				ClassUtils.getClass(View.class, boolean.class), v, hasFocus);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnDragListener#onDrag(android.view.View,
	 * android.view.DragEvent)
	 */
	@SuppressLint("NewApi")
	
	public boolean onDrag(View v, DragEvent event) {
		// TODO Auto-generated method stub
		return ClassUtils.invokeClickMethod(super.activity, super.methodName,
				ClassUtils.getClass(View.class, DragEvent.class), v, event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.View.OnCreateContextMenuListener#onCreateContextMenu(android
	 * .view.ContextMenu, android.view.View,
	 * android.view.ContextMenu.ContextMenuInfo)
	 */
	
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		ClassUtils.invokeClickMethod(super.activity, super.methodName,
				ClassUtils.getClass(ContextMenu.class, View.class,
						ContextMenuInfo.class), menu, v, menuInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.View.OnAttachStateChangeListener#onViewAttachedToWindow(
	 * android.view.View)
	 */
	
	public void onViewAttachedToWindow(View v) {
		if(methods != null && methods.length > 0){
			for(int i = 0; i < methods.length; i++){
				if("onViewAttachedToWindow".equals(methods[i][0])){
						// TODO Auto-generated method stub
						ClassUtils.invokeClickMethod(super.activity, methods[i][1],
								ClassUtils.getClass(View.class), v);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.View.OnAttachStateChangeListener#onViewDetachedFromWindow
	 * (android.view.View)
	 */
	
	public void onViewDetachedFromWindow(View v) {
		if(methods != null && methods.length > 0){
			for(int i = 0; i < methods.length; i++){
				if("onViewDetachedFromWindow".equals(methods[i][0])){
						// TODO Auto-generated method stub
						ClassUtils.invokeClickMethod(super.activity, methods[i][1],
								ClassUtils.getClass(View.class), v);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		ClassUtils.invokeClickMethod(super.activity, super.methodName,
				ClassUtils.getClass(View.class), v);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.View.OnLayoutChangeListener#onLayoutChange(android.view.
	 * View, int, int, int, int, int, int, int, int)
	 */
	
	public void onLayoutChange(View v, int left, int top, int right,
			int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
		// TODO Auto-generated method stub
		ClassUtils.invokeClickMethod(super.activity, super.methodName,
				ClassUtils.getClass(View.class, int.class, int.class,
						int.class, int.class, int.class, int.class, int.class,
						int.class), v, left, top, right, bottom, oldLeft,
				oldTop, oldRight, oldBottom);
	}

}
