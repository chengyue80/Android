package com.iflytek.android.framework.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
/**
 * handler工具类，用于发送handler
 * com.iflytek.android.framework.thread.HandlerUtil
 * @author 陈智磊 <br/>
 * create at 2015年1月29日 下午3:10:54
 */
public abstract class HandlerUtil {
	private Handler handler;

	public HandlerUtil(Context context) {

		handler = new Handler(context.getMainLooper()) {
			@Override
			public void handleMessage(Message msg) {
				getMsg(msg.obj, msg.what);
				super.handleMessage(msg);
			}
		};
	}

	/**
	 * 发送消息
	 * @param obj
	 * @param what
	 */
	public void sendMsg(Object obj, int what) {

		handler.sendMessage(handler.obtainMessage(what, obj));
	}

	/**
	 * 获取消息
	 * @param obj
	 * @param what
	 */
	public abstract void getMsg(Object obj, int what);

}
