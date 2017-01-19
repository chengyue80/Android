package com.wxcily.xunplayer.player.ui;

import com.wxcily.xunplayer.player.MediaController;
import com.wxcily.xunplayer.player.MediaController.MediaControllerListener;
import com.wxcily.xunplayer.player.PlayerConfig;
import com.wxcily.xunplayer.player.R;
import com.wxcily.xunplayer.player.VideoService;
import com.wxcily.xunplayer.player.VideoService.PlayerListener;
import com.wxcily.xunplayer.player.VideoView;
import com.wxcily.xunplayer.player.VideoView.SurfaceCallback;
import com.wxcily.xunplayer.player.utils.NetworkUtil;
import com.wxcily.xunplayer.player.utils.StorageUtils;
import com.wxcily.xunplayer.player.utils.ToastFactory;

import io.vov.vitamio.MediaPlayer;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VideoActivity extends Activity implements SurfaceCallback,
		PlayerListener {
	private boolean created = false;
	private boolean surfaceCreated = false;// surface是否被创建
	private boolean serviceConnected = false;// service绑定状态

	private String[] video_path;
	private int video_time;
	private String video_name;
	private long startpos;
	private boolean Ishwcoder, network;

	private VideoService videoService;
	private VideoView videoView;
	private LinearLayout loadingLayout;
	private TextView loadingMessage;

	private MediaController mediaController;

	private ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			videoService = ((VideoService.LocalBinder) service).getService();
			serviceConnected = true;
			if (surfaceCreated) {
				handler.sendEmptyMessage(OPEN_FILE);
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			serviceConnected = false;
			videoService = null;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this))
			return;
		StorageUtils.getOwnCacheDirectory(getApplicationContext(),
				PlayerConfig.VIDEO_CACHE_DIR);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		parseIntent(getIntent());
		loadView(R.layout.activity_video);
		created = true;
	}

	private void parseIntent(Intent i) {
		video_path = i.getExtras().getStringArray("video_path");
		video_name = i.getExtras().getString("video_name");
		video_time = i.getIntExtra("video_time", 0);
		startpos = i.getLongExtra("startpos", 0);
		Ishwcoder = i.getBooleanExtra("hwcoder", false);
		network = i.getBooleanExtra("network", true);
	}

	private void loadView(int id) {
		setContentView(id);
		getWindow().setBackgroundDrawable(null);
		loadingLayout = (LinearLayout) findViewById(R.id.loading);
		loadingMessage = (TextView) findViewById(R.id.loading_message);
		videoView = (VideoView) findViewById(R.id.videoView);
		videoView.initialize(this, this, Ishwcoder);
	}

	// surface 监听
	@Override
	public void onSurfaceCreated(SurfaceHolder holder) {
		surfaceCreated = true;
		if (serviceConnected)
			handler.sendEmptyMessage(OPEN_FILE);
	}

	@Override
	public void onSurfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if (videoService != null) {
			setVideoLayout();
		}
	}

	@Override
	public void onSurfaceDestroyed(SurfaceHolder holder) {
		if (videoService != null && videoService.isInitialized()) {
			if (videoService.isPlaying()) {
				videoService.stop();
			}
		}
	}

	private static final int OPEN_FILE = 0;
	private static final int OPEN_START = 1;
	private static final int OPEN_SUCCESS = 2;
	private static final int OPEN_FAILED = 3;
	private static final int HW_FAILED = 4;
	private static final int BUFFER_START = 5;
	private static final int BUFFER_PROGRESS = 6;
	private static final int BUFFER_COMPLETE = 7;
	private static final int SEEK_START = 8;
	private static final int SEEK_COMPLETE = 9;

	// Hander
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case OPEN_FILE:
				videoService.setPlayerListener(VideoActivity.this);
				if (videoView != null)
					videoService.setDisplay(videoView.getHolder());
				if (!videoService.isInitialized()) {
					videoService.initialize(VideoActivity.this, video_path,
							startpos, Ishwcoder);
				}
				break;
			case OPEN_START:
				setVideoLoadingLayoutMessage(getResources().getString(
						R.string.player_loading));
				setVideoLoadingLayoutVisibility(View.VISIBLE);
				break;
			case OPEN_SUCCESS:
				loadPlayerConfig();
				setVideoLoadingLayoutVisibility(View.GONE);
				videoService.start();
				if (mediaController == null) {
					attachMediaController();
				}
				break;
			case OPEN_FAILED:
				Exit(getString(R.string.player_path_error));
				break;
			case BUFFER_START:
				setVideoLoadingLayoutVisibility(View.VISIBLE);
				handler.sendEmptyMessageDelayed(BUFFER_PROGRESS, 1000);
				if (mediaController != null) {
					mediaController.updatePausePlay();
				}
				break;
			case BUFFER_PROGRESS:
				if (videoService.getBufferProgress() >= 100) {
					setVideoLoadingLayoutVisibility(View.GONE);
				} else {
					loadingMessage.setText("已加载 "
							+ (int) (videoService.getBufferProgress()) + "%");
					handler.sendEmptyMessageDelayed(BUFFER_PROGRESS, 1000);
					stopPlayer();
				}
				break;
			case BUFFER_COMPLETE:
				setVideoLoadingLayoutVisibility(View.GONE);
				handler.removeMessages(BUFFER_PROGRESS);
				if (mediaController != null) {
					mediaController.updatePausePlay();
				}
				break;
			case HW_FAILED:
				if (videoService != null) {
					Ishwcoder = false;
					videoView.initialize(VideoActivity.this,
							VideoActivity.this, Ishwcoder);
				}
				break;
			case SEEK_START:
				if (network) {
					setVideoLoadingLayoutMessage(getResources().getString(
							R.string.player_loading));
					setVideoLoadingLayoutVisibility(View.VISIBLE);
				}else {
					startPlayer();
				}
				break;
			case SEEK_COMPLETE:
				setVideoLoadingLayoutVisibility(View.GONE);
				startPlayer();
				break;
			}
		};
	};

	private void Exit(String message) {
		if (message != null) {
			ToastFactory.getLongToast(VideoActivity.this, message).show();
		}
		VideoActivity.this.finish();
	}

	private boolean isInitialized() {
		return (created && videoService != null && videoService.isInitialized());
	}

	private void loadPlayerConfig() {
		if (!isInitialized())
			return;
		videoService.setBuffer(PlayerConfig.DEFAULT_BUF_SIZE);
		videoService.setVideoQuality(MediaPlayer.VIDEOQUALITY_MEDIUM);
		videoService.setDeinterlace(PlayerConfig.DEFAULT_DEINTERLACE);
		videoService.setVolume(PlayerConfig.DEFAULT_STEREO_VOLUME,
				PlayerConfig.DEFAULT_STEREO_VOLUME);
		if (videoView != null && isInitialized())
			setVideoLayout();
	}

	private void setVideoLayout() {
		videoView.setVideoLayout(videoService.getVideoWidth(),
				videoService.getVideoHeight(),
				videoService.getVideoAspectRatio());
	}

	@Override
	public void onStart() {
		super.onStart();
		if (!serviceConnected) {
			Intent serviceIntent = new Intent(this, VideoService.class);
			serviceIntent.putExtra("isHWCoder", Ishwcoder);
			bindService(serviceIntent, serviceConnection,
					Context.BIND_AUTO_CREATE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mediaController != null) {
			mediaController.show();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (!created)
			return;
		if (isInitialized()) {
			if (videoService != null && videoService.isPlaying()) {
				stopPlayer();
				startpos = videoService.getCurrentPosition();
			}
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (!created)
			return;
		if (isInitialized()) {
			if (videoService != null && videoService.isPlaying()) {
				stopPlayer();
				startpos = videoService.getCurrentPosition();
			}
		}
	}

	@Override
	public void onDestroy() {
		if (serviceConnected) {
			unbindService(serviceConnection);
		}
		super.onDestroy();
		if (isInitialized() && !videoService.isPlaying())
			release();
		if (mediaController != null)
			mediaController.release();
	}

	private void release() {
		if (videoService != null) {
			videoService.release();
			videoService.releaseContext();
		}
	}

	// playerservice 监听
	@Override
	public void onHWRenderFailed() {
		handler.sendEmptyMessage(HW_FAILED);
	}

	@Override
	public void onVideoSizeChanged(int width, int height) {
		if (videoService != null) {
			setVideoLayout();
		}
	}

	@Override
	public void onOpenStart() {
		handler.sendEmptyMessage(OPEN_START);
	}

	@Override
	public void onOpenSuccess() {
		handler.sendEmptyMessage(OPEN_SUCCESS);
	}

	@Override
	public void onOpenFailed() {
		handler.sendEmptyMessage(OPEN_FAILED);
	}

	@Override
	public void onBufferStart() {
		handler.sendEmptyMessage(BUFFER_START);
		stopPlayer();
	}

	@Override
	public void onBufferComplete() {
		handler.sendEmptyMessage(BUFFER_COMPLETE);
		if (videoService != null && (!videoService.isPlaying()))
			startPlayer();
	}

	private void stopPlayer() {
		if (isInitialized()) {
			videoService.stop();
		}
	}

	private void startPlayer() {
		if (isInitialized()) {
			if (network) {
				if (NetworkUtil.isAvailable(this)) {
					videoService.start();
				} else {
					ToastFactory.getLongToast(this,
							getString(R.string.player_network_eror)).show();
				}
			} else {
				videoService.start();
			}
		}
	}

	@Override
	public void onDownloadRateChanged(int kbPerSec) {
		// 下载速度监听
		if (isInitialized() && videoService.isPlaying()
				&& mediaController != null) {
			mediaController
					.setDownLoadSpeed(String.format("%d KB/s", kbPerSec));
		}
	}

	@Override
	public void onPlaybackComplete() {
		Exit(getString(R.string.player_complete));
	}

	@Override
	public void onNetWorkError() {
		if (network) {
			ToastFactory.getLongToast(this,
					getString(R.string.player_network_eror)).show();
			if (isInitialized() && videoService.isPlaying()) {
				stopPlayer();
			}
			if (mediaController != null) {
				mediaController.show();
			}
		}
	}

	@Override
	public void onSeekComplete() {
		handler.sendEmptyMessage(SEEK_COMPLETE);
	}

	// 设置是否显示进度条
	private void setVideoLoadingLayoutVisibility(int visibility) {
		loadingLayout.setVisibility(visibility);
	}

	// 设置进度提示
	private void setVideoLoadingLayoutMessage(String message) {
		loadingMessage.setText(message);
	}

	private void attachMediaController() {
		mediaController = new MediaController(this);
		mediaController.setMediaPlayer(mediaControllerListener);
		mediaController.setAnchorView(videoView.getRootView());
		mediaController.setVideoName(video_name);
		mediaController.setVideoTime(video_time);
		mediaController.show();
	}

	private MediaControllerListener mediaControllerListener = new MediaControllerListener() {

		@Override
		public void stop() {
			if (isInitialized()) {
				stopPlayer();
			}
		}

		@Override
		public void start() {
			if (isInitialized()) {
				startPlayer();
			}
		}

		@Override
		public void seekTo(long pos) {
			if (isInitialized())
				handler.sendEmptyMessage(SEEK_START);
			videoService.seekTo(pos);
		}

		@Override
		public boolean isPlaying() {
			if (isInitialized()) {
				return videoService.isPlaying();
			}
			return false;
		}

		@Override
		public long getDuration() {
			if (isInitialized()) {
				return videoService.getDuration();
			}
			return 0;
		}

		@Override
		public long getCurrentPosition() {
			if (isInitialized()) {
				return videoService.getCurrentPosition();
			}
			return 0;
		}

		@Override
		public void back() {
			Exit(null);
		}
	};
	
}
