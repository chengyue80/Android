package com.yue.demo.net;

import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.yue.demo.MainActivity;

/**
 * 调用基于CXF的Web Service
 * 
 * @author chengyue
 * 
 */
public class CallWs extends Activity {

	final static String SERVICE_NS = "http://lee/";
	final static String SERVICE_URL = "http://192.168.1.88:9999/crazyit";
	private EditText editText, editText2;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x123:
				editText.setText(msg.obj.toString());
				break;
			case 0x234:
				editText2.setText(msg.obj.toString());
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();

		// 调用的方法
		String methodName = "getUserList";
		// 创建HttpTransportSE传输对象
		final HttpTransportSE ht = new HttpTransportSE(SERVICE_URL); // ①

	}

	private void initView() {
		LinearLayout ll = new LinearLayout(this);
		ll.setLayoutParams(MainActivity.layoutParams_mm);
		ll.setOrientation(LinearLayout.VERTICAL);

		editText = new EditText(this);
		editText.setLayoutParams(MainActivity.layoutParams_mw);
		ll.addView(editText);

		editText2 = new EditText(this);
		editText2.setLayoutParams(MainActivity.layoutParams_mw);
		ll.addView(editText2);

		setContentView(ll);
	}
}
