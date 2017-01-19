package com.yue.demo.content;

import com.iflytek.android.framework.base.BaseActivity;
import com.yue.demo.R;
import com.yue.demo.DemoInfo;
import com.yue.demo.DemoListAdapter;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 使用ContentProvider实现数据共享<br/>
 * org.yue.providers
 * 
 * @author chengyue
 * 
 */
public class MainActivity_Provider extends BaseActivity {

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

	DemoInfo[] demos = {
			new DemoInfo(R.string.demo_content_name_contentresolver,
					R.string.demo_content_title_contentresolver,
					MyContentResolver.class),

			new DemoInfo(R.string.demo_content_name_dictresolver,
					R.string.demo_content_title_dictresolver,
					DictResolverTest.class),

			new DemoInfo(R.string.demo_content_name_contact,
					R.string.demo_content_title_contact,
					ContactProviderTest.class),

			new DemoInfo(R.string.demo_content_name_media,
					R.string.demo_content_title_media, MediaProviderTest.class),

			new DemoInfo(R.string.demo_content_name_monitorsms,
					R.string.demo_content_title_monitorsms, Monitor.class) };

}
