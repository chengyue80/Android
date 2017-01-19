package com.yue.demo.media;

import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.PresetReverb;
import android.media.audiofx.Visualizer;
import android.media.audiofx.Visualizer.OnDataCaptureListener;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.yue.demo.R;
import com.yue.demo.MainActivity;
import com.yue.demo.util.LogUtil;

/**
 * 音乐特效控制,setVolume
 * 
 * @author chengyue
 * 
 */
public class MediaPlayerTest extends Activity {

	private static final String tag = MediaPlayerTest.class.getSimpleName();

	/** 定义播放声音的MediaPlayer **/
	private MediaPlayer mPlayer;
	/** 定义系统的示波器 **/
	private Visualizer mVisualizer;
	/** 定义系统的均衡器 **/
	private Equalizer mEqualizer;
	/** 定义系统的重低音控制器 **/
	private BassBoost mBass;
	/** 定义系统的预设音场控制器 **/
	private PresetReverb mPresetReverb;
	private LinearLayout layout;
	private List<Short> reverbNames = new ArrayList<Short>();
	private List<String> reverbVals = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置控制音乐声音
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		layout = new LinearLayout(this);
		layout.setLayoutParams(MainActivity.layoutParams_mm);
		layout.setOrientation(LinearLayout.VERTICAL);
		setContentView(layout);

		// 创建MediaPlayer对象
		mPlayer = MediaPlayer.create(this, R.raw.earth);
		// mPlayer = new MediaPlayer();
		// mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		// FileDescriptor fd = getResources().get
		// mPlayer.setDataSource(fd, offset, length)
		// mPlayer.prepare();
		// mPlayer.setVolume(0.1f, 0.1f);
		// 播放
		// mPlayer.start();

