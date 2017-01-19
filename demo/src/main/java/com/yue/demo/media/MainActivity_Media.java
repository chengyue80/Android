package com.yue.demo.media;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.iflytek.android.framework.base.BaseActivity;
import com.yue.demo.DemoInfo;
import com.yue.demo.DemoListAdapter;
import com.yue.demo.R;

/**
 * 多媒体应用开发
 * 
 * @author chengyue
 * 
 */
public class MainActivity_Media extends BaseActivity {

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

	private DemoInfo[] demos = {

			new DemoInfo(R.string.demo_media_name_mediaEffect,
					R.string.demo_media_title_mediaEffect,
					MediaPlayerTest.class),

			new DemoInfo(R.string.demo_media_name_soundpool,
					R.string.demo_media_title_soundpool, SoundPoolTest.class),

			new DemoInfo(R.string.demo_media_name_videoview,
					R.string.demo_media_title_videoview, VideoViewTest.class),

			new DemoInfo(R.string.demo_media_name_surfaceview,
					R.string.demo_media_title_surfaceview,
					SurfaceViewPlayVideo.class),

			new DemoInfo(R.string.demo_media_name_mediarecorder,
					R.string.demo_media_title_mediarecorder,
					MediaRecorderTest.class),

			new DemoInfo(R.string.demo_media_name_camera,
					R.string.demo_media_title_camera, MyCameraActivity.class),

			new DemoInfo(R.string.demo_media_name_recordvideo,
					R.string.demo_media_title_recordvideo, RecordVideo.class),

			new DemoInfo(R.string.demo_media_name_vitamio,
					R.string.demo_media_title_vitamio, VitamioDemo.class)

	};
}
