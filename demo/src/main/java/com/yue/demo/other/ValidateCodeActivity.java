package com.yue.demo.other;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.yue.demo.R;
import com.yue.demo.util.ValidateCode;

public class ValidateCodeActivity extends Activity implements OnClickListener {
	private ImageView iv_showCode;
	private EditText et_phoneCode;
	private EditText et_phoneNum;
	//产生的验证码
	private String realCode;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.validatecode);
		
		
		et_phoneCode = (EditText) findViewById(R.id.et_phoneCodes);
		Button but_toSetCode = (Button) findViewById(R.id.but_forgetpass_toSetCodes);
		but_toSetCode.setOnClickListener(this);
		iv_showCode = (ImageView) findViewById(R.id.iv_showCode);
		//将验证码用图片的形式显示出来
		iv_showCode.setImageBitmap(ValidateCode.getInstance().createBitmap());
		
		iv_showCode.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.iv_showCode:
			iv_showCode.setImageBitmap(ValidateCode.getInstance().createBitmap());
			realCode = ValidateCode.getInstance().getCode();
			break;

		case R.id.but_forgetpass_toSetCodes:
			
			String phoneCode = et_phoneCode.getText().toString();
			if(phoneCode.equals(realCode)){
				Toast.makeText(ValidateCodeActivity.this, phoneCode+"验证码正确", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(ValidateCodeActivity.this, phoneCode+"验证码错误", Toast.LENGTH_SHORT).show();
			}
			
			break;
		}
		
		
	}


}
