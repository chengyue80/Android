package com.yue.demo.service;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yue.demo.R;
import com.yue.demo.util.LogUtil;

public class MusicList extends Activity implements OnScanCompletedListener {

	private ListView listView;
	private Cursor cursor;
	private String[] projection = { MediaStore.Audio.Media._ID,
			MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST,
			MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DISPLAY_NAME,
			MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ALBUM_ID };

	/**
	 * 歌曲id
	 */
	private int[] ids = { 0, 1, 2 };
	/**
	 * 所有的歌曲名
	 */
	private String[] titles = new String[] { "心愿", "约定", "美丽新世界" };
	/**
	 * 所有的歌手名
	 */
	private String[] singers = new String[] { "未知艺术家", "周蕙", "伍佰" };

	Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music_list);

		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				LogUtil.d(MusicBox.tag, "----handleMessage start----");
				listView.setAdapter(new MusicAdapter(MusicList.this, cursor));

			}

		};
		listView = (ListView) findViewById(R.id.music_listView);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(MusicList.this, MusicBox.class);
				LogUtil.d(MusicBox.tag, "onItemClick position : " + position);
				intent.putExtra("position", position);
				intent.putExtra("titles", titles);
				intent.putExtra("singers", singers);
				intent.putExtra("ids", ids);
				startActivity(intent);
			}
		});

		/** 当媒体扫描完成后会发送广播给我的应用程序 ***/
		IntentFilter intentfilter = new IntentFilter();
		intentfilter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);// 开始扫描
		intentfilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);// 扫描结束
		intentfilter.addDataScheme("file");
		if (receiver != null) {
			registerReceiver(receiver, intentfilter);
		}

		/** 发送广播让mediaprovide去扫描sd卡 **/
		// sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
		// Uri.parse("file://"
		// + Environment.getExternalStorageDirectory()
		// .getAbsolutePath())));

		// MediaScannerConnection.scanFile(this, new String[] { Environment
		// .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
		// .getPath()
		// + "/" + fileName }, null, null);

		MediaScannerConnection.scanFile(this, new String[] { Environment
				.getExternalStorageDirectory().getPath() }, null, this);

	}

	// 初始化数据
	private void initData() {
		if (cursor.moveToFirst()) {
			titles = new String[cursor.getCount()];
			singers = new String[cursor.getCount()];
			ids = new int[cursor.getCount()];

			for (int i = 0; i < cursor.getCount(); i++) {

				ids[i] = cursor.getInt(0);
				titles[i] = cursor.getString(1);
				singers[i] = cursor.getString(2);
				LogUtil.i(MusicBox.tag, "id : " + ids[i] + "  title : "
						+ titles[i] + "  singer ： " + singers[i]);
				cursor.moveToNext();
			}
		}

	}

	/**
	 * 注册广播接收器接收列表刷新信息
	 */
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			LogUtil.d(MusicBox.tag, "----MusicList receiver ----");
			LogUtil.d(MusicBox.tag, "intent : " + Uri.decode(intent.toUri(0)));
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_MEDIA_SCANNER_FINISHED)) {
				Toast.makeText(context, "扫描完毕！", Toast.LENGTH_SHORT).show();
				// 系统music的列表的地址
				// Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				// cursor = getContentResolver().query(uri, projection, null,
				// null, null);
				//
				// initData();
				// listView.setAdapter(new MusicAdapter(context, cursor));
				//
				// listView.setOnItemClickListener(new OnItemClickListener() {
				//
				// @Override
				// public void onItemClick(AdapterView<?> parent, View view,
				// int position, long id) {
				// Intent intent = new Intent(MusicList.this,
				// MusicBox.class);
				// intent.putExtra("position", position);
				// intent.putExtra("titles", titles);
				// intent.putExtra("singers", singers);
				// intent.putExtra("ids", ids);
				// startActivity(intent);
				// }
				// });
			}
		}
	};

	class MusicAdapter extends BaseAdapter {

		private Context context;
		private Cursor cursor;

		public MusicAdapter(Context context, Cursor cursor) {
			this.context = context;
			this.cursor = cursor;
		}

		public MusicAdapter(MusicList context, int[] ids) {
			this.context = context;

		}

		@Override
		public int getCount() {
			return cursor.getCount();
			// return ids.length;
		}

		@Override
		public Object getItem(int position) {
			return cursor.getString(0);
			// return ids[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = LayoutInflater.from(context);
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.music_item, null);
				holder = new ViewHolder();
				// 歌名
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.music_item_tv_title);
				// 时间
				holder.tvTime = (TextView) convertView
						.findViewById(R.id.music_item_tv_time);
				// 歌手
				holder.tvSinger = (TextView) convertView
						.findViewById(R.id.music_item_tv_singer);
				// 图片
				holder.imageView = (ImageView) convertView
						.findViewById(R.id.music_item_iv);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			cursor.moveToPosition(position);
			String title = cursor.getString(0) + "." + cursor.getString(1);
			holder.tvTitle.setText(title.length() > 25 ? title.subSequence(0,
					25) + "..." : title);
			int time = cursor.getInt(5);
			holder.tvTime.setText(format(time));
			holder.tvSinger.setText(cursor.getString(2));

			// String title = ids[position]++ + "." + titles[position];
			// holder.tvTitle.setText(title.length() > 25 ? title.subSequence(0,
			// 25) + "..." : title);
			// holder.tvTime.setText(format(100));
			// holder.tvSinger.setText(singers[position]);
			return convertView;
		}

		class ViewHolder {
			TextView tvTitle;
			TextView tvTime;
			TextView tvSinger;
			ImageView imageView;
		}
	}

	public String format(int time) {
		int min = time / 1000 / 60;
		int sec = time / 1000 % 60;
		return String.format("%d:%02d", min, sec);
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(receiver);
		super.onDestroy();
	}

	@Override
	public void onScanCompleted(String path, Uri uri1) {
		LogUtil.d(MusicBox.tag, "----onScanCompleted----\n path : " + path
				+ "\nuri : " + uri1);

		Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		cursor = getContentResolver().query(uri, projection, null, null, null);

		initData();
		// listView.setAdapter(new MusicAdapter(MusicList.this, cursor));
		handler.sendEmptyMessage(1);
		LogUtil.d(MusicBox.tag, "----onScanCompleted end----");

	}

}
