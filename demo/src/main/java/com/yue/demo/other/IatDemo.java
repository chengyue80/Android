package com.yue.demo.other;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.iflytek.android.framework.base.BaseActivity;
import com.iflytek.android.framework.toast.BaseToast;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.LexiconListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUnderstander;
import com.iflytek.cloud.SpeechUnderstanderListener;
import com.iflytek.cloud.UnderstanderResult;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.cloud.util.ContactManager.ContactListener;
import com.iflytek.speech.util.JsonParser;
import com.iflytek.sunflower.FlowerCollector;
import com.yue.demo.R;
import com.yue.demo.RootActivity;
import com.yue.demo.util.LogUtil;

/**
 * 语音听写：注意根据云平台的应用下载相应的msc文件并修改
 * 
 * @author chengyue
 * 
 */
public class IatDemo extends RootActivity implements OnClickListener {
	private static String TAG = IatDemo.class.getSimpleName();
	// 语音听写对象
	private SpeechRecognizer mIat;
	// 语音听写UI
	private RecognizerDialog mIatDialog;
	// 用HashMap存储听写结果
	private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

	private Toast mToast;
	private SharedPreferences mSharedPreferences;
	// 引擎类型
	private String mEngineType = SpeechConstant.TYPE_CLOUD;

	private final String PREFER_NAME = "speechdemo";
	private SpeechUnderstander mSpeechUnderstander;

	@SuppressLint("ShowToast")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		initLayout();
		// 初始化识别无UI识别对象
		// 使用SpeechRecognizer对象，可根据回调消息自定义界面；
		mIat = SpeechRecognizer.createRecognizer(this, mInitListener);

		// 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
		// 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
//		mIatDialog = new RecognizerDialog(IatDemo.this, mInitListener);

		mSharedPreferences = getSharedPreferences(PREFER_NAME,
				Activity.MODE_PRIVATE);
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

