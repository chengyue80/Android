/**
 * Copyright (c) 2014 All rights reserved
 * 名称：ExceptionHandler.java
 * 描述：TODO
 * @author wzgao
 * @date：2014-3-26 下午4:15:04
 * @version v1.0
 */
package com.iflytek.android.exception;

import java.lang.Thread.UncaughtExceptionHandler;

import com.iflytek.android.framework.activity.ActivityTack;
import com.iflytek.android.framework.util.FileUtils;
import com.iflytek.android.framework.util.StringUtils;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * @author wzgao
 * 
 */
public class ExceptionHandler implements UncaughtExceptionHandler {

	/** ExceptionHandler实例 */
	private static ExceptionHandler INSTANCE = null;

	/** 程序的Context对象 */
	private Context mContext;

	/** 系统默认的UncaughtException处理类 */
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	/**
	 * 是否开启异常日志
	 */
	private boolean isLogOpen;
	/** 日志存储路径 */
	private String path;

	private ExceptionHandler() {
	}

	/** 获取ExceptionHandler实例 */
	public static ExceptionHandler getInstance() {
		if (INSTANCE == null) {
			synchronized (INSTANCE) {
				if (INSTANCE == null) {
					INSTANCE = new ExceptionHandler();
				}
			}
		}
		return INSTANCE;
	}

	/**
	 * init: 初始化数据<br/>
	 * 
	 * @author chengyue
	 * @param ctx
	 * @param isLogOpen
	 *            是否开启异常日志
	 * @param path
	 *            日志的本地存储路径
	 * @since JDK 1.6
	 */
	public void init(Context ctx, boolean isLogOpen, String path) {
		mContext = ctx;
		this.isLogOpen = isLogOpen;
		this.path = path;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// TODO Auto-generated method stub
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			android.os.Process.killProcess(android.os.Process.myPid());
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			// Sleep一会后结束程序
			// 来让线程停止一会是为了显示Toast信息给用户，然后Kill程序
			try {
				Thread.sleep(3000);

			} catch (InterruptedException e) {

			}
			ActivityTack.getInstanse().exit();
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return true;
		}
		final String msg = Log.getStackTraceString(ex);
		// 使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				// Toast 显示需要出现在一个线程的消息队列中
				Looper.prepare();
				// 异常进行处理，此处只是提示用户未做其他对应处理
				Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG)
						.show();
				// 根据设置的路径输入错误日志
				if (isLogOpen && StringUtils.isNotBlank(path)) {
					FileUtils.writeFile(path, msg, true);
				}
				// 错误录入异常日志
				// Log.e("Error"+mContext.getPackageName(), msg);
				Looper.loop();
			}
		}.start();

		return true;
	}

}
