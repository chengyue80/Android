package com.yue.demo.event;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.yue.demo.util.LogUtil;

/**
 * 异步任务 ： AsyncTask
 * 
 * @author chengyue
 * 
 */
public class AsyncTaskTest extends Activity {

	ProgressDialog dialog;
	TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout ll = new LinearLayout(this);
		ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		ll.setOrientation(LinearLayout.VERTICAL);

		textView = new TextView(this);
		textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		textView.setText("aa");
		ll.addView(textView);

		Button button = new Button(this);
		button.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		button.setText("下载");
		ll.addView(button);
		setContentView(ll);

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DownTask downTask = new DownTask(AsyncTaskTest.this);
				try {
					// 执行异步任务
					// String url = http://www.crazyit.org/ethos.php;
					String url = "http://download.kugou.com/download/kugou_android";

					downTask.execute(new URL(url));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * 创建AsyncTask 的子类，AsyncTask是抽象类，不能直接使用
	 * 
	 * @author chengyue
	 * 
	 */
	class DownTask extends AsyncTask<URL, Integer, String> {

		// 可变长的输入参数，与AsyncTask.exucute()对应
		ProgressDialog pdialog;
		// 定义记录已经读取行的数量
		int hasRead = 0;
		Context mContext;

		public DownTask(Context ctx) {
			mContext = ctx;
		}

		// 重写该方法执行后台线程将要完成的任务
		// 可以调用publishProgress(Progress...value)方法更新任务的执行进度
		@Override
		protected String doInBackground(URL... params) {
			LogUtil.d(MainActivity_Event.Tag + "-----doInBackground-----");
			BufferedReader bf = null;
			// 定义一个connection
			HttpURLConnection conn = null;
			InputStream in = null;
			try {
				conn = (HttpURLConnection) params[0].openConnection();
				conn.setConnectTimeout(5000);
				conn.setRequestMethod("GET");
				in = conn.getInputStream();

				// byte[] buf = new byte[2048];
				// int len = -1;
				// int completeSize = 0;
				// while ((len = in.read(buf)) != -1) {
				// completeSize += len;
				// publishProgress(completeSize);
				// }

				bf = new BufferedReader(new InputStreamReader(in));
//				Mlog.e(MainActivity_Event.Tag + "doInBackground size : " + bf.);
				StringBuffer sb = new StringBuffer();
				String line;
				while ((line = bf.readLine()) != null) {
					sb.append(line + "\n");
					hasRead++;
					publishProgress(hasRead);
				}
				return sb.toString();

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (in != null) {
						in.close();
					}

					if (bf != null) {
						bf.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		// 执行耗时操作前调用，用于初始化工作，比如在界面上显示进度条
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			LogUtil.d(MainActivity_Event.Tag + "-----onPreExecute-----");
			dialog = new ProgressDialog(mContext);
			dialog.setTitle("进度条对话框");
			dialog.setMessage("执行中...");
			dialog.setCancelable(true);
			dialog.setMax(100);
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			// 进度条是否显示
			dialog.setIndeterminate(true);
			dialog.show();

		}

		// 进度条改变时调用，进度条改变时调用
		// 在doInBackground调用publishProgress方法更新任务的执行进度后，将触发该方法
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			LogUtil.d(MainActivity_Event.Tag
					+ "-----onProgressUpdate values[0] : " + values[0]);
			dialog.setProgress(values[0]);
//			System.out.println(values[0]);
		}

		// 执行完后消失，当doInBackground执行完后，系统会自动调用onPostExecute（）方法
		// 并将onPostExecute的返回值传给该方法
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			LogUtil.d(MainActivity_Event.Tag + "-----onPostExecute result : "
					+ result);
			textView.setText(result);
		}
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
	}
}
