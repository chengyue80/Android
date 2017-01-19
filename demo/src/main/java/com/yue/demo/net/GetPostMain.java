package com.yue.demo.net;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yue.demo.MainActivity;

/**
 * 使用url connection 提交请求
 * 
 * @author chengyue
 * 
 */
public class GetPostMain extends Activity {
	private String path = "http://172.16.200.49:8088/abc/login.jsp";
	private EditText address;
	Button get, post;
	TextView show;
	private String cookie = null;
	// 代表服务器响应的字符串
	String response;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x123) {
				// 设置show组件显示服务器响应
				show.setText(response);
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		get.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread() {
					@Override
					public void run() {
						
						response = GetPostUtil.sendGet(address.getText()
								.toString(), null, cookie);
						// 发送消息通知UI线程更新UI组件
						handler.sendEmptyMessage(0x123);
					}
				}.start();
			}
		});
		post.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread() {
					@Override
					public void run() {
						response = GetPostUtil.sendPost(address.getText()
								.toString(), "name=test&pass=test");
						cookie = response;
						handler.sendEmptyMessage(0x123);
					}
				}.start();
				// 发送消息通知UI线程更新UI组件
			}
		});
	}

	private void initView() {
		LinearLayout ll = new LinearLayout(this);
		ll.setLayoutParams(MainActivity.layoutParams_mm);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setGravity(Gravity.CENTER_HORIZONTAL);

		address = new EditText(this);
		address.setLayoutParams(MainActivity.layoutParams_mw);
		address.setHint("请输入网络资源的地址...");
		address.setText(path);
		ll.addView(address);

		LinearLayout layout = new LinearLayout(this);
		layout.setLayoutParams(MainActivity.layoutParams_mw);
		layout.setOrientation(LinearLayout.HORIZONTAL);

		get = new Button(this);
		get.setLayoutParams(MainActivity.layoutParams_ww);
		get.setText("发送GET请求");
		layout.addView(get);

		post = new Button(this);
		post.setLayoutParams(MainActivity.layoutParams_ww);
		post.setText("发送post请求");
		layout.addView(post);
		ll.addView(layout);

		show = new TextView(this);
		show.setLayoutParams(MainActivity.layoutParams_ww);
		ll.addView(show);

		setContentView(ll);
	}
}