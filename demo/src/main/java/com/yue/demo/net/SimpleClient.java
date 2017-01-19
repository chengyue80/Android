package com.yue.demo.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yue.demo.MainActivity;
import com.yue.demo.util.LogUtil;

public class SimpleClient extends Activity {

	private Button button, button2;
	private EditText show;
	private TextView msg_tv;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				super.handleMessage(msg);
				LogUtil.d("来自服务端的消息为：" + msg.obj);
				msg_tv.setText(msg.obj.toString());
			}
		};
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread(new Runnable() {

					@Override
					public void run() {

						try {
							Socket socket = new Socket(show.getText()
									.toString(), 8888);

							BufferedReader bf = new BufferedReader(
									new InputStreamReader(socket
											.getInputStream()));
							String msg = bf.readLine();
							Message message = new Message();
							message.what = 0x123;
							message.obj = msg;
							handler.sendMessage(message);
							// handler.obtainMessage(0x123, message);

							bf.close();
							socket.close();
						} catch (UnknownHostException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		});

		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {

					@Override
					public void run() {

						try {
							Socket socket = new Socket();
							socket.connect(new InetSocketAddress(show.getText()
									.toString(), 30000), 8888);

							socket.close();
						} catch (UnknownHostException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		});
	}

	private void initView() {
		LinearLayout ll = new LinearLayout(this);
		ll.setLayoutParams(MainActivity.layoutParams_mm);
		ll.setOrientation(LinearLayout.VERTICAL);

		show = new EditText(this);
		show.setLayoutParams(MainActivity.layoutParams_mw);
		show.setText("172.16.200.113");
		ll.addView(show);

		button = new Button(this);
		button.setLayoutParams(MainActivity.layoutParams_mw);
		button.setText("ip");
		ll.addView(button);

		msg_tv = new TextView(this);
		msg_tv.setLayoutParams(MainActivity.layoutParams_mw);
		msg_tv.setText("msg");
		ll.addView(msg_tv);

		button2 = new Button(this);
		button2.setLayoutParams(MainActivity.layoutParams_mw);
		button2.setText("超时连接");
		ll.addView(button2);
		setContentView(ll);

	}
}
