package com.yue.demo.ui.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.yue.demo.R;
import com.yue.demo.util.LogUtil;

public class PopupDemo extends Activity {

	private final String TAG = PopupDemo.class.getSimpleName();

	protected PopupWindow selectPicPopupWindow;

	private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.popupdemo);

		button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectPicPopupWindow = new SelectPopupWindows(PopupDemo.this);

				View contentView = LayoutInflater.from(PopupDemo.this).inflate(
						R.layout.popupdemo, null);
				selectPicPopupWindow.showAtLocation(contentView, Gravity.RIGHT,
						0, button.getHeight());
				// selectPicPopupWindow
			}
		});

	}

	class SelectPopupWindows extends PopupWindow {

		private View contentView;

		private ListView listView1, listView2;
		public ArrayList<String> datas;
		protected Myadapter adapter;

		public SelectPopupWindows(Context context) {
			super(context);
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			datas = new ArrayList<String>();

			for (int i = 0; i < 10; i++) {
				datas.add("数据 " + i);
			}
			contentView = inflater.inflate(R.layout.pop_list, null);

			// 设置SelectPicPopupWindow的View
			this.setContentView(contentView);
			// 设置SelectPicPopupWindow弹出窗体的宽
			this.setWidth(LayoutParams.WRAP_CONTENT);
			// 设置SelectPicPopupWindow弹出窗体的高
			this.setHeight(LayoutParams.WRAP_CONTENT);
			// 设置SelectPicPopupWindow弹出窗体可点击
			this.setFocusable(true);
			// 设置SelectPicPopupWindow弹出窗体动画效果
			this.setAnimationStyle(R.style.PopupwindowAnimationStyle);
			// 实例化一个ColorDrawable颜色为半透明
			ColorDrawable colorDrawable = new ColorDrawable(0xb0000000);
			// 设置SelectPicPopupWindow弹出窗体的背景
			this.setBackgroundDrawable(colorDrawable);
			// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
			contentView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					int height = contentView.findViewById(R.id.pop_list)
							.getTop();
					int y = (int) event.getY();
					if (event.getAction() == MotionEvent.ACTION_UP) {
						if (y < height) {
							dismiss();
						}
					}
					return true;
				}
			});

			listView1 = (ListView) contentView.findViewById(R.id.listView1);

			// listView2 = (ListView) contentView.findViewById(R.id.listView2);

			adapter = new Myadapter(context, datas);

			listView1.setAdapter(adapter);

			listView1.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					adapter.setSelectItem(position);
					adapter.notifyDataSetInvalidated();
				}
			});

		}
	}

	class Myadapter extends BaseAdapter {

		List<String> datas = new ArrayList<String>();
		private Context context;

		public Myadapter(Context context, ArrayList<String> datas) {

			this.context = context;
			this.datas = datas;
		}

		@Override
		public int getCount() {
			return datas.size();
		}

		@Override
		public Object getItem(int position) {
			return datas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.pop_listview_item, null);
			TextView textView = (TextView) convertView
					.findViewById(R.id.textView1);
			textView.setText(datas.get(position));

			if (convertView.isSelected()) {
				LogUtil.d(TAG, "convertView isSelected");
				Toast.makeText(getApplicationContext(),
						"convertView isSelected", 1000).show();
			}

			if (convertView.isFocused()) {

				LogUtil.d(TAG, "convertView isFocused");
				Toast.makeText(getApplicationContext(),
						"convertView isFocused", 1000).show();
			}

			if (textView.isSelected()) {
				LogUtil.d(TAG, "textView isSelected");
				Toast.makeText(getApplicationContext(), "textView isSelected",
						1000).show();
			}

			if (textView.isFocused()) {
				Toast.makeText(getApplicationContext(), "textView isFocused",
						1000).show();

				LogUtil.d(TAG, "textView isFocused");
			}

			if (position == selectItem) {
				textView.setBackgroundColor(Color.RED);
				textView.setTextSize(20);
			} else {
				textView.setTextSize(12);
				textView.setBackgroundColor(Color.TRANSPARENT);
			}

			return convertView;
		}

		public void setSelectItem(int selectItem) {
			this.selectItem = selectItem;
		}

		private int selectItem = -1;
	}

}
