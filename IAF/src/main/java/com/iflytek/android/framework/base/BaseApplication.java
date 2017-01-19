/**
 * 
 */
package com.iflytek.android.framework.base;

import java.util.HashMap;

import android.app.Application;
import android.content.Context;

import com.iflytek.android.exception.ExceptionHandler;

/**
 * @author wzgao
 * 
 */
public class BaseApplication extends Application {

	/**
	 * 缓冲
	 */
	HashMap<String, String> dataCache;
	/**
	 * 全局容器
	 */
	public static Context iafContext;

	private ExceptionHandler exceptionHandler;

	/**
	 * Volley请求队列
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		dataCache = new HashMap<String, String>();
		iafContext = getApplicationContext();
	}

	/**
	 * TODO 开启全局异常
	 * 
	 * @param logPath
	 *            日志存储路径
	 * @throw
	 * @return void
	 * @change 2016-03-18 增加动态配置日志输出路径logPath
	 */
	public void openException(boolean logError, String logPath) {
		this.exceptionHandler = ExceptionHandler.getInstance();
		// 启动全局异常信息捕获
		this.exceptionHandler.init(getApplicationContext(), logError, logPath);
	}

}
