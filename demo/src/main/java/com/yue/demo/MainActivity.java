package com.yue.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Toast;

import com.yue.demo.activity.MainActivity_Activity;
import com.yue.demo.content.MainActivity_Provider;
import com.yue.demo.desktop.MainActivity_Desktop;
import com.yue.demo.event.MainActivity_Event;
import com.yue.demo.gps.MainActivity_GPS;
import com.yue.demo.graphics.MainActivity_Graphics;
import com.yue.demo.intent.MainActivity_Intent;
import com.yue.demo.io.MainActivity_IO;
import com.yue.demo.link.Link;
import com.yue.demo.media.MainActivity_Media;
import com.yue.demo.net.MainActivity_Net;
import com.yue.demo.notifications.MainActivity_Notification;
import com.yue.demo.opengl.Mainactivity_Opengl;
import com.yue.demo.other.MainActivity_other;
import com.yue.demo.res.MainActivity_Res;
import com.yue.demo.sensor.MainActivity_Sensor;
import com.yue.demo.service.MainActivity_Service;
import com.yue.demo.ui.MainActivity_UI;

/**
 * 程序入口，导航到各个二级MainActivity
 * 
 * @author chengyue
 */
public class MainActivity extends RootActivity {

	private final String TAG = MainActivity.class.getSimpleName();

	public static LayoutParams layoutParams_mm = new LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	public static LayoutParams layoutParams_mw = new LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	public static LayoutParams layoutParams_ww = new LayoutParams(
			LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	public static LayoutParams layoutParams_weight = new LayoutParams(
			LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f);

	private static Context mContext;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mContext = getApplicationContext();
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
	private void onListItemClick(int index) {
		Intent intent = new Intent(this, demos[index].demoClass);
		startActivity(intent);
	}

	private static final DemoInfo[] demos = {
			new DemoInfo(R.string.demo_main_name_ui,
					R.string.demo_main_title_ui, MainActivity_UI.class),

			new DemoInfo(R.string.demo_main_name_event,
					R.string.demo_main_title_event, MainActivity_Event.class),

			new DemoInfo(R.string.demo_main_name_activity,
					R.string.demo_main_title_activity,
					MainActivity_Activity.class),

			new DemoInfo(R.string.demo_main_name_intent,
					R.string.demo_main_title_intent, MainActivity_Intent.class),

			new DemoInfo(R.string.demo_main_name_res,
					R.string.demo_main_title_res, MainActivity_Res.class),

			new DemoInfo(R.string.demo_main_name_graphics,
					R.string.demo_main_title_graphics,
					MainActivity_Graphics.class),

			new DemoInfo(R.string.demo_main_name_io,
					R.string.demo_main_title_io, MainActivity_IO.class),

			new DemoInfo(R.string.demo_main_name_provider,
					R.string.demo_main_title_provider,
					MainActivity_Provider.class),

			new DemoInfo(R.string.demo_main_name_service,
					R.string.demo_main_title_service,
					MainActivity_Service.class),

			new DemoInfo(R.string.demo_main_name_media,
					R.string.demo_main_title_media, MainActivity_Media.class),

			new DemoInfo(R.string.demo_main_name_opengl,
					R.string.demo_main_title_opengl, Mainactivity_Opengl.class),

			new DemoInfo(R.string.demo_main_name_net,
					R.string.demo_main_title_net, MainActivity_Net.class),

			new DemoInfo(R.string.demo_main_name_desktop,
					R.string.demo_main_title_desktop,
					MainActivity_Desktop.class),

			new DemoInfo(R.string.demo_main_name_sensor,
					R.string.demo_main_title_sensor, MainActivity_Sensor.class),

			new DemoInfo(R.string.demo_main_name_gps,
					R.string.demo_main_title_gps, MainActivity_GPS.class),

			new DemoInfo(R.string.demo_main_name_link,
					R.string.demo_main_title_link, Link.class),

			new DemoInfo(R.string.demo_main_name_notify,
					R.string.demo_main_title_notify,
					MainActivity_Notification.class),

			new DemoInfo(R.string.demo_main_name_other,
					R.string.demo_main_title_other, MainActivity_other.class)

	};

	public static int TOAST_FLAG = 0x110;
	public static Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			Object object = msg.obj;
			switch (msg.what) {
			case 0x110:
				Toast.makeText(mContext, object.toString(), Toast.LENGTH_LONG)
						.show();
				break;

			default:
				break;
			}
		};
	};

	public static void myToast(Context context, String msg) {
		Looper.prepare();
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
		Looper.loop();
	}
}
