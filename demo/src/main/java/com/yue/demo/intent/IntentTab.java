package com.yue.demo.intent;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.yue.demo.R;
import com.yue.demo.activity.MainActivity_Activity;
import com.yue.demo.event.MainActivity_Event;
import com.yue.demo.other.MainActivity_other;

/**
 * TabHost的使用
 * 
 * @author Chengyue
 * 
 */
public class IntentTab extends TabActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intent_tab);
		// 获取该Activity里面的TabHost组件
		TabHost tabHost = getTabHost();
		// 使用Intent添加第一个Tab页面
		tabHost.addTab(tabHost
				.newTabSpec("tab1")
				.setIndicator("已接电话",
						getResources().getDrawable(R.drawable.ic_launcher))
				.setContent(new Intent(this, MainActivity_other.class)));
		// 使用Intent添加第二个Tab页面
		tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("呼出电话")
				.setContent(new Intent(this, MainActivity_Activity.class)));
		// 使用Intent添加第三个Tab页面
		tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("未接电话")
				.setContent(new Intent(this, MainActivity_Event.class)));
	}
}
