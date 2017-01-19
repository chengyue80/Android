package com.yue.demo.ui.view;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import com.yue.demo.R;
import com.yue.demo.util.LogUtil;

public class EditTextTestActivity extends Activity {
	
	private EditText name,pwd;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller_edittext_test);
        name = (EditText) findViewById(R.id.edittext_name);
        pwd = (EditText) findViewById(R.id.edittext_pwd);
        
        LogUtil.d("EditTextTestActivity", "name inputType : " + name.getInputType());
        name.setInputType(InputType.TYPE_CLASS_TEXT);
        LogUtil.d("EditTextTestActivity", "pwd inputType : " + pwd.getInputType());

    }

}
