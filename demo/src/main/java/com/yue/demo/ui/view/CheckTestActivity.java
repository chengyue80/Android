package com.yue.demo.ui.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.yue.demo.R;

public class CheckTestActivity extends Activity implements
        CompoundButton.OnCheckedChangeListener {

    // private String sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller_checktest);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.controller_radiogroup);

        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                /*
                 * switch(checkedId) { case R.id.radioMan: sex="��"; break; case
                 * R.id.radiowoMan: sex="Ů"; break; case R.id.radioelse:
                 * sex="������"; break;
                 * 
                 * }
                 */
                RadioButton radioButton = (RadioButton) findViewById(checkedId);
                Toast.makeText(CheckTestActivity.this, radioButton.getText(),
                        Toast.LENGTH_SHORT).show();

            }
        });

        CheckBox chkjava = (CheckBox) findViewById(R.id.controller_chkJava);
        CheckBox chkandroid = (CheckBox) findViewById(R.id.controller_chkAndroid);
        CheckBox chknet = (CheckBox) findViewById(R.id.controller_chknet);
        chkjava.setOnCheckedChangeListener(this);
        chkandroid.setOnCheckedChangeListener(this);
        chknet.setOnCheckedChangeListener(this);

        ToggleButton tbutton = (ToggleButton) findViewById(R.id.controller_tButton);
        tbutton.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        Toast.makeText(CheckTestActivity.this,
                buttonView.getText().toString() + isChecked, Toast.LENGTH_SHORT)
                .show();
    }

}
