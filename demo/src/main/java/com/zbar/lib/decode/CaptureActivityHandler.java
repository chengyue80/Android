package com.zbar.lib.decode;

import android.os.Handler;
import android.os.Message;

import com.zbar.lib.CaptureActivity;
import com.zbar.lib.camera.CameraManager;
import com.zbar.lib.camera.SysCode;

/**
 *  扫描消息转发
 */
public final class CaptureActivityHandler extends Handler {

	DecodeThread decodeThread = null;
	CaptureActivity activity = null;
	private State state;

	private enum State {
		PREVIEW, SUCCESS, DONE
	}

	public CaptureActivityHandler(CaptureActivity activity) {
		this.activity = activity;
		decodeThread = new DecodeThread(activity);
		decodeThread.start();
		state = State.SUCCESS;
		CameraManager.get().startPreview();
		restartPreviewAndDecode();
	}

	@Override
	public void handleMessage(Message message) {

		switch (message.what) {
		case SysCode.MSG_WHAT.AUTO_FOCUS:
			if (state == State.PREVIEW) {
				CameraManager.get().requestAutoFocus(this, SysCode.MSG_WHAT.AUTO_FOCUS);
			}
			break;
		case SysCode.MSG_WHAT.RESTART_PREVIEW:
			restartPreviewAndDecode();
			break;
		case SysCode.MSG_WHAT.DECODE_SUCCEEDED:
			state = State.SUCCESS;
			activity.handleDecode((String) message.obj);// 解析成功，回调
			break;

		case SysCode.MSG_WHAT.DECODE_FAILED:
			state = State.PREVIEW;
			CameraManager.get().requestPreviewFrame(decodeThread.getHandler(),
					SysCode.MSG_WHAT.DECODE);
			break;
		}

	}

	public void quitSynchronously() {
		state = State.DONE;
		CameraManager.get().stopPreview();
		removeMessages(SysCode.MSG_WHAT.DECODE_SUCCEEDED);
		removeMessages(SysCode.MSG_WHAT.DECODE_FAILED);
		removeMessages(SysCode.MSG_WHAT.DECODE);
		removeMessages(SysCode.MSG_WHAT.AUTO_FOCUS);
	}

	private void restartPreviewAndDecode() {
		if (state == State.SUCCESS) {
			state = State.PREVIEW;
			CameraManager.get().requestPreviewFrame(decodeThread.getHandler(),
					SysCode.MSG_WHAT.DECODE);
			CameraManager.get().requestAutoFocus(this, SysCode.MSG_WHAT.AUTO_FOCUS);
		}
	}

}
