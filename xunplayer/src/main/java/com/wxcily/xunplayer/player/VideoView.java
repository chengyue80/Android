package com.wxcily.xunplayer.player;

import com.wxcily.xunplayer.player.utils.DeviceInfo;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;

public class VideoView extends SurfaceView{
	private Activity activity;
	private SurfaceCallback surfaceCallback;

	public VideoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(mCallback);
		getHolder().setFormat(PixelFormat.RGBA_8888); 
	}

	@SuppressWarnings("deprecation")
	public void initialize(Activity activity, SurfaceCallback surfaceCallback, boolean push) {
		this.activity = activity;
		this.surfaceCallback = surfaceCallback;
		if (push)
			getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		else
			getHolder().setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
	}

	private void setSurfaceLayout( int videoWidth, int videoHeight, float videoAspectRatio) {
		LayoutParams layoutParams = getLayoutParams();
		int windowWidth = DeviceInfo.getScreenWidth(activity);
		int windowHeight = DeviceInfo.getScreenHeight(activity);
		float windowRatio = windowWidth/(float) windowHeight;
		layoutParams.width = windowRatio < videoAspectRatio? windowWidth : (int) (videoAspectRatio * windowHeight);
		layoutParams.height =  windowRatio > videoAspectRatio ? windowHeight : (int) (windowWidth / videoAspectRatio);
		setLayoutParams(layoutParams);
		getHolder().setFixedSize(videoWidth, videoHeight);
	}

	public void setVideoLayout(int videoWidth, int videoHeight, float videoRatio) {
		setSurfaceLayout(videoWidth, videoHeight, videoRatio);
	}

	private SurfaceHolder.Callback mCallback = new SurfaceHolder.Callback() {
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			holder.setKeepScreenOn(true);
			if (surfaceCallback != null)
				surfaceCallback.onSurfaceChanged(holder, format, width, height);
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			if (surfaceCallback != null)
				surfaceCallback.onSurfaceCreated(holder);
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			if (surfaceCallback != null)
				surfaceCallback.onSurfaceDestroyed(holder);
		}
	};

	

	public interface SurfaceCallback {
		public void onSurfaceCreated(SurfaceHolder holder);

		public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height);

		public void onSurfaceDestroyed(SurfaceHolder holder);
	}
}
