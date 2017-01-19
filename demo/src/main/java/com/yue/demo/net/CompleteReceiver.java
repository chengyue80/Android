package com.yue.demo.net;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class CompleteReceiver extends BroadcastReceiver {

	private DownloadManager downloadManager;

	@Override
	public void onReceive(Context context, Intent intent) {
		// get complete download id
		long completeDownloadId = intent.getLongExtra(
				DownloadManager.EXTRA_DOWNLOAD_ID, -1);
		// to do here
		Toast.makeText(context, "下载完成，请点击安装", Toast.LENGTH_LONG).show();

		// TODO
		Log.d("DOWNLOAD", "completeDownloadId ： " + completeDownloadId);

		downloadManager = (DownloadManager) context
				.getSystemService(Context.DOWNLOAD_SERVICE);
		Query query = new Query();
		query.setFilterById(completeDownloadId);
		Cursor cursor = downloadManager.query(query);

		int columncCount = cursor.getColumnCount();

		while (cursor.moveToNext()) {

			for (int i = 0; i < columncCount; i++) {
				String columnName = cursor.getColumnName(i);
				String value = cursor.getString(i);
				if (columnName.equals("hint")) {
					Log.e("path : ", value);
					Intent intent2 = new Intent();
					intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent2.setAction(android.content.Intent.ACTION_VIEW);
					intent2.setDataAndType(Uri.parse(value),
							"application/vnd.android.package-archive");
					context.startActivity(intent2);
				}
				if (value != null) {
					System.out.println(columnName + ": " + value);
				} else {
					System.out.println(columnName + ": null");
				}
			}

		}
	}
};
