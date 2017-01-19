package com.yue.demo.media;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yue.demo.MainActivity;
import com.yue.demo.other.MainActivity_other;
import com.yue.demo.util.ConstantUtil;
import com.yue.demo.util.FileUtil;
import com.yue.demo.util.LogUtil;

/**
 * 使用MediaRecorder录制音频
 * 
 * @author chengyue
 * 
 */
public class MediaRecorderTest extends Activity {

	private MediaRecorder mRecorder = null;
	private MediaPlayer mPlayer = null;
	private int CURR_POSITION = 0;
	private String SAVE_PATH = ConstantUtil.MEDIA;
	private String fileName = "recorder.mp3";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout ll = new LinearLayout(this);
		ll.setLayoutParams(MainActivity.layoutParams_mm);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setGravity(Gravity.CENTER_HORIZONTAL);

		final Button start = new Button(this);
		start.setLayoutParams(MainActivity.layoutParams_ww);
		start.setText("开始录音");
		ll.addView(start);

		Button stop = new Button(this);
		stop.setLayoutParams(MainActivity.layoutParams_ww);
		stop.setText("停止录音");
		ll.addView(stop);

		final Button play = new Button(this);
		play.setLayoutParams(MainActivity.layoutParams_ww);
		play.setText("播放录音");
		ll.addView(play);

		Button stop1 = new Button(this);
		stop1.setLayoutParams(MainActivity.layoutParams_ww);
		stop1.setText("停止播放");
		ll.addView(stop1);

		setContentView(ll);

		try {
			FileUtil.createDir(SAVE_PATH);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		mPlayer = new MediaPlayer();

		// mPlayer.setOnCompletionListener(new OnCompletionListener() {
		//
		// @Override
		// public void onCompletion(MediaPlayer mp) {
		//
		// }
		// });
		start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (mRecorder == null) {
					// new出MediaRecorder对象
					mRecorder = new MediaRecorder();
				}
				// 设置MediaRecorder的音频源为麦克风
				mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				// 设置MediaRecorder录制的音频格式
				mRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
				// 设置MediaRecorder录制音频的编码为amr.貌似android就支持amr编码。
				mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
				// 设置录制好的音频文件保存路径
				mRecorder.setOutputFile(SAVE_PATH + fileName);

				// mRecorder.setOutputFile(SAVE_PATH);
				try {
					mRecorder.prepare();// 准备录制
					mRecorder.start();// 开始录制
					start.setEnabled(false);
					play.setEnabled(false);

					// mRecorder.setMaxFileSize()
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		stop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mRecorder.stop();// 停止刻录
				// recorder.reset(); // 重新启动MediaRecorder.
				mRecorder.release(); // 刻录完成一定要释放资源
				mRecorder = null;
				start.setEnabled(true);
				play.setEnabled(true);
			}
		});

		play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				canRecord();
				isEnoughForDownload(20);

				try {
					File file = new File(SAVE_PATH + fileName);
					if (!file.exists()) {

						Toast.makeText(MediaRecorderTest.this, "音频文件不存在，无法播放",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if (mPlayer.isPlaying()) {
						play.setEnabled(false);
						return;
					}
					mPlayer.reset();
					mPlayer.setDataSource(SAVE_PATH + fileName);
					// mPlayer.setVideoScalingMode(mode)
					mPlayer.prepare();
					mPlayer.seekTo(CURR_POSITION);
					mPlayer.start();
					CURR_POSITION = 0;
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});

		stop1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (mPlayer != null && mPlayer.isPlaying()) {
					CURR_POSITION = mPlayer.getCurrentPosition();
					LogUtil.d(MainActivity_other.Tag + "paly position ： "
							+ CURR_POSITION);
					mPlayer.stop();

				}
				play.setEnabled(true);
			}
		});
	}

	private boolean canRecord() {

		LogUtil.d(MainActivity_other.Tag + "sdcard : "
				+ Environment.getExternalStorageState());
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(MediaRecorderTest.this,
					"ExternalStorage is not exsit !", Toast.LENGTH_SHORT)
					.show();
		}

		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	private String getDirectory() {
		LogUtil.d(MainActivity_other.Tag + "dir : "
				+ Environment.getExternalStorageDirectory().getPath());

		String path = Environment.getExternalStorageDirectory().getParent();

		LogUtil.d(MainActivity_other.Tag + "getParent dir : " + path);

		File file = new File(path);
		File[] files = file.listFiles();

		for (File file2 : files) {

			LogUtil.i("MediaRecorderTest", MainActivity_other.Tag
					+ "file name : " + file2.getPath());
		}
		return Environment.getExternalStorageDirectory().getPath();
	}

	/**
	 * 判断是否有足够的空间供下载
	 * 
	 * @param downloadSize
	 *            单位 ：mb
	 * @return
	 */
	public boolean isEnoughForDownload(long downloadSize) {

		StatFs statFs = new StatFs(Environment.getExternalStorageDirectory()
				.getAbsolutePath());

		// sd卡分区数
		int blockCounts = statFs.getBlockCount();

		LogUtil.d(MainActivity_other.Tag + "blockCounts : " + blockCounts);

		// sd卡可用分区数
		int avCounts = statFs.getAvailableBlocks();

		LogUtil.d(MainActivity_other.Tag + "avCounts : " + avCounts);

		// 一个分区数的大小
		long blockSize = statFs.getBlockSize();

		LogUtil.d(MainActivity_other.Tag + "blockSize : " + blockSize);

		// sd卡可用空间
		long spaceLeft = avCounts * blockSize;

		LogUtil.d(MainActivity_other.Tag + "spaceLeft : " + spaceLeft / 1024
				/ 1024 + " MB");

		LogUtil.d(MainActivity_other.Tag + "downloadSize : " + downloadSize);

		if (spaceLeft < downloadSize) {
			return false;
		}

		return true;
	}

	@Override
	public void finish() {
		if (mRecorder != null) {
			mRecorder.stop();// 停止刻录
			// recorder.reset(); // 重新启动MediaRecorder.
			mRecorder.release(); // 刻录完成一定要释放资源
			mRecorder = null;
		}

		if (mPlayer != null && mPlayer.isPlaying()) {
			mPlayer.stop();
			mPlayer.release();
		}
		super.finish();
	}
}
