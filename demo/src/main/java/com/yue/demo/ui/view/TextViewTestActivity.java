package com.yue.demo.ui.view;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.TextView;

import com.iflytek.android.framework.base.BaseActivity;
import com.yue.demo.R;

public class TextViewTestActivity extends BaseActivity implements OnClickListener {

	private TextView textView1, textView2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.controller_textview_test);

		final TextView txt = (TextView) findViewById(R.id.controller_textView1);

		txt.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if (hasFocus) {
					txt.setText("focusable 触发！");

				} else {
					txt.setText("focusable  null");
				}
			}
		});
		txt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				txt.setFocusable(true);
				txt.setFocusableInTouchMode(true);
			}
		});

		textView1 = (TextView) findViewById(R.id.textView1);
		textView2 = (TextView) findViewById(R.id.textView2);
		textView1.setFocusable(true);
		textView1.setFocusableInTouchMode(true);
		textView1.setOnClickListener(this);
		textView2.setOnClickListener(this);

		findViewById(R.id.controller_txtmarquee).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						findViewById(R.id.controller_txtmarquee).setFocusable(
								true);
						findViewById(R.id.controller_txtmarquee)
								.setFocusableInTouchMode(true);
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textView1:
			textView1.setFocusable(true);
			textView1.setFocusableInTouchMode(true);
			textView2.setFocusable(false);
			break;
		case R.id.textView2:
			textView2.setFocusable(true);
			textView2.setFocusableInTouchMode(true);
			textView1.setFocusable(false);

			break;

		default:
			break;
		}

	}

}
