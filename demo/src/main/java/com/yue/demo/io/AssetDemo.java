package com.yue.demo.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.yue.demo.R;
import com.yue.demo.util.LogUtil;
import com.yue.demo.util.ZipUtil;

public class AssetDemo extends Activity {

	private String TAG = AssetDemo.class.getSimpleName();

	private Button test;
	private AssetManager am;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		am = getAssets();
		test = (Button) findViewById(R.id.button1);
		test.setText("asset 文件移动复制");
		test.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				InputStream in = null;
				OutputStream outputStream = null;
				File desFile = new File(Environment
						.getExternalStorageDirectory() + "/wjbz/html/www.zip");

				try {

					in = am.open("www.zip");
					outputStream = new FileOutputStream(desFile);

					byte[] buffer = new byte[1024];
					int length = in.read(buffer);
					while (length > 0) {
						outputStream.write(buffer, 0, length);
						length = in.read(buffer);
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (null != in) {
						try {
							in.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (null != outputStream) {
						try {
							outputStream.flush();
							outputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
				}

				String path = Environment.getExternalStorageDirectory()
						+ "/wjbz/html/";
				ZipUtil.unZip(path + "www.zip", path, "utf-8");

				if (desFile.exists()) {
					Toast.makeText(getApplicationContext(), "复制成功", 2000)
							.show();
				}

			}
		});
	}
}
