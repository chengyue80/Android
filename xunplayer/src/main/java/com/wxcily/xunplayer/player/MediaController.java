package com.wxcily.xunplayer.player;

import com.wxcily.xunplayer.player.utils.StringUtils;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.wxcily.xunplayer.player.R;

public class MediaController extends FrameLayout {
	private MediaControllerListener mediaControllerListener;
	private Context context;
	private PopupWindow window;
	private View root;
	private RelativeLayout rootLayout;
	private RelativeLayout topLayout;
	private RelativeLayout bottomLayout;

	private View Anchor;

	private TextView video_name;
	private TextView time_text;
	private TextView download_speed;
	private ImageButton play_button;
	private TextView mediacontroller_time_current, mediacontroller_time_total;
	private TextView current_time;
	private SeekBar seekBar;

	private boolean download_speed_show = false;
	private boolean isDraging = false;
	private boolean isshow = false;
	private boolean video_time_know = false;
	private long video_total_time = 0;

	private Animation animSlideInTop;
	private Animation animSlideInBottom;
	private Animation animSlideOutTop;
	private Animation animSlideOutBottom;

	public interface MediaControllerListener {
		void start();

		void stop();

		void back();

		void seekTo(long pos);

		boolean isPlaying();

		long getDuration();

		long getCurrentPosition();

	}

	public MediaController(Context context) {
		super(context);
		this.context = context;
		initAnim();
		initView();
	}

