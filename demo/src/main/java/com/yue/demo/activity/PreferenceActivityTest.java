package com.yue.demo.activity;

import java.util.List;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.Toast;

import com.yue.demo.R;

/**
 * PreferenceActivity结合PreferenceFragment实现参数设置界面
 * 
 * @author chengyue
 * @version 1.0
 */
public class PreferenceActivityTest extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 该方法用于为该界面设置一个标题按钮
		if (hasHeaders()) {
			Button button = new Button(this);
			button.setText("设置操作");
			// 将该按钮添加到该界面上
			setListFooter(button);
		}
		String phone = PreferenceManager.getDefaultSharedPreferences(
				getApplicationContext()).getString("warning_time", "111");
		Toast.makeText(getApplicationContext(), phone, 200).show();
		// addPreferencesFromResource(R.xml.setting);
	}

	// 重写该该方法，负责加载页面布局文件
	@Override
	public void onBuildHeaders(List<Header> target) {
		// 加载选项设置列表的布局文件
		loadHeadersFromResource(R.xml.preference_headers, target);
	}

	public static class Prefs1Fragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences);
			addPreferencesFromResource(R.xml.setting);
		}
	}

	public static class Prefs2Fragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.display_prefs);
			// 获取传入该Fragment的参数
			String website = getArguments().getString("website");
			Toast.makeText(getActivity(), "网站域名是：" + website, Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	protected boolean isValidFragment(String fragmentName) {
		return true;
	}
}
