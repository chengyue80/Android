package com.yue.demo.desktop;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.yue.demo.R;
import com.yue.demo.util.LogUtil;

/**
 * 开发桌面控件
 * 
 * @author chengyue
 * 
 */
public class DesktopApp extends AppWidgetProvider {

	private final String TAG = DesktopApp.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
	}

	/**
	 * 这个方法字面意思是负责更新桌面小控件，但貌似只有在小控件被用户放到桌面上时被调用了一次，读者可以自己通过输出Log来测试
	 * 实现桌面控件是通常都会考虑重写该方法
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		LogUtil.d(TAG, "----onUpdate----");
		Toast.makeText(context, "onUpdate", Toast.LENGTH_SHORT).show();
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		// 加载指定界面布局文件，创建RemoteViews对象
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.photoupload); // ①
		remoteViews.setImageViewResource(R.id.imageView, R.drawable.aaa);
		ComponentName componentName = new ComponentName(context, DesktopApp.class);
		appWidgetManager.updateAppWidget(componentName, remoteViews);
	}

	@SuppressLint("NewApi")
	@Override
	public void onAppWidgetOptionsChanged(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId,
			Bundle newOptions) {
		super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId,
				newOptions);
	}

	/** 当一个或多个桌面控件被删除时回调该方法 */
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		LogUtil.d(TAG, "----onDeleted----");
		Toast.makeText(context, "onDeleted", Toast.LENGTH_SHORT).show();
	}

	/** 当收到ACTION_APPWIDGET_ENABLED Broadcast时回调该方法 */
	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);
		LogUtil.d(TAG, "----onEnabled----");
		Toast.makeText(context, "onEnabled", Toast.LENGTH_SHORT).show();
	}

	/** 当收到ACTION_APPWIDGET_DISABLED Broadcast时回调该方法 */
	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		super.onDisabled(context);
		LogUtil.d(TAG, "----onDisabled----");
		Toast.makeText(context, "onDisabled", Toast.LENGTH_SHORT).show();
	}

}
