package com.yue.demo.sensor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.yue.demo.R;
import com.yue.demo.MainActivity;

/**
 * 水平仪
 * 
 * @author chengyue
 * 
 */
public class Gradienter extends Activity implements SensorEventListener {

	private String TAG = Gradienter.class.getSimpleName();

	private SensorManager sensorManager;

	private MyView myView;
	// 定义最大倾斜角度，超过则气泡位于边界
	private int MAX_ANGLE = 30;

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

		int sensorType = event.sensor.getType();
		switch (sensorType) {
		case Sensor.TYPE_ORIENTATION:
			// 获取手机顶部（0~-180）或尾部（0~180）翘起的角度
			float yAngle = event.values[1];
			// 获取手机左侧（0~-90）或右侧（0~90）翘起的角度
			float zAngle = event.values[2];
			// 气泡位于中间时（水平仪完全水平），气泡的X、Y座标
			int x = (myView.back.getWidth() - myView.bubble.getWidth()) / 2;
			int y = (myView.back.getHeight() - myView.bubble.getHeight()) / 2;
			// 如果与Z轴的倾斜角还在最大角度之内
			if (Math.abs(zAngle) <= MAX_ANGLE) {
				// 根据与Z轴的倾斜角度计算X座标的变化值（倾斜角度越大，X座标变化越大）
				int deltaX = (int) ((myView.back.getWidth() - myView.bubble
						.getWidth()) / 2 * zAngle / MAX_ANGLE);
				x += deltaX;
			}
			// 如果与Z轴的倾斜角已经大于MAX_ANGLE，气泡应到最左边
			else if (zAngle > MAX_ANGLE) {
				x = 0;
			}
			// 如果与Z轴的倾斜角已经小于负的MAX_ANGLE，气泡应到最右边
			else {
				x = myView.back.getWidth() - myView.bubble.getWidth();
			}
			// 如果与Y轴的倾斜角还在最大角度之内
			if (Math.abs(yAngle) <= MAX_ANGLE) {
				// 根据与Y轴的倾斜角度计算Y座标的变化值（倾斜角度越大，Y座标变化越大）
				int deltaY = (int) ((myView.back.getHeight() - myView.bubble
						.getHeight()) / 2 * yAngle / MAX_ANGLE);
				y += deltaY;
			}
			// 如果与Y轴的倾斜角已经大于MAX_ANGLE，气泡应到最下边
			else if (yAngle > MAX_ANGLE) {
				y = myView.back.getHeight() - myView.bubble.getHeight();
			}
			// 如果与Y轴的倾斜角已经小于负的MAX_ANGLE，气泡应到最右边
			else {
				y = 0;
			}
			// 如果计算出来的X、Y座标还位于水平仪的仪表盘内，更新水平仪的气泡座标
			if (isContain(x, y)) {
				myView.bubbleX = x;
				myView.bubbleY = y;
			}
			// 通知系统重回MyView组件
			myView.postInvalidate();
			break;
		}
	}

	// 计算x、y点的气泡是否处于水平仪的仪表盘内
	private boolean isContain(int x, int y) {
		// 计算气泡的圆心座标X、Y
		int bubbleCx = x + myView.bubble.getWidth() / 2;
		int bubbleCy = y + myView.bubble.getWidth() / 2;
		// 计算水平仪仪表盘的圆心座标X、Y
		int backCx = myView.back.getWidth() / 2;
		int backCy = myView.back.getWidth() / 2;
		// 计算气泡的圆心与水平仪仪表盘的圆心之间的距离。
		double distance = Math.sqrt((bubbleCx - backCx) * (bubbleCx - backCx)
				+ (bubbleCy - backCy) * (bubbleCy - backCy));
		// 若两个圆心的距离小于它们的半径差，即可认为处于该点的气泡依然位于仪表盘内
		if (distance < (myView.back.getWidth() - myView.bubble.getWidth()) / 2) {
			return true;
		} else {
			return false;
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

		myView = new MyView(this, null);
		myView.setLayoutParams(MainActivity.layoutParams_mw);
		ll.addView(myView);

		setContentView(ll);
	}

	class MyView extends View {

		// 定义水平仪仪表盘图片
		Bitmap back;
		// 定义水平仪中的气泡图标
		Bitmap bubble;
		// 定义水平仪中气泡的X、Y坐标
		int bubbleX, bubbleY;

		public MyView(Context context, AttributeSet attrs) {
			super(context, attrs);
			back = BitmapFactory.decodeResource(getResources(),
					R.drawable.gradienter_back);
			bubble = BitmapFactory.decodeResource(getResources(),
					R.drawable.bubble);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			// 绘制水平仪表盘图片
			canvas.drawBitmap(back, 0, 0, null);
			// 根据气泡坐标绘制气泡
			canvas.drawBitmap(bubble, bubbleX, bubbleY, null);
		}

	}
}