		// 语音理解
		/**
		 * 申请的appid时，我们为开发者开通了开放语义（语义理解）
		 * 由于语义理解的场景繁多，需开发自己去开放语义平台：http://www.xfyun.cn/services/osp
		 * 配置相应的语音场景，才能使用语义理解，否则文本理解将不能使用，语义理解将返回听写结果。
		 */
		// 初始化对象
		mSpeechUnderstander = SpeechUnderstander.createUnderstander(this,
				mInitListener);
	}

	/**
	 * 初始化Layout。
	 */
	private void initLayout() {
		Button button1 = (Button) findViewById(R.id.button1);
		Button button2 = (Button) findViewById(R.id.button2);
		button1.setOnClickListener(IatDemo.this);
		button2.setOnClickListener(IatDemo.this);
		button1.setText("语音听写");
		button2.setText("语义理解");
	}

	int ret = 0; // 函数调用返回值

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		// 开始听写
		// 如何判断一次听写结束：OnResult isLast=true 或者 onError
		case R.id.button1:
			mIatResults.clear();
			// 设置参数
			setParam();
			setParamUnderstander();
			
			if (mSpeechUnderstander.isUnderstanding()) {
				mSpeechUnderstander.stopUnderstanding();
				BaseToast.DefaultToast(getApplicationContext(), "停止录音", 2000);
			} else {
				int resultCode = mSpeechUnderstander
						.startUnderstanding(speechUnderstanderListener);
				if (resultCode != ErrorCode.SUCCESS) {
					showTip("语义理解,错误码："
							+ new SpeechError(ret).getErrorDescription());
				} else {
					showTip(getString(R.string.text_begin));
				}
			}
			boolean isShowDialog = mSharedPreferences.getBoolean(
					getString(R.string.pref_key_iat_show), false);
			if (isShowDialog) {
				// 显示听写对话框
				mIatDialog.setListener(recognizerDialogListener);
				mIatDialog.show();
				showTip(getString(R.string.text_begin));
			} else {
				// 不显示听写对话框
				if(mIat.isListening()){
					
				}
				ret = mIat.startListening(recognizerListener);
				if (ret != ErrorCode.SUCCESS) {
					showTip("听写失败,错误码："
							+ new SpeechError(ret).getErrorDescription());
				} else {
					showTip(getString(R.string.text_begin));
				}
			}
			

			
			

			break;
		case R.id.button2:
			setParamUnderstander();
			if (mSpeechUnderstander.isUnderstanding()) {
				mSpeechUnderstander.stopUnderstanding();
				BaseToast.DefaultToast(getApplicationContext(), "停止录音", 2000);
			} else {
				int resultCode = mSpeechUnderstander
						.startUnderstanding(speechUnderstanderListener);
				if (resultCode != ErrorCode.SUCCESS) {
					showTip("语义理解,错误码："
							+ new SpeechError(ret).getErrorDescription());
				} else {
					showTip(getString(R.string.text_begin));
				}
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 初始化监听器。
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d(TAG, "SpeechRecognizer init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				showTip("初始化失败，错误码："
						+ new SpeechError(code).getErrorDescription());
			}
		}
	};

	/**
	 * 上传联系人/词表监听器。
	 */
	private LexiconListener lexiconListener = new LexiconListener() {

		@Override
		public void onLexiconUpdated(String lexiconId, SpeechError error) {
			if (error != null) {
				showTip(error.toString());
			} else {
				showTip(getString(R.string.text_upload_success));
			}
		}
	};

	/**
	 * 听写监听器。
	 */
	private RecognizerListener recognizerListener = new RecognizerListener() {

		@Override
		public void onBeginOfSpeech() {
			showTip("开始说话");
		}

		@Override
		public void onError(SpeechError error) {
			// Tips：
			// 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
			// 如果使用本地功能（语音+）需要提示用户开启语音+的录音权限。
			showTip(error.getPlainDescription(true));
		}

		@Override
		public void onEndOfSpeech() {
			showTip("结束说话");
		}

		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			Log.d(TAG, "recognizerListener onResult : " + results.getResultString());
			printResult(results);

			if (isLast) {
				// TODO 最后的结果
			}
		}

		@Override
		public void onVolumeChanged(int volume, byte[] data) {
			showTip("当前正在说话，音量大小：" + volume);
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
		}
	};

	/**
	 * printResult:语音识别结果 <br/>
	 *
	 * @author chengyue
	 * @param results
	 * @since JDK 1.6
	 */
	private void printResult(RecognizerResult results) {
		String text = JsonParser.parseIatResult(results.getResultString());

		String sn = null;
		// 读取json结果中的sn字段
		try {
			JSONObject resultJson = new JSONObject(results.getResultString());
			sn = resultJson.optString("sn");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		mIatResults.put(sn, text);

		StringBuffer resultBuffer = new StringBuffer();
		for (String key : mIatResults.keySet()) {
			resultBuffer.append(mIatResults.get(key));
		}
		showTip(resultBuffer.toString());

	}

	/**
	 * 听写UI监听器
	 */
	private RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener() {
		public void onResult(RecognizerResult results, boolean isLast) {
			LogUtil.d(TAG, "recognizerDialogListener onResult : " + results.toString());
			printResult(results);
		}

		/**
		 * 识别回调错误.
		 */
		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}

	};

	/**
	 * 获取联系人监听器。
	 */
	private ContactListener mContactListener = new ContactListener() {

		@Override
		public void onContactQueryFinish(String contactInfos, boolean changeFlag) {
			// 注：实际应用中除第一次上传之外，之后应该通过changeFlag判断是否需要上传，否则会造成不必要的流量.
			// if(changeFlag) {
			// 指定引擎类型
			mIat.setParameter(SpeechConstant.ENGINE_TYPE,
					SpeechConstant.TYPE_CLOUD);
			mIat.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
			ret = mIat.updateLexicon("contact", contactInfos, lexiconListener);
			if (ret != ErrorCode.SUCCESS) {
				showTip("上传联系人失败：" + ret);
			}
		}
	};

	/** 语音理解监听器 */
	private SpeechUnderstanderListener speechUnderstanderListener = new SpeechUnderstanderListener() {

		@Override
		public void onVolumeChanged(int arg0, byte[] arg1) {

		}

		@Override
		public void onResult(UnderstanderResult result) {
			LogUtil.e(TAG, "speechUnderstanderListener onResult");
			if(null != result){
				String text = result.getResultString();
				LogUtil.e(TAG, "speechUnderstanderListener onResult " + text);
				LogUtil.e(TAG, "speechUnderstanderListener onResult " + result.describeContents());
				
			}

		}

		@Override
		public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {

		}

		@Override
		public void onError(SpeechError arg0) {

		}

		@Override
		public void onEndOfSpeech() {

		}

		@Override
		public void onBeginOfSpeech() {

		}
	};

	private void showTip(final String str) {
		mToast.setText(str);
		mToast.show();
	}

	/**
	 * 参数设置：语音听写
	 * 
	 * @param param
	 * @return
	 */
	public void setParam() {
		// 清空参数
		mIat.setParameter(SpeechConstant.PARAMS, null);

		// 设置听写引擎
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
		// 设置返回结果格式
		mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

		String lag = mSharedPreferences.getString("iat_language_preference",
				"mandarin");
		if (lag.equals("en_us")) {
			// 设置语言
			mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
		} else {
			// 设置语言
			mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
			// 设置语言区域
			mIat.setParameter(SpeechConstant.ACCENT, lag);
		}

		// 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
		mIat.setParameter(SpeechConstant.VAD_BOS,
				mSharedPreferences.getString("iat_vadbos_preference", "4000"));

		// 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
		mIat.setParameter(SpeechConstant.VAD_EOS,
				mSharedPreferences.getString("iat_vadeos_preference", "1000"));

		// 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
		mIat.setParameter(SpeechConstant.ASR_PTT,
				mSharedPreferences.getString("iat_punc_preference", "1"));

		// 设置音频保存路径，保存音频格式仅为pcm，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH,
				Environment.getExternalStorageDirectory()
						+ "/iflytek/wavaudio.pcm");

		// 设置听写结果是否结果动态修正，为“1”则在听写过程中动态递增地返回结果，否则只在听写结束之后返回最终结果
		// 注：该参数暂时只对在线听写有效
		mIat.setParameter(SpeechConstant.ASR_DWA,
				mSharedPreferences.getString("iat_dwa_preference", "0"));
	}

	/**
	 * 参数设置:语义理解
	 * 
	 * @param param
	 * @return
	 */
	public void setParamUnderstander() {
		
		mSpeechUnderstander.setParameter(SpeechConstant.DOMAIN, "iat");
		mSpeechUnderstander.setParameter(SpeechConstant.NLP_VERSION, "2.0");
		mSpeechUnderstander.setParameter(SpeechConstant.RESULT_TYPE, "json");

		String lang = mSharedPreferences.getString(
				"understander_language_preference", "mandarin");
		if (lang.equals("en_us")) {
			// 设置语言
			mSpeechUnderstander.setParameter(SpeechConstant.LANGUAGE, "en_us");
		} else {
			// 设置语言
			mSpeechUnderstander.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
			// 设置语言区域
			mSpeechUnderstander.setParameter(SpeechConstant.ACCENT, lang);
		}
		// 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
		mSpeechUnderstander.setParameter(SpeechConstant.VAD_BOS,
				mSharedPreferences.getString("understander_vadbos_preference",
						"4000"));

		// 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
		mSpeechUnderstander.setParameter(SpeechConstant.VAD_EOS,
				mSharedPreferences.getString("understander_vadeos_preference",
						"1000"));

		// 设置标点符号，默认：1（有标点）
		mSpeechUnderstander.setParameter(SpeechConstant.ASR_PTT,
				mSharedPreferences.getString("understander_punc_preference",
						"1"));

		// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		// 注：AUDIO_FORMAT参数语记需要更新版本才能生效
		mSpeechUnderstander.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
		mSpeechUnderstander.setParameter(SpeechConstant.ASR_AUDIO_PATH,
				Environment.getExternalStorageDirectory() + "/msc/sud.wav");
	}
	
//	private  parseUnderstanrderResult
	
	private class SpeechResult {
//		{
//			  "semantic": {
//			    "slots": {
//			      "channel": "taobao", 
//			      "keywords": "儿童床"
//			    }
//			  }, 
//			  "rc": 0, 
//			  "operation": "QUERY", 
//			  "service": "websearch", 
//			  "text": "淘宝搜索儿童床"
//			}
		
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 退出时释放连接
		mIat.cancel();
		mIat.destroy();
	}

	@Override
	protected void onResume() {
		// 移动数据统计分析
		FlowerCollector.onResume(IatDemo.this);
		FlowerCollector.onPageStart(TAG);
		super.onResume();
	}

	@Override
	protected void onPause() {
		// 移动数据统计分析
		FlowerCollector.onPageEnd(TAG);
		FlowerCollector.onPause(IatDemo.this);
		super.onPause();
	}
}
