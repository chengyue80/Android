package com.yue.demo.net;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.iflytek.android.framework.base.BaseActivity;
import com.yue.demo.MainActivity;
import com.yue.demo.R;
import com.yue.demo.util.LogUtil;
import com.zbar.lib.CaptureActivity;

public class JsCallAndroid extends BaseActivity {

	private WebView webView;

	public List<String> list;

	private String TAG = JsCallAndroid.class.getSimpleName();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
		// 此处为了简化编程，使用file协议加载本地assets目录下的HTML页面
		// 如果有需要，也可使用http协议加载远程网站的HTML页面。
		webView.loadUrl("file:///android_asset/test.html");
		// 获取WebView的设置对象
		WebSettings webSettings = webView.getSettings();
		// 开启JavaScript调用
		webSettings.setJavaScriptEnabled(true);
		// 将MyObject对象暴露给JavaScript脚本
		// 这样test.html页面中的JavaScript可以通过myObj来调用MyObject的方法
		webView.addJavascriptInterface(new MyObject(), "myObject");
		webView.setWebViewClient(new MyWebViewClient());
		webView.setWebChromeClient(new WebChromeClient(){});

	}

	private void initView() {

		LinearLayout layout = new LinearLayout(this);
		layout.setLayoutParams(MainActivity.layoutParams_mm);
		layout.setOrientation(LinearLayout.VERTICAL);

		webView = new WebView(this);
		webView.setLayoutParams(MainActivity.layoutParams_mm);
		layout.addView(webView);

		setContentView(layout);
	}
	
	

	private void initData() {
		list = new ArrayList<String>();
		for (int i = 0; i < 5; i++) {
			list.add("我是List中的第" + (i + 1) + "行");
		}
	}

	private final static int FILECHOOSER_RESULTCODE = 5173;

	public static final int SCAN_RESULT = 2;

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode == FILECHOOSER_RESULTCODE) {
			final Uri result = intent == null || resultCode != RESULT_OK ? null
					: intent.getData();
			Log.d(TAG , "result : " + result);
			
			webView.post(new Runnable() {

				@Override
				public void run() {
					String uri = "javascript:setPicture(\"" + result + "\")";
					webView.loadUrl(uri);
//					webView.loadUrl(String.format("javascript:GetList()"));

					
				}
			});
		}else if(requestCode == SCAN_RESULT){
			if(resultCode == RESULT_OK){
				LogUtil.d("===","data : " + Uri.encode(intent.toUri(0)));
				LogUtil.d("===","result : " + intent.getStringExtra("result"));
				final String result = intent.getStringExtra("result");
//				webView.post(new Runnable() {
//
//					@Override
//					public void run() {
//						webView.loadUrl("javascript:setScanResult(\"" + result + "\")");
//
//						
//					}
//				});
				webView.loadUrl("http://103.39.110.136:91/index.html");
			}
			
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {

			if (webView.canGoBack()) {
				webView.goBack();
			} else {
				finish();
			}
			return true;
		} else {
			return false;
		}
	}

	public class MyObject {

		// 该方法将会暴露给JavaScript脚本调用
		@JavascriptInterface
		public void showToast(String name) {
			Toast.makeText(JsCallAndroid.this, name + "，您好！", Toast.LENGTH_LONG)
					.show();
		}

		// 该方法将会暴露给JavaScript脚本调用
		@JavascriptInterface
		public void showList() {
			// 显示一个普通的列表对话框
			new AlertDialog.Builder(JsCallAndroid.this)
					.setTitle("图书列表")
					.setIcon(R.drawable.ic_launcher)
					.setItems(
							new String[] { "疯狂Java讲义", "疯狂Android讲义",
									"轻量级Java EE企业应用实战" }, null)
					.setPositiveButton("确定", null).create().show();
		}

		/**
		 * 该方法将在js脚本中，通过window.javatojs.....()进行调用
		 * 
		 * @return
		 */
		@JavascriptInterface
		public String getObject(int index) {
			return list.get(index);
		}

		@JavascriptInterface
		public int getSize() {
			return list.size();
		}

		@JavascriptInterface
		public void Callfunction() {
			webView.post(new Runnable() {

				@Override
				public void run() {
					webView.loadUrl(String.format("javascript:GetList()"));

				}
			});
		}

		@JavascriptInterface
		public void getPicture() {
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory("android.intent.category.OPENABLE");
			i.setType("image/*");
			startActivityForResult(Intent.createChooser(i, "File Chooser"),
					FILECHOOSER_RESULTCODE);

		}
		
		@JavascriptInterface
		public void scanCode(){
			startActivityForResult(new Intent(JsCallAndroid.this,
					CaptureActivity.class), SCAN_RESULT);
		}
		
	}
	
	
	/**
	 * @author admin 自定义web视图
	 */
	private class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// 使用当前的WebView加载页面
			System.out.println("url:" + url);
			if (url.startsWith("http")) {
				view.loadUrl(url);
			}
			return true;
		}
	}
	
	


}
