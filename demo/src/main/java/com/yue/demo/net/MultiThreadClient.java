package com.yue.demo.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yue.demo.MainActivity;
import com.yue.demo.util.LogUtil;

public class MultiThreadClient extends Activity {

	private final String tag = MultiThreadClient.class.getSimpleName();
	private EditText ip, input;
	private Button send;
	private TextView info;
	Handler handler;

	ClientThread clientThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				if (msg.what == 0x123) {

					// 将读取的内容追加显示在文本框中
					LogUtil.d(tag, "msg.obj : " + msg.obj.toString());
					info.append("\n" + msg.obj.toString());
				}
			}
		};

		clientThread = new ClientThread(handler);
		new Thread(clientThread).start();

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// 当用户按下发送按钮后，将用户输入的数据封装成Message，
				// 然后发送给子线程的Handler
				Message message = new Message();
				message.what = 0x345;
				message.obj = input.getText().toString();
				clientThread.revHandler.sendMessage(message);
				input.setText("");
			}
		});
	}

	private void initView() {
		LinearLayout ll = new LinearLayout(this);
		ll.setLayoutParams(MainActivity.layoutParams_mm);
		ll.setOrientation(LinearLayout.VERTICAL);

		ip = new EditText(this);
		ip.setLayoutParams(MainActivity.layoutParams_mw);
		ip.setText("172.16.200.49");
		ll.addView(ip);

		input = new EditText(this);
		input.setLayoutParams(MainActivity.layoutParams_mw);
		input.setHint("请输入要发送的内容");
		ll.addView(input);

		send = new Button(this);
		send.setLayoutParams(MainActivity.layoutParams_mw);
		send.setText("发送");
		ll.addView(send);

		info = new TextView(this);
		info.setLayoutParams(MainActivity.layoutParams_mw);
		info.setText("服务端发送的消息：");
		ll.addView(info);

		setContentView(ll);

	}

	class ClientThread implements Runnable {
		private Socket s;
		// 定义向UI线程发送消息的Handler对象
		private Handler handler;
		// 定义接收UI线程的消息的Handler对象
		public Handler revHandler;
		// 该线程所处理的Socket所对应的输入流
		BufferedReader br = null;
		OutputStream os = null;

		public  ClientThread(Handler handler) {
			this.handler = handler;
		}

		public void run() {
			try {
				s = new Socket(ip.getText().toString(), 8888);
				br = new BufferedReader(new InputStreamReader(
						s.getInputStream()));
				os = s.getOutputStream();
//				s.
				// 启动一条子线程来读取服务器响应的数据
				new Thread() {
					@Override
					public void run() {
						String content = null;
						// 不断读取Socket输入流中的内容。
						try {
							while ((content = br.readLine()) != null) {
								// 每当读到来自服务器的数据之后，发送消息通知程序界面显示该数据
								Message msg = new Message();
								msg.what = 0x123;
								msg.obj = content;
								handler.sendMessage(msg);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}.start();
				// 为当前线程初始化Looper
				Looper.prepare();
				// 创建revHandler对象
				revHandler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						// 接收到UI线程中用户输入的数据
						if (msg.what == 0x345) {
							// 将用户在文本框内输入的内容写入网络
							try {
								os.write((msg.obj.toString() + "\r\n")
										.getBytes("utf-8"));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				};
				// 启动Looper
				Looper.loop();
			} catch (SocketTimeoutException e1) {
				System.out.println("网络连接超时！！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}
}
