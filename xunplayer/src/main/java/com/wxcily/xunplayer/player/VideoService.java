package com.wxcily.xunplayer.player;

import com.wxcily.xunplayer.player.utils.NetworkUtil;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnErrorListener;
import io.vov.vitamio.MediaPlayer.OnHWRenderFailedListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer.OnSeekCompleteListener;
import io.vov.vitamio.MediaPlayer.OnVideoSizeChangedListener;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.view.SurfaceHolder;

public class VideoService extends Service implements OnCompletionListener,
		OnPreparedListener, OnVideoSizeChangedListener, OnErrorListener,
		OnInfoListener,OnSeekCompleteListener{
	// 播放器状态

	private long seekTo = 0;// 播放进度
	private boolean isInit;// 判断是否初始化
	private String[] data;// 播放地址数组
	private SurfaceHolder surfaceHolder;
	private MediaPlayer mediaPlayer;
	private PlayerListener playerlistener;

	private final IBinder mBinder = new LocalBinder();

	public class LocalBinder extends Binder {
		public VideoService getService() {
			return VideoService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		isInit = false;
	}

	// 初始化播放器
	private void playerInit(boolean isHWCoder) {
		mediaPlayer = new MediaPlayer(
				VideoService.this.getApplicationContext(), isHWCoder);
		// 硬件渲染失败监听
		mediaPlayer.setOnHWRenderFailedListener(new OnHWRenderFailedListener() {
			@Override
			public void onFailed() {
				if (playerlistener != null)
					playerlistener.onHWRenderFailed();
			}
		});
		mediaPlayer.setOnCompletionListener(this);// 完成的监听
		mediaPlayer.setOnPreparedListener(this);// 准备的监听
		mediaPlayer.setOnVideoSizeChangedListener(this);// 视频尺寸变化监听
		mediaPlayer.setOnErrorListener(this);// 错误监听
		mediaPlayer.setOnInfoListener(this);// 信息监听
		mediaPlayer.setOnSeekCompleteListener(this);
	}

	// 初始化
	public void initialize(PlayerListener listener, String[] datas,
			long startPos, boolean isHWCodec) {
		if (mediaPlayer == null) {
			playerInit(isHWCodec);
		}
		playerlistener = listener;
		data = datas;
		seekTo = startPos;
		playerlistener.onOpenStart();
		openVideo();
	}

	// 打开视频的方法
	private void openVideo() {
		if (data == null || mediaPlayer == null || data.length == 0)
			return;
		// 重置播放器
		isInit = false;
		mediaPlayer.reset();
		mediaPlayer.setScreenOnWhilePlaying(true);
		mediaPlayer.setDataSegments(data,
				Environment.getExternalStorageDirectory() + "/"
						+ PlayerConfig.VIDEO_CACHE_DIR);
		if (surfaceHolder != null && surfaceHolder.getSurface() != null
				&& surfaceHolder.getSurface().isValid()) {
			mediaPlayer.setDisplay(surfaceHolder);
		}
		mediaPlayer.prepareAsync();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		release();
		releaseContext();
	}

	public void release() {
		if (mediaPlayer != null) {
			mediaPlayer.reset();
			playerlistener = null;
			isInit = false;
		}
	}

	public void releaseContext() {
		if (mediaPlayer != null)
			mediaPlayer.release();
		mediaPlayer = null;
	}

	// 停止播放
	public void stop() {
		if (isInit) {
			if (isPlaying()) {
				mediaPlayer.pause();
			}
		}
	}

	// 开始播放
	public void start() {
		if (isInit) {
			if (!isPlaying()) {
				mediaPlayer.start();
			}
		}
	}

	// 定义接口
	public static interface PlayerListener {
		public void onHWRenderFailed();

		public void onVideoSizeChanged(int width, int height);

		public void onOpenStart();

		public void onOpenSuccess();

		public void onOpenFailed();

		public void onBufferStart();

		public void onBufferComplete();

		public void onDownloadRateChanged(int kbPerSec);

		public void onPlaybackComplete();

		public void onNetWorkError();
		
		public void onSeekComplete();
	}

	// 警告信息监听
	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		if (NetworkUtil.isAvailable(getApplicationContext())) {
			switch (what) {
			case MediaPlayer.MEDIA_INFO_BUFFERING_START:
				playerlistener.onBufferStart();
				break;
			case MediaPlayer.MEDIA_INFO_BUFFERING_END:
				playerlistener.onBufferComplete();
				break;
			case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
				playerlistener.onDownloadRateChanged(extra);
				break;
			}
		} else {
			playerlistener.onNetWorkError();
		}
		return true;
	}

	// 播放错误
	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		playerlistener.onOpenFailed();
		return false;
	}

	// 视频尺寸变化
	@Override
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		if (playerlistener != null)
			playerlistener.onVideoSizeChanged(width, height);
	}

	// 准备完成
	@Override
	public void onPrepared(MediaPlayer mp) {
		openSuccess();
	}

	// 打开成功做的处理
	private void openSuccess() {
		isInit = true;
		mediaPlayer.seekTo(seekTo);
		playerlistener.onOpenSuccess();
	}

	// 播放位置设置
	public void seekTo(long percent) {
		if (isInit)
			mediaPlayer.seekTo(percent);
	}

	// 获取视频时间
	public long getDuration() {
		if (isInit)
			return mediaPlayer.getDuration();
		return 0;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		if (playerlistener != null) {
			playerlistener.onPlaybackComplete();
		}
	}

	// 设置视频质量
	public void setVideoQuality(int quality) {
		if (isInit)
			mediaPlayer.setVideoQuality(quality);
	}

	// 设置逐行扫描
	public void setDeinterlace(boolean deinterlace) {
		if (isInit)
			mediaPlayer.setDeinterlace(deinterlace);
	}

	// 设置音量
	public void setVolume(float left, float right) {
		if (isInit)
			mediaPlayer.setVolume(left, right);
	}

	// 判断是否正在缓冲
	protected boolean isBuffering() {
		return (isInit && mediaPlayer.isBuffering());
	}

	// 设置缓冲区大小
	public void setBuffer(int bufSize) {
		if (isInit)
			mediaPlayer.setBufferSize(bufSize);
	}

	// 获取缓冲
	public float getBufferProgress() {
		if (isInit)
			return mediaPlayer.getBufferProgress();
		return 0f;
	}

	// 获取当前播放位置
	public long getCurrentPosition() {
		if (isInit)
			return mediaPlayer.getCurrentPosition();
		return 0;
	}

	// 获取视频宽度
	public int getVideoWidth() {
		if (isInit)
			return mediaPlayer.getVideoWidth();
		return 0;
	}

	// 获取视频高度
	public int getVideoHeight() {
		if (isInit)
			return mediaPlayer.getVideoHeight();
		return 0;
	}

	// 获取视频横纵比例
	public float getVideoAspectRatio() {
		if (isInit)
			return mediaPlayer.getVideoAspectRatio();
		return 0f;
	}

	// 是否正在播放
	public boolean isPlaying() {
		return (isInit && mediaPlayer.isPlaying());
	}

	// 设置屏幕
	public void setDisplay(SurfaceHolder surface) {
		surfaceHolder = surface;
		if (mediaPlayer != null)
			mediaPlayer.setDisplay(surfaceHolder);
	}

	// 释放屏幕
	public void releaseSurface() {
		if (isInit)
			mediaPlayer.releaseDisplay();
	}

	// 设置回调接口
	public void setPlayerListener(PlayerListener listener) {
		playerlistener = listener;
	}

	// 是否初始化成功
	public boolean isInitialized() {
		return isInit;
	}

	@Override
	public void onSeekComplete(MediaPlayer mp) {
		// TODO Auto-generated method stub
		
	}

}
