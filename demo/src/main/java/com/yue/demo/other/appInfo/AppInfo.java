package com.yue.demo.other.appInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.yue.demo.R;
import com.yue.demo.util.LogUtil;

public class AppInfo extends Activity {

	private ListView listView;
	private List<ApplicationInfo> infos;
	// ȫ�ֱ�������¼ѡ�е�item
	public static int select_item = -1;
	private MyListAdapter myAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_appinfo);

		listView = (ListView) findViewById(R.id.lv_appInfo);

		infos = getPackageManager().getInstalledApplications(0);

		List<HashMap<String, String>> mData = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> one = new HashMap<String, String>();
		one.put("data", "tom");
		one.put("data1", "aaa");
		HashMap<String, String> two = new HashMap<String, String>();
		two.put("data", "tom1");
		HashMap<String, String> the = new HashMap<String, String>();
		the.put("data", "tom2");
		HashMap<String, String> four = new HashMap<String, String>();
		four.put("data", "tom3");
		mData.add(one);
		mData.add(two);
		mData.add(the);
		mData.add(four);

		mData.toString();

		SharedPreferences preferences = getSharedPreferences("listtest",
				Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);

		Editor editor = preferences.edit();
		editor.putString("data", mData.toString());
		editor.commit();

		// Editor editor2= preferences.edit();

		String strData = preferences.getString("data", null);

		System.out.println(strData + "===============");
		System.out.println("list 元数据 ："
				+ strData.substring(1, strData.length() - 1));
		if (strData != null) {
			List<HashMap<String, String>> mData2 = new ArrayList<HashMap<String, String>>();
			String[] listStr = strData.substring(1, strData.length() - 1)
					.split(", ");
			for (String str : listStr) {
				System.out.println("map 元数据：" + str);
				String[] mapStr = str.substring(1, str.length() - 1).split("=");
				HashMap<String, String> map = new HashMap<String, String>();
				System.out.println("数据 ： " + mapStr[0] + "; " + mapStr[1]);

			}
		}

		SimpleAdapter sAdapter = new SimpleAdapter(AppInfo.this, mData,
				android.R.layout.simple_list_item_1, new String[] { "data" },
				new int[] { android.R.id.text1 }) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				View v = super.getView(position, convertView, parent);
				System.out.println("-------------------" + position);
				return v;
			}
		};
		myAdapter = new MyListAdapter(getApplicationContext(), infos);
		listView.setAdapter(myAdapter);
		// listView.notifyAll();
//		listView.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
//				Log.d("TAG", "onItemSelected");
//				select_item = position;
//				view.findViewById(R.id.iv_appinfo_item_right).setVisibility(
//						View.VISIBLE);
//				myAdapter.notifyDataSetChanged();
//
//				// Log.d("TAG", position + " : " + ll.isFocused()
//				// + "===convertView.isSelected() :" +
//				// ll.isSelected()+"---- num = ");
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//				// TODO Auto-generated method stub
//				Log.d("TAG", "onNothingSelected");
//			}
//		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// view.setTag()
				view.findViewById(R.id.ll_appinfo_item).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
							}
						});
				Toast.makeText(AppInfo.this, infos.get(position).packageName,
						Toast.LENGTH_SHORT).show();
				System.out.println(infos.get(position).toString());
				String packageName = infos.get(position).packageName;
				String name = infos.get(position).name;
				LogUtil.d("appinfo:", "packageName/name : " + packageName + "/" + name);

			}
		});

	}

	private void setWhosPath(OnClickListener clkLsn, View view) {

		// Log.d(TAG, "setWhosPath()");
		view.setOnClickListener(clkLsn);
		// for (int i = 0; i < mHistItems.size(); i++) {
		//
		// mHistItems.get(i).setOnClickListener(clkLsn);
		// }
	}

}
