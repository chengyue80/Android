package com.yue.demo.graphics;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.yue.demo.R;
import com.yue.demo.util.LogUtil;

/**
 * 使用MatrixTest控制变换
 * 
 * @author chengyue
 * 
 */
public class MatrixTest extends Activity {
	private EditText scaleEt;
	private EditText rotateEt;
	private EditText translateEt1;
	private EditText translateEt2;
	private ImageView imageView;
	private Matrix matrix;
	private Bitmap bitMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.matrix);

		scaleEt = (EditText) findViewById(R.id.et_scale);
		rotateEt = (EditText) findViewById(R.id.et_rotate);
		translateEt1 = (EditText) findViewById(R.id.et_translateX);
		translateEt2 = (EditText) findViewById(R.id.et_translateY);
		imageView = (ImageView) findViewById(R.id.iv_matrix);
		bitMap = BitmapFactory.decodeResource(getResources(), R.drawable.aaa);
		imageView.setImageBitmap(bitMap);
		matrix = new Matrix();
	}

	// 缩放
	public void scaleBitmap(View view) {
		matrix.postScale(getValues(scaleEt), getValues(scaleEt));
		imageView.setImageMatrix(matrix);
	}

	// 旋转
	public void rotateBitmap(View view) {
		int x = bitMap.getWidth() / 2;
		int y = bitMap.getHeight() / 2;
		matrix.postRotate(getValues(rotateEt), x, y);
		imageView.setImageMatrix(matrix);
	}

	// 平移
	public void translateBitmap(View view) {
		matrix.postTranslate(getValues(translateEt1), getValues(translateEt2));
		imageView.setImageMatrix(matrix);
	}

	// 还原
	public void clearMatrix(View view) {
		matrix.reset();
		imageView.setImageMatrix(matrix);
	}

	private float getValues(EditText et) {
		return Float.parseFloat(et.getText().toString());
	}

	class MatrixView extends View {

		private Bitmap bitmap;

		private Matrix matrix = new Matrix();

		// 设置倾斜度
		private float sx = 0.0f;

		private int width, height;
		// 缩放比例
		private float scale = 1.0f;
		// 判断缩放还是旋转
		private boolean isScale = false;

		public MatrixView(Context context) {
			super(context);
			bitmap = ((BitmapDrawable) context.getResources().getDrawable(
					R.drawable.aaa)).getBitmap();
			width = bitmap.getWidth();
			height = bitmap.getHeight();
			this.setFocusable(true);

		}

		public MatrixView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@SuppressLint("DrawAllocation")
		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			// 重置Matrix
			matrix.reset();
			if (!isScale) {// 旋转
				matrix.setSkew(sx, 0);
			} else {// 缩放
				matrix.setScale(scale, scale);// 长宽等比例缩放
			}

			Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, width, height,
					matrix, true);

			canvas.drawBitmap(bitmap2, matrix, null);
		}

		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			switch (keyCode) {
			// 向左倾斜
			case KeyEvent.KEYCODE_A:
				LogUtil.d(MainActivity_Graphics.Tag + "sx : " + sx);
				isScale = false;
				sx += 0.1;
				postInvalidate();
				break;
			// 向右倾斜
			case KeyEvent.KEYCODE_D:
				isScale = false;
				sx -= 0.1;
				postInvalidate();
				break;
			// 放大
			case KeyEvent.KEYCODE_W:
				isScale = true;
				if (scale < 2.0)
					scale += 0.1;
				postInvalidate();
				break;
			// 缩小
			case KeyEvent.KEYCODE_S:
				isScale = true;
				if (scale > 0.5)
					scale -= 0.1;
				postInvalidate();
				break;
			}
			return super.onKeyDown(keyCode, event);
		}
	}
}
