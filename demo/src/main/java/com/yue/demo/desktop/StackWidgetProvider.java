package com.yue.demo.desktop;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.yue.demo.R;

public class StackWidgetProvider extends AppWidgetProvider {

	public static final String TOAST_ACTION = "org.yue.mytest.desktop.TOAST_ACTION";
	public static final String EXTRA_ITEM = "org.yue.mytest.desktop.EXTRA_ITEM";

	// 重写该方法，将该组件当成BroadcastReceiver使用
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(TOAST_ACTION)) {
			// 获取Intent中的数据
			int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);
			// 显示Toast提示
			Toast.makeText(context, "点击第【" + viewIndex + "】个列表项",
					Toast.LENGTH_SHORT).show();
		}
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget_layout);

		Intent intent = new Intent(context, StackWidgetService.class);
		remoteViews.setRemoteAdapter(R.id.stack_view, intent);
		// 设置当StackWidgetService提供的列表项为空时，直接显示empty_view组件
		remoteViews.setEmptyView(R.id.stack_view, R.id.empty_view);
		// 创建启动StackWidgetProvider组件（作为BroadcastReceiver）的Intent
		Intent toastIntent = new Intent(context, StackWidgetProvider.class);
		toastIntent.setAction(TOAST_ACTION);

		PendingIntent pIntent = PendingIntent.getBroadcast(context, 0,
				toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		// 关联
		remoteViews.setPendingIntentTemplate(R.id.stack_view, pIntent);
		appWidgetManager.updateAppWidget(new ComponentName(context,
				StackWidgetProvider.class), remoteViews);

	}

}
