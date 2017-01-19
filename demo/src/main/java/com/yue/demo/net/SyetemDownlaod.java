package com.yue.demo.net;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.yue.demo.R;
import com.yue.demo.util.LogUtil;

public class SyetemDownlaod extends Activity {

	private final String TAG = SyetemDownlaod.class.getSimpleName();

	private Button button, button2, button3, button4;
	DownloadManager downloadManager;
	private CompleteReceiver completeReceiver;

	List<String> ids = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		button = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		button3 = (Button) findViewById(R.id.button3);
		button4 = (Button) findViewById(R.id.button4);

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String apkUrl = "http://img.meilishuo.net/css/images/AndroidShare/Meilishuo_3.6.1_10006.apk";
				downLoad(apkUrl);

			}
		});

		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (downloadManager != null && ids.size() > 0) {
					LogUtil.d(TAG, "ids.size() : " + ids.size());
					for (String id : ids) {
						LogUtil.i(TAG, "download id : " + id);
						downloadManager.remove(Long.parseLong(id));
					}
				}
			}
		});
		button3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String apkUrl = "http://60.174.83.177:8081/download/publicinspect.apk";
				downLoad(apkUrl);

			}
		});

		// completeReceiver = new CompleteReceiver();
		// /** register download success broadcast **/
		// registerReceiver(completeReceiver, new IntentFilter(
		// DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	}

	protected void downLoad(String apkUrl) {

		downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		// String apkUrl =
		// "http://img.meilishuo.net/css/images/AndroidShare/Meilishuo_3.6.1_10006.apk";
		DownloadManager.Request request = new DownloadManager.Request(
				Uri.parse(apkUrl));

		String[] data = apkUrl.split(File.separator);
		LogUtil.d("DOWNLOAD", "appName  : " + data[data.length - 1]);
		Environment.getExternalStoragePublicDirectory("BZFamily");
		File folder = Environment.getExternalStorageDirectory();
		LogUtil.d("DOWNLOAD", "foldName : " + folder.getAbsolutePath());

		if (!(folder.exists() && folder.isDirectory())) {
			folder.mkdirs();
		}

		// 表示设置下载地址为sd卡的Trinea文件夹，文件名为MeiLiShuo.apk。
		request.setDestinationInExternalPublicDir("Trinea", "MeiLiShuo.apk");
		// 表示允许MediaScanner扫描到这个文件，默认不允许。
		request.allowScanningByMediaScanner();
		// 设置下载中通知栏提示的标题
		request.setTitle("MeiLiShuo");
		// 设置下载中通知栏提示的介绍
		request.setDescription("MeiLiShuo desc");
		request.setVisibleInDownloadsUi(true);
		// 表示下载进行中和下载完成的通知栏是否显示。默认只显示下载中通知。
		// VISIBILITY_VISIBLE_NOTIFY_COMPLETED表示下载完成后显示通知栏提示。
		// VISIBILITY_HIDDEN表示不显示任何通知栏提示，
		// 这个需要在AndroidMainfest中添加权限android.permission.DOWNLOAD_WITHOUT_NOTIFICATION.
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		// request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
		// request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
		// request.setMimeType("application/cn.trinea.download.file");
		long downloadId = downloadManager.enqueue(request);
		LogUtil.d(TAG, "start id : " + downloadId);
		ids.add(String.valueOf(downloadId));

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
