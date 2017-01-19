package com.yue.demo.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yue.demo.R;
import com.yue.demo.MainActivity;
import com.yue.demo.util.LogUtil;

/**
 * 使用URL读取网络资源
 * 
 * @author chengyue
 * 
 */
public class URLTest extends Activity {

	private final String tag = URLTest.class.getSimpleName();
	private Bitmap bitmap;
	private Handler handler;
	private ImageView show;

	private EditText address;
	private Button getRes;

	String path = "http://c.hiphotos.baidu.com/image/pic/item/e824b899a9014c08cc2a67ac087b02087bf4f4a2.jpg";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				Object object = msg.obj;

				switch (msg.what) {
				case 0x110:
					show.setImageBitmap(bitmap);

					break;

				case 0x111:
					Toast.makeText(URLTest.this, object.toString(),
							Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			}
		};
		getRes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				new Thread(new Runnable() {

					@Override
					public void run() {

						try {
							String spec = address.getText().toString();
							if (address == null) {
								Message message = new Message();
								message.what = 0x111;
								message.obj = "请输入网络资源的地址";
								handler.sendMessage(message);
								return;
							}

							URL url = new URL(spec);

							String info = "URL的资源名 ：" + url.getFile()
									+ "\nURL的主机名 ：" + url.getHost()
									+ "\nURL的路径部分 ：" + url.getPath()
									+ "\nURL的端口号 ：" + url.getPort()
									+ "\nURL的协议名称：" + url.getProtocol()
									+ "\nURL的查询字符串部分：" + url.getQuery();
							LogUtil.d(tag, info);

							Message msg = new Message();
							msg.what = MainActivity.TOAST_FLAG;
							msg.obj = info;
							MainActivity.mHandler.sendMessage(msg);

							InputStream is = url.openStream();
							bitmap = BitmapFactory.decodeStream(is);
							handler.sendEmptyMessage(0x110);
							is.close();

							is = url.openStream();
							OutputStream os = openFileOutput("urltest.jpg",
									MODE_WORLD_READABLE);
							// byte[] bs = new byte[1024];
							// int len = 0;
							// while ((len = is.read(bs)) != 0) {
							//
							// os.write(bs, 0, len);
							// }
							BufferedReader br = new BufferedReader(
									new InputStreamReader(is));
							String content = null;
							while ((content = br.readLine()) != null) {
								os.write(content.getBytes());
							}
							is.close();
							os.close();
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
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
		ll.setGravity(Gravity.CENTER_HORIZONTAL);

		address = new EditText(this);
		address.setLayoutParams(MainActivity.layoutParams_mw);
		address.setHint("请输入网络资源的地址...");
		address.setText(path);
		ll.addView(address);

		getRes = new Button(this);
		getRes.setLayoutParams(MainActivity.layoutParams_mw);
		getRes.setText("获取网络图片");
		ll.addView(getRes);

		show = new ImageView(this);
		show.setLayoutParams(MainActivity.layoutParams_ww);
		show.setImageResource(R.drawable.aaa);
		ll.addView(show);

		setContentView(ll);
	}

}
