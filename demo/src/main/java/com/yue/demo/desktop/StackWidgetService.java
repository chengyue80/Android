package com.yue.demo.desktop;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.yue.demo.R;
import com.yue.demo.util.LogUtil;

public class StackWidgetService extends RemoteViewsService {

	/**
	 * 重写该方法，该方法返回一个RemoteViewsFactory对象。 RemoteViewsFactory对象的的作用类似于Adapter，
	 * 它负责为RemoteView中指定组件提供多个列表项。
	 */
	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		return new StackRemoteViewsFactory(this, intent);
	}

	class StackRemoteViewsFactory implements RemoteViewsFactory {

		private int[] items = null;
		private Context mContext = null;

		public StackRemoteViewsFactory(Context context, Intent intent) {
			mContext = context;
		}

		@Override
		public void onCreate() {
			// 初始化items数组
			items = new int[] { R.drawable.bomb5, R.drawable.bomb6,
					R.drawable.bomb7, R.drawable.bomb8, R.drawable.bomb9,
					R.drawable.bomb10, R.drawable.bomb11, R.drawable.bomb12,
					R.drawable.bomb13, R.drawable.bomb14, R.drawable.bomb15,
					R.drawable.bomb16 };
		}

		@Override
		public void onDataSetChanged() {

		}

		@Override
		public void onDestroy() {
			items = null;

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return items.length;
		}

		// 该方法的返回值控制各位置所显示的RemoteViews
		@Override
		public RemoteViews getViewAt(int position) {
			// 创建RemoteViews对象，加载/res/layout目录下widget_item.xml文件
			RemoteViews rv = new RemoteViews(mContext.getPackageName(),
					R.layout.widget_item);

			rv.setImageViewResource(R.id.widget_item, items[position]);
			// 创建Intent、用于传递RemoteViewsService对象
			Intent intent = new Intent();
			intent.putExtra(StackWidgetProvider.EXTRA_ITEM, position);

			rv.setOnClickFillInIntent(R.id.widget_item, intent);

			// 此处使用让线程暂停0.5秒来模拟加载该组件
			try {
				LogUtil.d("StackWidgetService", "加载【" + position + "】位置的组件");
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return rv;
		}

		@Override
		public RemoteViews getLoadingView() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return true;
		}
	}

}
