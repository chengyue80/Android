package com.yue.demo.other;

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
import com.yue.demo.broadcast.BroadCastActivity;
import com.yue.demo.io.OtherSharePreferencetest;
import com.yue.demo.jsontest.JsonTest;
import com.yue.demo.jsontest.JsonTestMsc;
import com.yue.demo.other.appInfo.AppInfo;
import com.yue.demo.photoupload.Upload;
import com.yue.demo.popwindows.ShowPopupWindow;
import com.yue.demo.res.textView_test.TextViewActivity;

public class MainActivity_other extends Activity {

	public static final String Tag = "other >> ";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ListView mListView = (ListView) findViewById(R.id.listView);
		// 添加ListItem，设置事件响应
		mListView.setAdapter(new DemoListAdapter(this, demos));
		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View v, int index,
					long arg3) {
				onListItemClick(index);
			}
		});

	}

	protected void onListItemClick(int index) {
		Intent intent = new Intent(this, demos[index].demoClass);
		startActivity(intent);
	}

	private static final DemoInfo[] demos = {

			new DemoInfo(R.string.demo_other_name_email,
					R.string.demo_other_title_email, EmailActivity.class),

			// new DemoInfo(R.string.demo_other_name_datePicker,
			// R.string.demo_other_title_datePicker, TimePickerActivity.class),

			new DemoInfo(R.string.demo_other_name_upload,
					R.string.demo_other_title_upload, Upload.class),

			new DemoInfo(R.string.demo_other_name_otherSharePreferencetest,
					R.string.demo_other_title_otherSharePreferencetest,
					OtherSharePreferencetest.class),

			new DemoInfo(R.string.demo_other_name_resolution,
					R.string.demo_other_title_resolution, ResolutionTest.class),

			new DemoInfo(R.string.demo_other_name_appinfo,
					R.string.demo_other_title_appinfo, AppInfo.class),

			new DemoInfo(R.string.demo_other_name_textviewAlign,
					R.string.demo_other_title_textviewAlign,
					TextViewActivity.class),

			new DemoInfo(R.string.demo_other_name_popupWindows,
					R.string.demo_other_title_popupwindows,
					ShowPopupWindow.class),

			new DemoInfo(R.string.demo_other_name_broadcast,
					R.string.demo_other_title_broadcast,
					BroadCastActivity.class),

			new DemoInfo(R.string.demo_other_name_jsonTest,
					R.string.demo_other_title_jsontest, JsonTest.class),

			new DemoInfo(R.string.demo_other_name_jsonTestMsc,
					R.string.demo_other_title_jsonTestMsc, JsonTestMsc.class),

			new DemoInfo(R.string.demo_other_name_notification,
					R.string.demo_other_title_notification,
					NotificationTest.class),

			new DemoInfo(R.string.demo_other_name_install,
					R.string.demo_other_title_install, InstallTest.class),

			new DemoInfo(R.string.demo_other_name_topactivity,
					R.string.demo_other_title_topactivity, TopActvityTest.class),

			new DemoInfo(R.string.demo_other_name_clearData,
					R.string.demo_other_title_clearData, ClearDataDemo.class),

			new DemoInfo(R.string.demo_other_name_draggable,
					R.string.demo_other_title_draggable,
					DraggableGridViewSampleActivity.class),

			new DemoInfo(R.string.demo_other_name_code,
					R.string.demo_other_title_code, ValidateCodeActivity.class),

			new DemoInfo(R.string.demo_other_name_mdsha1,
					R.string.demo_other_title_mdsha1, MDSha1Demo.class),

			new DemoInfo(R.string.demo_other_name_alarm,
					R.string.demo_other_title_alarm, AlarmClockDemo.class),

	};
}