		// 初始化示波器
		setupVisualizer();
		// 初始化均衡控制器
		setupEqualizer();
		// 初始化重低音控制器
		setupBassBoost();
		// 初始化预设音场控制器
		setupPresetReverb();
		// 开发播放音乐
		mPlayer.start();

	}

	/**
	 * 初始化示波器
	 */
	private void setupVisualizer() {

		final MyVisualizerView mVisualizerView = new MyVisualizerView(this);

		// mVisualizerView.setLayoutParams(new ViewGroup.LayoutParams(
		// ViewGroup.LayoutParams.MATCH_PARENT,
		// (int) (120f * getResources().getDisplayMetrics().density)));

		int hight = (int) (120f * getResources().getDisplayMetrics().density);
		LogUtil.d(tag, "density : " + getResources().getDisplayMetrics().density);

		mVisualizerView.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, hight));

		// 将MyVisualizerView组件添加到layout容器中
		layout.addView(mVisualizerView);

		// 以MediaPlayer的AudioSessionId创建Visualizer
		// 相当于设置Visualizer负责显示该MediaPlayer的音频数据
		mVisualizer = new Visualizer(mPlayer.getAudioSessionId());
		mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
		mVisualizer.setDataCaptureListener(new OnDataCaptureListener() {

			@Override
			public void onWaveFormDataCapture(Visualizer visualizer,
					byte[] waveform, int samplingRate) {
				// 用waveform波形数据更新mVisualizerView组件
				// Mlog.d(tag, "waveform[] : " + waveform);
				mVisualizerView.updateVisualizer(waveform);
			}

			@Override
			public void onFftDataCapture(Visualizer visualizer, byte[] fft,
					int samplingRate) {

			}
		}, Visualizer.getMaxCaptureRate() / 2, true, false);

		mVisualizer.setEnabled(true);

	}

	/**
	 * 初始化均衡控制器
	 */
	private void setupEqualizer() {

		mEqualizer = new Equalizer(0, mPlayer.getAudioSessionId());
		// 启用均衡控制效果
		mEqualizer.setEnabled(true);

		TextView textView = new TextView(this);
		textView.setText("均衡器：");
		layout.addView(textView);

		// 获取均衡控制器支持最小值和最大值
		final short minEQLevel = mEqualizer.getBandLevelRange()[0];
		short maxEQLevel = mEqualizer.getBandLevelRange()[1];
		// 获取均衡控制器支持的所有频率

		short bands = mEqualizer.getNumberOfBands();
		for (short i = 0; i < bands; i++) {
			TextView eqTextView = new TextView(this);
			// 显示频率
			eqTextView.setLayoutParams(MainActivity.layoutParams_mw);
			eqTextView.setLayoutParams(new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT));
			eqTextView.setGravity(Gravity.CENTER_HORIZONTAL);
			eqTextView.setText(mEqualizer.getCenterFreq(i) / 1000 + " Hz");
			layout.addView(eqTextView);
			// 创建一个水平排列组件的LinearLayout

			LinearLayout tmpLayout = new LinearLayout(this);
			tmpLayout.setOrientation(LinearLayout.HORIZONTAL);
			// 显示均衡器最小值
			TextView minDbTv = new TextView(this);
			minDbTv.setLayoutParams(MainActivity.layoutParams_ww);
			minDbTv.setText(minEQLevel / 100 + " dB");

			// 显示均衡器最大值
			TextView maxDbTv = new TextView(this);
			maxDbTv.setLayoutParams(MainActivity.layoutParams_ww);
			maxDbTv.setText(maxEQLevel / 100 + " dB");

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.weight = 1;
			// 定义SeekBar做为调整工具
			SeekBar bar = new SeekBar(this);
			bar.setLayoutParams(layoutParams);
			bar.setMax(maxEQLevel - minEQLevel);
			bar.setProgress(mEqualizer.getBandLevel(i));

			final short band = i;

			bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {

				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {

				}

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					// 设置该频率的均衡值
					mEqualizer.setBandLevel(band,
							(short) (minEQLevel + progress));
				}
			});

			tmpLayout.addView(minDbTv);
			tmpLayout.addView(bar);
			tmpLayout.addView(maxDbTv);

			layout.addView(tmpLayout);

		}

	}

	/**
	 * 初始化重低音控制器
	 */
	private void setupBassBoost() {

		mBass = new BassBoost(0, mPlayer.getAudioSessionId());
		mBass.setEnabled(true);

		TextView bbTextView = new TextView(this);
		bbTextView.setText("重低音：");
		layout.addView(bbTextView);

		SeekBar bar = new SeekBar(this);
		// 重低音的范围为0～1000
		bar.setMax(1000);
		bar.setProgress(0);
		layout.addView(bar);

		bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

				mBass.setStrength((short) progress);
			}
		});

	}

	/**
	 * 初始化预设音场控制器
	 */
	private void setupPresetReverb() {

		mPresetReverb = new PresetReverb(0, mPlayer.getAudioSessionId());
		mPresetReverb.setEnabled(true);

		TextView prTitle = new TextView(this);
		prTitle.setText("音场: ");
		layout.addView(prTitle);

		// 获取系统支持的所有预设音场
		for (short i = 0; i < mEqualizer.getNumberOfPresets(); i++) {
			LogUtil.d(tag, "reverbNames ： " + i + "   reverbValues : "
					+ mEqualizer.getPresetName(i));
			reverbNames.add(i);
			reverbVals.add(mEqualizer.getPresetName(i));
		}

		// 使用Spinner做为音场选择工具
		final Spinner spinner = new Spinner(this);
		layout.addView(spinner);
		spinner.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, reverbVals));

		spinner.setContentDescription(reverbVals.get(0));
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				mPresetReverb.setPreset(reverbNames.get(position));
				spinner.setContentDescription(reverbVals.get(position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (isFinishing() && mPlayer != null) {
			// 释放所有对象
			mVisualizer.release();
			mEqualizer.release();
			mPresetReverb.release();
			mBass.release();
			mPlayer.release();
			mPlayer = null;
		}
	}

	public static class MyVisualizerView extends View {
		// bytes数组保存了波形抽样点的值
		private byte[] bytes;
		private float[] points;
		private Paint paint = new Paint();
		private Rect rect = new Rect();
		private byte type = 0;

		public MyVisualizerView(Context context) {
			super(context);

			bytes = null;
			paint.setStrokeWidth(1f);
			paint.setAntiAlias(true);
			paint.setColor(Color.GREEN);
			paint.setStyle(Style.FILL);
		}

		public void updateVisualizer(byte[] ftt) {
			bytes = ftt;
			// 通知该组件重绘自己。
			invalidate();
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {

			// 当用户触碰该组件时，切换波形类型
			if (event.getAction() != MotionEvent.ACTION_DOWN)
				return false;

			type++;

			if (type >= 3) {
				type = 0;
			}
			return true;
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			if (bytes == null)
				return;

			canvas.drawColor(Color.WHITE);
			// 使用rect对象记录该组件的宽度和高度
			rect.set(0, 0, getWidth(), getHeight());

			switch (type) {
			// -------绘制块状的波形图-------
			case 0:
				for (int i = 0; i < bytes.length - 1; i++) {
					float left = getWidth() * i / (bytes.length - 1);
					// 根据波形值计算该矩形的高度
					float top = rect.height() - (byte) (bytes[i + 1] + 128)
							* rect.height() / 128;
//					MLog.d(tag, "top : " + top);
//					MLog.i(tag, "top : " + (-bytes[i+1] * rect.height()/128));
					float right = left + 1;
					float bottom = rect.height();

					// Mlog.i(tag, "left: " + left + "  top: " + top +
					// "  right: "
					// + right + "  bottom: " + bottom);
					canvas.drawRect(left, top, right, bottom, paint);
				}
				break;

			// -------绘制柱状的波形图（每隔18个抽样点绘制一个矩形）-------
			case 1:
				for (int i = 0; i < bytes.length - 1; i += 18) {
					float left = rect.width() * i / (bytes.length - 1);
					// 根据波形值计算该矩形的高度
					float top = rect.height() - (byte) (bytes[i + 1] + 128)
							* rect.height() / 128;
					float right = left + 6;
					float bottom = rect.height();
					// Mlog.i(tag, "left: " + left + "  top: " + top +
					// "  right: "
					// + right + "  bottom: " + bottom);
					canvas.drawRect(left, top, right, bottom, paint);
				}
				break;
			// -------绘制曲线波形图-------
			case 2:
				// 如果point数组还未初始化
				if (points == null || points.length < bytes.length * 4) {
					points = new float[bytes.length * 4];
				}
				for (int i = 0; i < bytes.length - 1; i++) {
					// 计算第i个点的x坐标
					points[i * 4] = rect.width() * i / (bytes.length - 1);
					// 根据bytes[i]的值（波形点的值）计算第i个点的y坐标
					points[i * 4 + 1] = (rect.height() / 2)
							+ ((byte) (bytes[i] + 128)) * 128
							/ (rect.height() / 2);
					// 计算第i+1个点的x坐标
					points[i * 4 + 2] = rect.width() * (i + 1)
							/ (bytes.length - 1);
					// 根据bytes[i+1]的值（波形点的值）计算第i+1个点的y坐标
					points[i * 4 + 3] = (rect.height() / 2)
							+ ((byte) (bytes[i + 1] + 128)) * 128
							/ (rect.height() / 2);
				}
				// 绘制波形曲线
				canvas.drawLines(points, paint);
				break;
			}
		}

	}
}
