package com.yue.demo.io;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yue.demo.MainActivity;
import com.yue.demo.R;
import com.yue.demo.RootActivity;
import com.yue.demo.util.LogUtil;

/**
 * 手势监测
 * 
 * @author chengyue
 * 
 */
public class GestrueTest extends RootActivity implements OnGestureListener {

	private static final String Tag = GestrueTest.class.getSimpleName();

	// 定义手势检测实例
	GestureDetector detector = null;
	private ImageView imageView = null;

	// 初始的图片资源
	Bitmap bitmap;
	// 定义图片的宽、高
	int width, height;
	// 记录当前的缩放比
	float currentScale = 1;
	// 控制图片缩放的Matrix对象
	Matrix matrix;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();

		// 创建手势检测器
		detector = new GestureDetector(this, this);

		matrix = new Matrix();
		// 获取被缩放的源图片
		bitmap = BitmapFactory
				.decodeResource(getResources(), R.drawable.flower);
		// 获得位图宽、高
		width = bitmap.getWidth();
		height = bitmap.getHeight();
		// 设置ImageView初始化时显示的图片。
		imageView.setImageBitmap(BitmapFactory.decodeResource(
				this.getResources(), R.drawable.flower));
	}

	private void initView() {
		LinearLayout ll = new LinearLayout(this);
		ll.setLayoutParams(MainActivity.layoutParams_mm);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setBackgroundColor(Color.GREEN);

		imageView = new ImageView(this);
		imageView.setLayoutParams(MainActivity.layoutParams_mm);
		imageView.setScaleType(ScaleType.CENTER);
		ll.addView(imageView);

		setContentView(ll);

	}

	// 将Activity或组件上的触碰事件交给GestureDetector处理
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return detector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		LogUtil.d(Tag, "----onDown----");
		Toast.makeText(this, "onDown", Toast.LENGTH_SHORT).show();
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		LogUtil.d(Tag, "----onShowPress----");
		Toast.makeText(this, "onShowPress", Toast.LENGTH_SHORT).show();

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		LogUtil.d(Tag, "----onSingleTapUp----");
		Toast.makeText(this, "onSingleTapUp", Toast.LENGTH_SHORT).show();
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		LogUtil.d(Tag, "----onScroll----");
		// Toast.makeText(this, "onScroll", Toast.LENGTH_SHORT).show();
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		LogUtil.d(Tag, "----onLongPress----");
		Toast.makeText(this, "onLongPress", Toast.LENGTH_SHORT).show();

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		LogUtil.d(Tag, "----onFling----");
		Toast.makeText(this, "onFling", Toast.LENGTH_SHORT).show();
		velocityX = velocityX > 4000 ? 4000 : velocityX;
		velocityX = velocityX < -4000 ? -4000 : velocityX;
		// 根据手势的速度来计算缩放比，如果velocityX>0，放大图像，否则缩小图像。
		currentScale += currentScale * velocityX / 4000.0f;
		// 保证currentScale不会等于0
		currentScale = currentScale > 0.01f ? currentScale : 0.01f;

		LogUtil.i(Tag, "velocityX : " + velocityX + "  velocityY : " + velocityY
				+ "  currentScale : " + currentScale);
		LogUtil.i(Tag, "width : " + width + "  height : " + height);
		// 重置Matrix
		matrix.reset();
		// 缩放Matrix
		matrix.setScale(currentScale, currentScale, width / 2, height / 2);
		BitmapDrawable temp = (BitmapDrawable) imageView.getDrawable();
		// 如果图片还未回收，先强制回收该图片
		if (!temp.getBitmap().isRecycled())
			temp.getBitmap().recycle();
		// 根据原始位图和Matrix创建新图片
		Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		// 显示新的位图
		imageView.setImageBitmap(bitmap2);
		return false;
	}

}
