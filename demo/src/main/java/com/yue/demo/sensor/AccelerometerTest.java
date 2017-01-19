package com.yue.demo.sensor;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yue.demo.MainActivity;
import com.yue.demo.util.LogUtil;

/**
 * 加速传感器
 * 
 * @author chengyue
 * 
 */
public class AccelerometerTest extends Activity implements SensorEventListener {

	private String TAG = AccelerometerTest.class.getSimpleName();

	private SensorManager sensorManager;

	private TextView textView;
	private EditText editText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		// sensor = sensorManager.getDefaultSensor(SensorManager.);
	}

	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	protected void onStop() {
		sensorManager.unregisterListener(this);
		super.onStop();
	}

	// 以下是实现SensorEventListener接口必须实现的方法
	// 当传感器的值发生改变时回调该方法
	@Override
	public void onSensorChanged(SensorEvent event) {
		float[] values = event.values;
		StringBuilder sb = new StringBuilder();
		sb.append("X方向上的加速度：");
		sb.append(values[0]);
		sb.append("\nY方向上的加速度：");
		sb.append(values[1]);
		sb.append("\nZ方向上的加速度：");
		sb.append(values[2]);
		editText.setText(sb.toString());
	}

	/**
	 * 当传感器精度改变时回调该方法。
	 */
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		Toast.makeText(this, "onAccuracyChanged", Toast.LENGTH_LONG).show();
		LogUtil.d(TAG, "onAccuracyChanged");
	}

	private void initView() {
		LinearLayout ll = new LinearLayout(this);
		ll.setLayoutParams(MainActivity.layoutParams_mm);
		ll.setOrientation(LinearLayout.VERTICAL);

		textView = new TextView(this);
		textView.setLayoutParams(MainActivity.layoutParams_mw);
		textView.setText("传感器传回来的值");
		ll.addView(textView);

		editText = new EditText(this);
		editText.setLayoutParams(MainActivity.layoutParams_mw);
		editText.setEnabled(false);
		editText.setCursorVisible(false);
		ll.addView(editText);

		setContentView(ll);
	}
}
