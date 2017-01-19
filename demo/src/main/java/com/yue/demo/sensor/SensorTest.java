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

import com.yue.demo.R;
import com.yue.demo.MainActivity;

public class SensorTest extends Activity implements SensorEventListener {

	private String TAG = SensorTest.class.getSimpleName();

	private EditText orientation, magnetic, temperature, light, pressure;

	private SensorManager sensorManager;

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

		// 为系统的磁场传感器注册监听器
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_GAME);

		// 为系统的温度传感器注册监听器
		sensorManager
				.registerListener(this, sensorManager
						.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),
						SensorManager.SENSOR_DELAY_GAME);

		// 为系统的光传感器注册监听器
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
				SensorManager.SENSOR_DELAY_GAME);

		// 为系统的压力传感器注册监听器
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE),
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
		float[] values = event.values;

		int sensorType = event.sensor.getType();
		StringBuilder sb = null;
		// 判断是哪个传感器发生改变
		switch (sensorType) {
		// 方向传感器:
		case Sensor.TYPE_ORIENTATION:
			sb = new StringBuilder();
			sb.append("绕Z轴转过的角度：");
			sb.append(values[0]);
			sb.append("\n绕X轴转过的角度：");
			sb.append(values[1]);
			sb.append("\n绕Y轴转过的角度：");
			sb.append(values[2]);
			orientation.setText(sb.toString());

			break;
		// 磁场传感器
		case Sensor.TYPE_MAGNETIC_FIELD:
			sb = new StringBuilder();
			sb.append("X方向上的角度：");
			sb.append(values[0]);
			sb.append("\nY方向上的角度：");
			sb.append(values[1]);
			sb.append("\nZ方向上的角度：");
			sb.append(values[2]);
			magnetic.setText(sb.toString());
			break;
		// 温度传感器
		case Sensor.TYPE_AMBIENT_TEMPERATURE:
			sb = new StringBuilder();
			sb.append("当前温度为：");
			sb.append(values[0]);
			temperature.setText(sb.toString());
			break;
		// 光传感器
		case Sensor.TYPE_LIGHT:
			sb = new StringBuilder();
			sb.append("当前光的强度为：");
			sb.append(values[0]);
			light.setText(sb.toString());
			break;
		// 压力传感器
		case Sensor.TYPE_PRESSURE:
			sb = new StringBuilder();
			sb.append("当前压力为：");
			sb.append(values[0]);
			pressure.setText(sb.toString());
			break;
		}

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

		TextView textView = new TextView(this);
		textView.setLayoutParams(MainActivity.layoutParams_mw);
		textView.setText(R.string.orientation);
		ll.addView(textView);

		orientation = new EditText(this);
		orientation.setLayoutParams(MainActivity.layoutParams_mw);
		orientation.setEnabled(false);
		orientation.setCursorVisible(false);
		ll.addView(orientation);

		TextView textView2 = new TextView(this);
		textView2.setLayoutParams(MainActivity.layoutParams_mw);
		textView2.setText(R.string.magnetic);
		ll.addView(textView2);

		magnetic = new EditText(this);
		magnetic.setLayoutParams(MainActivity.layoutParams_mw);
		magnetic.setEnabled(false);
		magnetic.setCursorVisible(false);
		ll.addView(magnetic);

		TextView textView3 = new TextView(this);
		textView3.setLayoutParams(MainActivity.layoutParams_mw);
		textView3.setText(R.string.temperature);
		ll.addView(textView3);

		temperature = new EditText(this);
		temperature.setLayoutParams(MainActivity.layoutParams_mw);
		temperature.setEnabled(false);
		temperature.setCursorVisible(false);
		ll.addView(temperature);

		TextView textView4 = new TextView(this);
		textView4.setLayoutParams(MainActivity.layoutParams_mw);
		textView4.setText(R.string.light);
		ll.addView(textView4);

		light = new EditText(this);
		light.setLayoutParams(MainActivity.layoutParams_mw);
		light.setEnabled(false);
		light.setCursorVisible(false);
		ll.addView(light);

		TextView textView5 = new TextView(this);
		textView5.setLayoutParams(MainActivity.layoutParams_mw);
		textView5.setText(R.string.pressure);
		ll.addView(textView5);

		pressure = new EditText(this);
		pressure.setLayoutParams(MainActivity.layoutParams_mw);
		pressure.setEnabled(false);
		pressure.setCursorVisible(false);
		ll.addView(pressure);

		setContentView(ll);
	}

}
