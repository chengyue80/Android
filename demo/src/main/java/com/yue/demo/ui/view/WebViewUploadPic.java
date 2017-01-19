package com.yue.demo.ui.view;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yue.demo.util.LogUtil;

/**
 * @desc: webView中选择本地图片
 * @author: chengyue
 * @createTime: 2015-10-28下午8:00:31
 * @version: v1.0
 */
@SuppressLint("SetJavaScriptEnabled")
public class WebViewUploadPic extends Activity {
	// <uses-permission android:name="android.permission.INTERNET" />
	// <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"
	// />
	// <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
	// />
	// android:targetSdkVersion="19" 不得大于19

	private final String TAG = WebViewUploadPic.class.getSimpleName();
	WebView webView;
	private ValueCallback<Uri> mUploadMessage;
	private ValueCallback<Uri[]> mUploadMessage_L;
	private final static int FILECHOOSER_RESULTCODE = 1;

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode == FILECHOOSER_RESULTCODE) {
			final int sdk = android.os.Build.VERSION.SDK_INT;
			if (sdk >= android.os.Build.VERSION_CODES.LOLLIPOP) {
				if (mUploadMessage_L == null)
					return;
				Uri result = intent == null || resultCode != RESULT_OK ? null
						: intent.getData();
				mUploadMessage_L.onReceiveValue(new Uri[] { result });
				mUploadMessage_L = null;

			} else {

				if (null == mUploadMessage)
					return;
				Uri result = intent == null || resultCode != RESULT_OK ? null
						: intent.getData();
				mUploadMessage.onReceiveValue(result);
				mUploadMessage = null;
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		webView = new WebView(this);
		// 移除不安全接口
		webView.setSaveEnabled(false);
		webView.removeJavascriptInterface("searchBoxJavaBridge_");
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setGeolocationEnabled(true);// 路线定位
		settings.setDomStorageEnabled(true);// 当前位置定位，附近搜索
		// settings.setDatabaseEnabled(true);

		// webView.loadUrl("http://58.243.16.122:9900/bus/index.html");
		webView.loadUrl("http://60.174.83.217:8201/SWZL/?userId=xx&userName=%E5%B0%8F%E6%BA%AA&userPhone=13075562622&userSex=");
		webView.setWebViewClient(new myWebClient());
		webView.setWebChromeClient(new MyWebChromeClient());

		setContentView(webView);

	}

	private class MyWebChromeClient extends WebChromeClient {

		@Override
		public boolean onShowFileChooser(WebView webView,
				ValueCallback<Uri[]> filePathCallback,
				FileChooserParams fileChooserParams) {
			final int sdk = android.os.Build.VERSION.SDK_INT;
			if (sdk >= android.os.Build.VERSION_CODES.LOLLIPOP) {
				mUploadMessage_L = filePathCallback;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);

				String[]  accept = fileChooserParams.getAcceptTypes(); 
				StringBuffer buffer = new StringBuffer();
				for (int j = 0; j < accept.length; j++) {
					if(j == accept.length -1 ){
						buffer.append(accept[j]);
					}else {
						buffer.append(accept[j] + ",");
					}
				}
				LogUtil.d(TAG, "accept : " + buffer.toString());
				i.setType(buffer.toString());
				startActivityForResult(Intent.createChooser(i, "File Chooser"),
						FILECHOOSER_RESULTCODE);
			}
			return true;
		}

		// The undocumented magic method override
		// Eclipse will swear at you if you try to put @Override here
		// For Android 3.0-
		public void openFileChooser(ValueCallback<Uri> uploadMsg) {
			LogUtil.d(TAG, "openFileChooser 3.0-");
			openFileChooser(uploadMsg, "*/*");

		}

		// For Android 3.0+
		public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
			LogUtil.d(TAG, "openFileChooser 3.0+");
			openFileChooser(uploadMsg, acceptType, null);
		}

		// For Android 4.1
		public void openFileChooser(ValueCallback<Uri> uploadMsg,
				String acceptType, String capture) {
			LogUtil.d(TAG, "openFileChooser 4.1");
			LogUtil.d(TAG, "acceptType : " + acceptType);
			mUploadMessage = uploadMsg;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			i.setType(acceptType);
			startActivityForResult(Intent.createChooser(i, "File Chooser"),
					FILECHOOSER_RESULTCODE);

		}

		// 地理位置分享
		@Override
		public void onGeolocationPermissionsShowPrompt(String origin,
				Callback callback) {
			// TODO Auto-generated method stub
			callback.invoke(origin, true, false);
			super.onGeolocationPermissionsShowPrompt(origin, callback);
		}

	}

	public class myWebClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub

			view.loadUrl(url);
			return true;

		}
	}
}
