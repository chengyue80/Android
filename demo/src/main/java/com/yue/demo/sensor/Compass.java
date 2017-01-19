package com.yue.demo.sensor;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.yue.demo.R;
import com.yue.demo.MainActivity;

/**
 * 指南针
 * 
 * @author chengyue
 * 
 */
public class Compass extends Activity implements SensorEventListener {

	private String TAG = Compass.class.getSimpleName();

	private ImageView znzImage;
	private SensorManager sensorManager;
	// 记录指南针图片转过的角度
	float currentDegree = 0f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 为系统的方向传感器注册监听器
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	protected void onStop() {
		sensorManager.unregisterListener(this);
		super.onStop();
	}

	@Override
	protected void onPause() {
		sensorManager.unregisterListener(this);
		super.onPause();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		// 获取手机顶部朝向与正北方向的夹角
		float degree = event.values[0];
		// 穿件旋转动画（反向转过degree角度）
		RotateAnimation animation = new RotateAnimation(currentDegree, -degree,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animation.setDuration(200);
		znzImage.startAnimation(animation);
		currentDegree = -degree;
	}

	/** 当传感器精度改变时回调该方法 */
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	private void initView() {
		LinearLayout ll = new LinearLayout(this);
		ll.setLayoutParams(MainActivity.layoutParams_mm);
		ll.setOrientation(LinearLayout.VERTICAL);

		znzImage = new ImageView(this);
		znzImage.setLayoutParams(MainActivity.layoutParams_mw);
		znzImage.setScaleType(ScaleType.FIT_CENTER);
		znzImage.setImageResource(R.drawable.znz);
		ll.addView(znzImage);

		setContentView(ll);
	}

}
