package com.yue.demo.ui.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.yue.demo.R;
import com.yue.demo.DemoInfo;
import com.yue.demo.DemoListAdapter;
import com.yue.demo.activity.ExpandableListActivityTest;

public class MainActivity_View extends Activity {

	private DemoInfo[] demos = {
			new DemoInfo(R.string.demo_ui_view_name_textview,
					R.string.demo_ui_view_title_textview,
					TextViewTestActivity.class),

			new DemoInfo(R.string.demo_ui_view_name_button,
					R.string.demo_ui_view_title_button,
					ButtonTestActivity.class),

			new DemoInfo(R.string.demo_ui_view_name_edittext,
					R.string.demo_ui_view_title_edittext,
					EditTextTestActivity.class),

			new DemoInfo(R.string.demo_ui_view_name_imageview,
					R.string.demo_ui_view_title_imageview,
					ImageTestActivity.class),

			new DemoInfo(R.string.demo_ui_view_name_check,
					R.string.demo_ui_view_title_check, CheckTestActivity.class),

			new DemoInfo(R.string.demo_ui_view_name_clock,
					R.string.demo_ui_view_title_clock, ClockTestActivity.class),

			new DemoInfo(R.string.demo_ui_view_name_expand,
					R.string.demo_ui_view_title_expand,
					ExpandableListActivityTest.class),

			new DemoInfo(R.string.demo_ui_view_name_spinner,
					R.string.demo_ui_view_title_spinner, SpinnerTest.class),

			new DemoInfo(R.string.demo_ui_view_name_circle,
					R.string.demo_ui_view_title_circle,
					CircleViewActivity.class),

			new DemoInfo(R.string.demo_ui_view_name_pop,
					R.string.demo_ui_view_title_pop, PopupDemo.class),

			new DemoInfo(R.string.demo_ui_view_name_touchImage,
					R.string.demo_ui_view_title_touchImage,
					TouchImageViewActivity.class),

			new DemoInfo(R.string.demo_ui_view_name_webview,
					R.string.demo_ui_view_title_webview, WebViewUploadPic.class),

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ListView mListView = (ListView) findViewById(R.id.listView);

		mListView.setAdapter(new DemoListAdapter(this, demos));
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				onListItemClick(arg2);
			}
		});
	}

	private void onListItemClick(int index) {

		Intent intent = new Intent(this, demos[index].demoClass);
		startActivity(intent);

	}

}
