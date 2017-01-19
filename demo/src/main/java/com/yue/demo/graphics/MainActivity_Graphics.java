package com.yue.demo.graphics;

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
 * 图形与图像处理
 * 
 * @author chengyue
 * 
 */
public class MainActivity_Graphics extends BaseActivity {

	public static final String Tag = "graphics >> ";

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

	private DemoInfo[] demos = {
			new DemoInfo(R.string.demo_graphics_name_bitmap,
					R.string.demo_graphics_title_bitmap, BitmapTest.class),

			new DemoInfo(R.string.demo_graphics_name_canvas,
					R.string.demo_graphics_title_canvas, CanvasTest.class),

			new DemoInfo(R.string.demo_graphics_name_path,
					R.string.demo_graphics_title_path, PathTest.class),

			new DemoInfo(R.string.demo_graphics_name_handdraw,
					R.string.demo_graphics_title_handdraw, HandDraw.class),

			new DemoInfo(R.string.demo_graphics_name_pinBall,
					R.string.demo_graphics_title_pinBall, PinBall.class),

			new DemoInfo(R.string.demo_graphics_name_matrix,
					R.string.demo_graphics_title_matrix, MatrixTest.class),

			new DemoInfo(R.string.demo_graphics_name_moveback,
					R.string.demo_graphics_title_moveback, MoveBack.class),

			new DemoInfo(R.string.demo_graphics_name_wrap,
					R.string.demo_graphics_title_wrap, WrapTest.class),

			new DemoInfo(R.string.demo_graphics_name_shader,
					R.string.demo_graphics_title_shader, ShaderTest.class),

			new DemoInfo(R.string.demo_graphics_name_animationdrawable,
					R.string.demo_graphics_title_animationdrawable,
					AnimationDrawableTest.class),

			new DemoInfo(R.string.demo_graphics_name_blast,
					R.string.demo_graphics_title_blast, Blast.class),

			new DemoInfo(R.string.demo_graphics_name_tweenanim,
					R.string.demo_graphics_title_tweenanim, TweenAnimTest.class),

			new DemoInfo(R.string.demo_graphics_name_butterfly,
					R.string.demo_graphics_title_butterfly, Butterfly.class),

			new DemoInfo(R.string.demo_graphics_name_listviewtween,
					R.string.demo_graphics_title_listviewtween,
					ListViewTween.class),

			new DemoInfo(R.string.demo_graphics_name_animator,
					R.string.demo_graphics_title_animator, AnimatorTest.class),

			new DemoInfo(R.string.demo_graphics_name_surfaceview,
					R.string.demo_graphics_title_surfaceview,
					SurfaceViewTest.class),

			new DemoInfo(R.string.demo_graphics_name_showwave,
					R.string.demo_graphics_title_showwave, ShowWaVe.class) };

}
