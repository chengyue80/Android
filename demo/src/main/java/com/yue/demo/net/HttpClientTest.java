package com.yue.demo.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.yue.demo.R;
import com.yue.demo.MainActivity;
import com.yue.demo.util.LogUtil;

/**
 * 访问被保护的资源
 * 
 * @author chengyue
 * 
 */
public class HttpClientTest extends Activity {
	// 定义全局的HTTPClient
	private HttpClient httpCilent;
	private String path = "http://172.16.200.95:8088/foo/";
	private EditText address;
	Button btnget, btnPost;
	TextView show;

	final Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			super.handleMessage(msg);

			if (msg.what == 0x123) {
				show.append(msg.obj.toString() + "\n");
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		// 系统提供了一个类去实例化httpCilent
		httpCilent = new DefaultHttpClient();
		btnget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				address.setText(path + "secret.jsp");
				show.setText("");
				new Thread(new Runnable() {

					@Override
					public void run() {
						get();
					}
				}).start();
			}
		});

		btnPost.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				address.setText(path + "login.jsp");
				show.setText("");
				login();
			}
		});

	}

	private void get() {
		try {
			// 我要用httpCilent发送get请求，那么我先定义get请求 参数是地址
			HttpGet get = new HttpGet(address.getText().toString());
			// 执行get请求 获取response对象
			HttpResponse response = httpCilent.execute(get);

			// 获取返回结果 首先判断状态
			if (response.getStatusLine().getStatusCode() == 200) {
				// 200代表请求成功 getEntity 是返回的内容
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream stream = entity.getContent();
					BufferedReader br = new BufferedReader(
							new InputStreamReader(stream));
					String line = null;
					StringBuffer bf = new StringBuffer();

					while ((line = br.readLine()) != null) {
						bf.append(line);
					}

					Message message = new Message();
					message.what = 0x123;
					message.obj = bf.toString();
					handler.sendMessage(message);
					LogUtil.d("request : " + bf.toString());

					MainActivity.myToast(HttpClientTest.this, bf.toString());

				}

			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void login() {

		final View view = getLayoutInflater().inflate(R.layout.login, null);

		new AlertDialog.Builder(HttpClientTest.this).setTitle("登录")
				.setView(view)
				.setPositiveButton("登录", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						String name = ((EditText) view.findViewById(R.id.name))
								.getText().toString();
						String pass = ((EditText) view.findViewById(R.id.pass))
								.getText().toString();

						new Thread(new Runnable() {

							@Override
							public void run() {

								try {
									String url = "http://60.174.83.217:8080/wjbz-service-1.0.0.9/services/rest/credit/exchange";
									// HttpPost post = new HttpPost(address
									// .getText().toString());
									HttpPost post = new HttpPost(url);

									List<NameValuePair> params = new ArrayList<NameValuePair>();
									// params.add(new BasicNameValuePair("name",
									// "test"));
									// params.add(new BasicNameValuePair("pass",
									// "test"));
									params.add(new BasicNameValuePair("userId",
											"402894b24ea9bcf0014eaa5984520002"));
									params.add(new BasicNameValuePair("certId",
											"1111"));
									params.add(new BasicNameValuePair(
											"isEncrypt", "true"));
									params.add(new BasicNameValuePair("token",
											"127fed99-481c-4135-878b-cf9df14b3e99"));
//									{"certId":"1111","token":"127fed99-481c-4135-878b-cf9df14b3e99","isEncrypt":"true","userId":"402894b24ea9bcf0014eaa5984520002"}

									// 设置请求参数
									post.setEntity(new UrlEncodedFormEntity(
											params, HTTP.UTF_8));

									HttpResponse response = httpCilent
											.execute(post);

									if (response.getStatusLine()
											.getStatusCode() == 200) {
										String msg = EntityUtils
												.toString(response.getEntity());
										MainActivity.myToast(
												HttpClientTest.this, msg);
									} else {

										MainActivity.myToast(
												HttpClientTest.this, "请求出错");
									}
								} catch (ClientProtocolException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}).start();

					}
				}).setNegativeButton("取消", null).show();
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

		btnget = new Button(this);
		btnget.setLayoutParams(MainActivity.layoutParams_ww);
		btnget.setText("访问页面 secret.jsp");
		layout.addView(btnget);

		btnPost = new Button(this);
		btnPost.setLayoutParams(MainActivity.layoutParams_ww);
		btnPost.setText("登录系统 login.jsp");
		layout.addView(btnPost);
		ll.addView(layout);

		show = new TextView(this);
		show.setLayoutParams(MainActivity.layoutParams_ww);
		ll.addView(show);

		setContentView(ll);
	}
}
