package com.yue.demo.ui.view;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.TextView;

import com.iflytek.android.framework.annotation.ViewInject;
import com.iflytek.android.framework.base.BaseActivity;
import com.yue.demo.R;

public class ClockTestActivity extends BaseActivity {

	@ViewInject(id = R.id.tv_CountDownTimer)
	private TextView countDownTimer;

	private CountDownTimer downTimer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.controller_clocktest);
		final Chronometer chr = (Chronometer) findViewById(R.id.controller_chrtest);
		chr.setBase(SystemClock.elapsedRealtime());

		chr.setOnChronometerTickListener(new OnChronometerTickListener() {

			@Override
			public void onChronometerTick(Chronometer chronometer) {
				if (SystemClock.elapsedRealtime() - chr.getBase() > 40 * 1000) {
					chr.stop();
				}

			}
		});
		chr.start();

		initDownTimer();

	}

	private void initDownTimer() {
		if (downTimer == null) {

			downTimer = new CountDownTimer(10 * 1000, 1000) {

				@Override
				public void onTick(long millisUntilFinished) {
					countDownTimer.setText(String.format(
							getString(R.string.countdowntimer),
							String.valueOf(millisUntilFinished / 1000)));
				}

				@Override
				public void onFinish() {
					countDownTimer.setText("点击重发");
				}
			};
		}
		
		downTimer.start();
	}

}
