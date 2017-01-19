package com.iflytek.android.framework.toast;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * 自定义消息栏
 * @author Administrator
 *
 */
public class BaseToast {

	// 防止重复显示消息
	private static Toast mToast;
	// 创建handler
	private static Handler mHandler = new Handler();
	// 消息线程
	private static Runnable r = new Runnable() {
		public void run() {
			mToast.cancel();
		}
	};

	/**
	 * 
	 * 描述：显示提示信息（非重复出现）
	 * @param mContext
	 * @param text
	 * @param duration
	 * @throws
	 */
	public static void showToastNotRepeat(Context context, String text, int duration) {

		mHandler.removeCallbacks(r);
		if (mToast != null)
			mToast.setText(text);
		else
			mToast = Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_SHORT);
		mHandler.postDelayed(r, duration);

		mToast.show();
	}

	/**
	 * 
	 * 描述：显示提示信息（非重复出现）
	 * @param mContext
	 * @param resourceId
	 * @param duration
	 * @throws
	 */
	public static void showToastNotRepeat(Context context, int resourceId, int duration) {
		showToastNotRepeat(context.getApplicationContext(), context.getResources().getString(resourceId), duration);
	}
	
	/**
	 * 描述：默认提示语
	 * 
	 * @param context
	 *            android的上下文
	 * @param text
	 *            显示的文本内容
	 * @param duration
	 *            消息消失时间
	 * @throws
	 */
	public static void DefaultToast(Context context, String text, int duration) {
		Toast.makeText(context, text, duration).show();
	}

	/**
	 * 
	 * 描述：自定义显示位置提示语
	 * 
	 * @param context
	 *            android的上下文
	 * @param text
	 *            显示的文本内容
	 * @param duration
	 *            消息消失时间
	 * @param gravity
	 * @param xOffset
	 * @param yOffset
	 * @throws
	 */
	public static void DefaultToast(Context context, String text, int duration,
			int gravity, int xOffset, int yOffset) {
		Toast toast = Toast.makeText(context, text, duration);
		toast.setGravity(gravity, xOffset, yOffset);
		toast.show();
	}

	/**
	 * 描述：默认提示语
	 * 
	 * @param context
	 *            android的上下文
	 * @param resourceId
	 *            资源ID
	 * @param duration
	 *            消息消失时间
	 * @throws
	 */
	public static void DefaultToast(Context context, int resourceId, int duration) {
		Toast.makeText(context, context.getResources().getString(resourceId),
				duration).show();
	}

	/**
	 * 
	 * 描述：自定义显示位置提示语
	 * 
	 * @param context
	 *            android的上下文
	 * @param resourceId
	 *            资源ID
	 * @param duration
	 *            消息消失时间
	 * @param gravity
	 * @param xOffset
	 * @param yOffset
	 * @throws
	 */
	public static void DefaultToast(Context context, int resourceId, int duration,
			int gravity, int xOffset, int yOffset) {
		Toast toast = Toast.makeText(context,
				context.getResources().getString(resourceId), duration);
		toast.setGravity(gravity, xOffset, yOffset);
		toast.show();
	}
}
