package com.yue.demo.other;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.iflytek.android.framework.annotation.ViewInject;
import com.iflytek.android.framework.base.BaseActivity;
import com.yue.demo.R;

public class EmailActivity extends BaseActivity {
	@ViewInject(id = R.id.button1, listenerName = "onClick", methodName = "OnClick")
	private Button sendBtn;
	@ViewInject(id = R.id.button2, listenerName = "onClick", methodName = "OnClick")
	private Button sendToManyBtn;
	@ViewInject(id = R.id.button3, listenerName = "onClick", methodName = "OnClick")
	private Button sendAttachmentBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sendBtn.setText("发邮件");
		sendToManyBtn.setText("多方发送");
		sendAttachmentBtn.setText("包含附件");

	}

	public void OnClick(View view) {
		switch (view.getId()) {
		case R.id.button1:
			Intent mIntent = new Intent(Intent.ACTION_SEND);
			mIntent.setType("plain/text");
			mIntent.putExtra(Intent.EXTRA_EMAIL,
					new String[] { "chengyue@iflytek.com" });
			startActivity(Intent.createChooser(mIntent, "邮件"));

			break;
		case R.id.button2:
			Intent intent = new Intent(Intent.ACTION_SENDTO);
			intent.setData(Uri.parse("mailto:2522599543@qq.com"));
			intent.putExtra(Intent.EXTRA_EMAIL, new String[] {
					"2522599543@qq.com", "chengyue@iflytek.com" });
			// ����
			intent.putExtra(Intent.EXTRA_CC,
					new String[] { "2522599543@qq.com" });
			// ����
			intent.putExtra(Intent.EXTRA_BCC,
					new String[] { "2522599543@qq.com" });
			intent.putExtra(Intent.EXTRA_SUBJECT, "EXTRA_SUBJECT");
			intent.putExtra(Intent.EXTRA_TEXT, "EXTRA_TEXT");
			startActivity(intent);
			break;
		case R.id.button3:
			Intent intent3 = new Intent(Intent.ACTION_SEND);
			intent3.putExtra(Intent.EXTRA_EMAIL,
					new String[] { "2522599543@qq.com" });
			intent3.putExtra(Intent.EXTRA_SUBJECT, "EXTRA_SUBJECT");
			intent3.putExtra(Intent.EXTRA_TEXT, "EXTRA_TEXT");
			intent3.putExtra(Intent.EXTRA_STREAM, Uri.parse(""));
			intent3.setType("text/plain");
			startActivity(intent3);
			break;

		default:
			break;
		}
	}

}
