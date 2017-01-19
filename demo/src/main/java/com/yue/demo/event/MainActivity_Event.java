package com.yue.demo.event;

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

public class MainActivity_Event extends Activity {
	public static final String Tag = "event >> ";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
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

	private DemoInfo[] demos = {
			new DemoInfo(R.string.demo_event_name_plantgame,
					R.string.demo_event_title_plantgame, PlantGame.class),

			new DemoInfo(R.string.demo_event_name_sendsms,
					R.string.demo_event_title_sendsms, SendSmsActivity.class),

			new DemoInfo(R.string.demo_event_name_ball,
					R.string.demo_event_title_ball, BallActivity.class),

			new DemoInfo(R.string.demo_event_name_conf,
					R.string.demo_event_title_conf, ConfigurationTest.class),

			new DemoInfo(R.string.demo_event_name_async,
					R.string.demo_event_title_async, AsyncTaskTest.class)

	};
}
