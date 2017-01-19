package com.yue.demo.res;

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

/**
 * 
 * 资源加载的入口Activity
 * 
 * @author chengyue
 * 
 */
public class MainActivity_Res extends Activity {

	public static final String Tag = "res >> ";
	private DemoInfo[] demos = {

			new DemoInfo(R.string.demo_res_name_drawable,
					R.string.demo_res_title_drawable, DrawableTest.class),

			new DemoInfo(R.string.demo_res_name_valueanim,
					R.string.demo_res_title_valueanim, ValueAnimatorTest.class),

			new DemoInfo(R.string.demo_res_name_layout,
					R.string.demo_res_title_layout, LayoutAnimations.class),

			new DemoInfo(R.string.demo_res_name_xml,
					R.string.demo_res_title_xml, XmlResTest.class),

			new DemoInfo(R.string.demo_res_name_attr,
					R.string.demo_res_title_attr, AttrResTest.class),

			new DemoInfo(R.string.demo_res_name_clip,
					R.string.demo_res_title_clip, ClipDrawableDemo.class)

	};

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
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
