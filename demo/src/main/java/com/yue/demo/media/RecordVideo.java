package com.yue.demo.media;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.yue.demo.MainActivity;
import com.yue.demo.util.LogUtil;

/**
 * 使用MediaRecorder录制视频
 * 
 * @author chengyue
 * 
 */
public class RecordVideo extends Activity {

	private final String tag = RecordVideo.class.getSimpleName();
	private SurfaceView surfaceView;
	private Button record, stop;
	private SurfaceHolder holder;
	private MediaRecorder mRecorder;
	private boolean isRecording = false;
	// 系统的视频文件
	File videoFile;
	private final String PATH = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/DCIM/Camera/";
	private final String fileName = "test";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();

		holder = surfaceView.getHolder();
		// 设置分辨率
		holder.setFixedSize(640, 560);
		holder.setKeepScreenOn(true);

		holder.addCallback(new Callback() {

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {

			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {

			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {

			}
		});

		record.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!Environment.getExternalStorageState().equals(
						android.os.Environment.MEDIA_MOUNTED)) {
					Toast.makeText(RecordVideo.this, "SD卡不存在，请插入SD卡！",
							Toast.LENGTH_SHORT).show();
					return;
				}

				videoFile = new File(PATH + fileName + ".mp4");

				mRecorder = new MediaRecorder();
//				mRecorder.reset();
				mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
				// 设置视频文件的输出格式
				// 必须在设置声音编码格式、图像编码格式之前设置
				mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
				mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
				mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);

				mRecorder.setVideoSize(640, 560);
				mRecorder.setVideoFrameRate(10);

				LogUtil.d(tag, "path : " + videoFile.getAbsolutePath());
				mRecorder.setOutputFile(videoFile.getAbsolutePath());

				mRecorder.setPreviewDisplay(holder.getSurface());

				try {
					mRecorder.prepare();

					mRecorder.start();
					LogUtil.d(tag, "----recoding----");
					record.setEnabled(false);
					stop.setEnabled(true);
					isRecording = true;
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

				if (isRecording) {

					mRecorder.stop();
					mRecorder.release();
					mRecorder = null;
					record.setEnabled(true);
					stop.setEnabled(false);
				}

			}
		});

	}

	private void initView() {

		RelativeLayout rl = new RelativeLayout(this);
		rl.setLayoutParams(MainActivity.layoutParams_mm);
		// ll.setOrientation(LinearLayout.VERTICAL);

		// MainActivity.layoutParams_mm.weight = 1;
		surfaceView = new SurfaceView(this);
		surfaceView.setLayoutParams(MainActivity.layoutParams_mm);
		rl.addView(surfaceView);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);

		// capture 位于父 View 的底部，在父 View 中水平居中
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setGravity(Gravity.CENTER_HORIZONTAL);
		rl.addView(ll, params);

		record = new Button(this);
		record.setLayoutParams(MainActivity.layoutParams_ww);
		record.setText("录制");
		ll.addView(record);

		stop = new Button(this);
		stop.setText("停止");
		stop.setLayoutParams(MainActivity.layoutParams_ww);
		stop.setEnabled(false);
		ll.addView(stop);

		// lp3.addRule(RelativeLayout.BELOW, 1);
		// lp3.addRule(RelativeLayout.RIGHT_OF, 2);
		// lp3.addRule(RelativeLayout.ALIGN_RIGHT, 1);
		// // btn3 位于 btn1 的下方、btn2 的右方且其右边和 btn1 的右边对齐（要扩充）

		setContentView(rl);

	}
}