	// 初始化动画
	private void initAnim() {
		animSlideOutBottom = AnimationUtils.loadAnimation(context,
				R.anim.slide_out_bottom);
		animSlideOutTop = AnimationUtils.loadAnimation(context,
				R.anim.slide_out_top);
		animSlideInBottom = AnimationUtils.loadAnimation(context,
				R.anim.slide_in_bottom);
		animSlideInTop = AnimationUtils.loadAnimation(context,
				R.anim.slide_in_top);
		animSlideOutBottom.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				rootLayout.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});
	}

	private void initView() {
		root = LayoutInflater.from(context).inflate(
				R.layout.layout_mediacontroller, this);
		topLayout = (RelativeLayout) root.findViewById(R.id.toplayout);
		bottomLayout = (RelativeLayout) root.findViewById(R.id.bottomlayout);

		window = new PopupWindow(context);
		window.setFocusable(true);
		window.setBackgroundDrawable(null);
		window.setOutsideTouchable(true);
		window.setContentView(root);
		window.setWidth(android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		window.setHeight(android.view.ViewGroup.LayoutParams.MATCH_PARENT);

		rootLayout = (RelativeLayout) root.findViewById(R.id.root);
		rootLayout.setVisibility(View.GONE);

		root.findViewById(R.id.back_button).setOnClickListener(onClickListener);
		video_name = (TextView) root.findViewById(R.id.video_name);
		time_text = (TextView) root.findViewById(R.id.time_text);
		download_speed = (TextView) root.findViewById(R.id.download_speed);
		download_speed.setVisibility(View.GONE);
		play_button = (ImageButton) root.findViewById(R.id.play_button);
		play_button.setOnClickListener(onClickListener);
		mediacontroller_time_current = (TextView) root
				.findViewById(R.id.time_current);
		mediacontroller_time_total = (TextView) root
				.findViewById(R.id.time_total);
		seekBar = (SeekBar) root.findViewById(R.id.seekbar);
		seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
		current_time = (TextView) root.findViewById(R.id.current_time);
		findViewById(R.id.houtui).setOnClickListener(onClickListener);
		findViewById(R.id.qianjin).setOnClickListener(onClickListener);
	}

	public void setVideoName(String name) {
		video_name.setText(name);
	}

	public void setVideoTime(int seconds) {
		if (seconds != 0) {
			video_time_know = true;
			video_total_time = (long) seconds * 1000;
			mediacontroller_time_total.setText(StringUtils
					.generateTime(video_total_time));
		}
	}

	public void setDownLoadSpeed(String name) {
		download_speed.setText(name);
		if (!download_speed_show) {
			download_speed.setVisibility(View.VISIBLE);
			download_speed_show = true;
		}

	}

	public void setAnchorView(View view) {
		Anchor = view;
		int[] location = new int[2];
		Anchor.getLocationOnScreen(location);
		Rect anchorRect = new Rect(location[0], location[1], location[0]
				+ Anchor.getWidth(), location[1] + Anchor.getHeight());
		window.showAtLocation(Anchor, Gravity.NO_GRAVITY, anchorRect.left,
				anchorRect.bottom);
	}

	public void setMediaPlayer(MediaControllerListener mediaControllerListener) {
		this.mediaControllerListener = mediaControllerListener;
		updatePausePlay();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int keyCode = event.getKeyCode();
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			release();
			mediaControllerListener.back();
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	private static final int MSG_SHOW = 1;
	private static final int MSG_HIDE = 2;
	private static final int MSG_UPDATE_PLAY_TIME = 3;
	private static final int MSG_UPDATE_SEEK_BAR = 4;
	private static final int MSG_TIME_TICK = 5;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SHOW:
				if (!video_time_know) {
					video_total_time = mediaControllerListener.getDuration();
					mediacontroller_time_total.setText(StringUtils
							.generateTime(video_total_time));
				}
				if (!isshow) {
					bottomLayout.startAnimation(animSlideInTop);
					topLayout.startAnimation(animSlideInBottom);
				}
				isshow = true;
				updatePausePlay();
				handler.removeMessages(MSG_HIDE);
				handler.sendEmptyMessage(MSG_TIME_TICK);
				handler.sendEmptyMessage(MSG_UPDATE_PLAY_TIME);
				handler.sendEmptyMessage(MSG_UPDATE_SEEK_BAR);
				handler.sendEmptyMessageDelayed(MSG_HIDE,
						PlayerConfig.DEFAULT_SHOW_TIME);
				rootLayout.setVisibility(View.VISIBLE);
				break;
			case MSG_HIDE:
				isshow = false;
				isDraging = false;
				handler.removeMessages(MSG_TIME_TICK);
				handler.removeMessages(MSG_UPDATE_PLAY_TIME);
				handler.removeMessages(MSG_UPDATE_SEEK_BAR);
				bottomLayout.startAnimation(animSlideOutTop);
				topLayout.startAnimation(animSlideOutBottom);
				break;
			case MSG_UPDATE_PLAY_TIME:
				if (!isDraging) {
					long currentPosition = mediaControllerListener
							.getCurrentPosition();
					mediacontroller_time_current.setText(StringUtils
							.generateTime(currentPosition));
				}
				handler.sendEmptyMessageDelayed(MSG_UPDATE_PLAY_TIME,
						PlayerConfig.DEFAULT_TIME_REFFRESH);
				break;
			case MSG_UPDATE_SEEK_BAR:
				if (!isDraging) {
					long position = mediaControllerListener
							.getCurrentPosition();
					long pos = 1000L * position / video_total_time;
					seekBar.setProgress((int) pos);
				}
				handler.sendEmptyMessageDelayed(MSG_UPDATE_SEEK_BAR,
						PlayerConfig.DEFAULT_TIME_REFFRESH);
				break;
			case MSG_TIME_TICK:
				time_text.setText(StringUtils.currentTimeString());
				handler.sendEmptyMessageDelayed(MSG_TIME_TICK,
						PlayerConfig.DEFAULT_TIME_REFFRESH);
				break;
			}
			super.handleMessage(msg);
		}
	};

	public void release() {
		if (window != null) {
			window.dismiss();
			window = null;
		}
		handler.removeMessages(MSG_SHOW);
		handler.removeMessages(MSG_HIDE);
		handler.removeMessages(MSG_UPDATE_PLAY_TIME);
		handler.removeMessages(MSG_UPDATE_SEEK_BAR);
		handler.removeMessages(MSG_TIME_TICK);
	}

	public void show() {
		handler.sendEmptyMessage(MSG_SHOW);
	}

	public void updatePausePlay() {
		if (mediaControllerListener.isPlaying())
			play_button
					.setImageResource(R.drawable.player_mediacontroller_pause);
		else
			play_button
					.setImageResource(R.drawable.player_mediacontroller_play);
	}

	private void doPauseResume() {
		if (mediaControllerListener.isPlaying())
			mediaControllerListener.stop();
		else
			mediaControllerListener.start();
		updatePausePlay();
	}

	// private final int BACK = 2131558396;
	// private final int PLAY = 2131558397;
	// private final int QIANJIN = 2131558398;
	// private final int HOUTUI = 2131558399;

	OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.back_button) {

				if (mediaControllerListener != null) {
					release();
					mediaControllerListener.back();
				}
			}
			if (v.getId() == R.id.houtui) {
				show();
				long position2 = mediaControllerListener.getCurrentPosition();
				position2 = position2 - 20000L;
				mediaControllerListener.seekTo(position2);

			}
			if (v.getId() == R.id.play_button) {
				show();
				doPauseResume();

			}
			if (v.getId() == R.id.qianjin) {
				show();
				long position = mediaControllerListener.getCurrentPosition();
				position = position + 20000L;
				mediaControllerListener.seekTo(position);

			}
			// switch (v.getId()) {
			// case BACK:
			// if (mediaControllerListener != null) {
			// release();
			// mediaControllerListener.back();
			// }
			// break;
			// case PLAY:
			// show();
			// doPauseResume();
			// break;
			// case QIANJIN:
			// show();
			// long position = mediaControllerListener.getCurrentPosition();
			// position = position + 20000L;
			// mediaControllerListener.seekTo(position);
			// break;
			// case HOUTUI:
			// show();
			// long position2 = mediaControllerListener.getCurrentPosition();
			// position2 = position2 - 20000L;
			// mediaControllerListener.seekTo(position2);
			// break;
			// }
		}
	};

	private OnSeekBarChangeListener onSeekBarChangeListener = new OnSeekBarChangeListener() {
		long newposition = 0;

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			mediaControllerListener.seekTo(newposition);
			current_time.setVisibility(View.GONE);
			isDraging = false;
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			isDraging = true;
			mediaControllerListener.stop();
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			Log.d("TAG", "progress : " + progress);
			if (isDraging) {
				show();
				newposition = (video_total_time * progress) / 1000;
				mediacontroller_time_current.setText(StringUtils
						.generateTime(newposition));
				current_time.setText(StringUtils.generateTime(newposition));
				current_time.setVisibility(View.VISIBLE);
			}
		}
	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isshow) {
			handler.removeMessages(MSG_HIDE);
			handler.sendEmptyMessageDelayed(MSG_HIDE,
					PlayerConfig.DEFAULT_SHOW_TIME);
		} else {
			show();
		}
		return super.onTouchEvent(event);
	}

}
