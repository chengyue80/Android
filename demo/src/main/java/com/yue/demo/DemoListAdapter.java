package com.yue.demo;

import com.yue.demo.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DemoListAdapter extends BaseAdapter {
	private Context mContext;
	private DemoInfo[] demos;

	public DemoListAdapter(Context mContext, DemoInfo[] demos) {
		super();
		this.mContext = mContext;
		this.demos = demos;
	}

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		convertView = View.inflate(mContext, R.layout.demo_info_item, null);
		TextView title = (TextView) convertView.findViewById(R.id.title);
		TextView desc = (TextView) convertView.findViewById(R.id.desc);
		title.setText(demos[index].title);
		desc.setText(demos[index].desc);
		return convertView;
	}

	@Override
	public int getCount() {
		return demos.length;
	}

	@Override
	public Object getItem(int index) {
		return demos[index];
	}

	@Override
	public long getItemId(int id) {
		return id;
	}
}
