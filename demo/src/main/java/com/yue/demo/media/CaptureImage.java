package com.yue.demo.media;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yue.demo.R;
import com.yue.demo.MainActivity;
import com.yue.demo.util.LogUtil;

/**
 * 拍照时自动对焦
 * 
 * @author chengyue
 * 
 */
public class CaptureImage extends Activity {

	private final String tag = CaptureImage.class.getSimpleName();

	private SurfaceView surfaceView;
	private ImageButton capture;
	private SurfaceHolder holder;
	private int screenHeight, screenWidth;
	// 定义系统所用的照相机
	Camera camera;
	// 是否在预览中
	boolean isPreview = false;
	protected AutoFocusCallback autoFocusCallback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		initView();

		WindowManager manager = getWindowManager();
		DisplayMetrics metrics = new DisplayMetrics();
		Display display = manager.getDefaultDisplay();
		display.getMetrics(metrics);
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;

		holder = surfaceView.getHolder();
		// 设置该Surface不需要自己维护缓冲区
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		holder.addCallback(new Callback() {

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				// 如果camera不为null ,释放摄像头
				if (camera != null) {

					if (isPreview)
						camera.stopPreview();
					camera.release();
					camera = null;
				}

			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				initCamera();

			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {

			}
		});

		capture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (camera != null) {
					// 控制摄像头自动对焦后才拍照
					camera.autoFocus(autoFocusCallback);
				}

			}
		});

		autoFocusCallback = new AutoFocusCallback() {

			/**
			 * 当自动对焦时激发该方法
			 */
			@Override
			public void onAutoFocus(boolean success, Camera camera) {
				if (success) {
					// takePicture()方法需要传入3个监听器参数
					// 第1个监听器：当用户按下快门时激发该监听器
					// 第2个监听器：当相机获取原始照片时激发该监听器
					// 第3个监听器：当相机获取JPG照片时激发该监听器
					camera.takePicture(new ShutterCallback() {

						@Override
						public void onShutter() {
							// TODO 按下快门瞬间会执行此处代码
						}
					}, new PictureCallback() {

						@Override
						public void onPictureTaken(byte[] data, Camera camera) {
							// TODO Auto-generated method stub
							// 此处代码可以决定是否需要保存原始照片信息
							if (data != null) {
								Bitmap bitmap = BitmapFactory.decodeByteArray(
										data, 0, data.length);

								LogUtil.d(tag,
										"2222 : \n" + "width : "
												+ bitmap.getWidth()
												+ "   height : "
												+ bitmap.getHeight());
							} else {

								LogUtil.d(tag, "原始 照片 data is null");
							}
						}
					}, pictureCallback);

				}
			}

			PictureCallback pictureCallback = new PictureCallback() {

				@Override
				public void onPictureTaken(byte[] data, Camera camera) {

					// 根据拍照所得的数据创建位图
					final Bitmap bitmap = BitmapFactory.decodeByteArray(data,
							0, data.length);

					LogUtil.d(tag, "1111 : \n" + "width : " + bitmap.getWidth()
							+ "   height : " + bitmap.getHeight());

					// 加载/layout/save.xml文件对应的布局资源
					View view = getLayoutInflater().inflate(
							R.layout.camera_save, null);
					final EditText name = (EditText) view
							.findViewById(R.id.phone_name);

					ImageView show = (ImageView) view
							.findViewById(R.id.photo_show);
					show.setImageBitmap(bitmap);

					new AlertDialog.Builder(CaptureImage.this)
							.setTitle("存储相片：")
							.setView(view)
							.setNegativeButton("取消", null)
							.setPositiveButton("保存",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// 创建一个位于SD卡上的文件
											File file = new File(
													Environment
															.getExternalStorageDirectory()
															.getAbsolutePath()
															+ "/DCIM/Camera/",
													name.getText().toString()
															+ ".jpg");
											FileOutputStream outputStream = null;

											try {
												outputStream = new FileOutputStream(
														file);

												bitmap.compress(
														CompressFormat.JPEG,
														100, outputStream);

											} catch (FileNotFoundException e) {
												e.printStackTrace();
											} finally {
												if (outputStream != null) {

													try {
														outputStream.close();
													} catch (IOException e) {
														e.printStackTrace();
													}

												}
											}
										}

									}).show();

					camera.stopPreview();
					camera.startPreview();
					isPreview = true;

				}
			};
		};

	}

	/** 打开摄像头 */
	protected void initCamera() {

		if (!isPreview) {
			// 默认打开后置摄像头。
			// 通过传入参数可以打开前置摄像头
			camera = Camera.open(0);
			camera.setDisplayOrientation(90);
			// camera.
		}

		if (camera != null && !isPreview) {

			Camera.Parameters parameters = camera.getParameters();
			parameters.setPreviewSize(screenWidth, screenHeight);
			// 设置预览照片时每秒显示多少帧的最小值和最大值
			parameters.setPreviewFpsRange(40, 100);
			parameters.setPictureSize(screenWidth, screenHeight);
			parameters.setPictureFormat(ImageFormat.JPEG);
			// 设置JPG照片的质量
			parameters.set("jpeg-quality", 300);

			try {
				camera.setPreviewDisplay(holder);
				// 开始预览
				camera.startPreview();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		isPreview = true;

	}

	@Override
	protected void onPause() {
		isPreview = false;
		if (camera != null &&isPreview)
			camera.stopPreview();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (camera != null && !isPreview) {
			camera.startPreview();
			isPreview = true;
		}
	}

	private void initView() {

		RelativeLayout ll = new RelativeLayout(this);
		ll.setLayoutParams(MainActivity.layoutParams_mm);
		// ll.setOrientation(LinearLayout.VERTICAL);

		// MainActivity.layoutParams_mm.weight = 1;
		surfaceView = new SurfaceView(this);
		surfaceView.setLayoutParams(MainActivity.layoutParams_mm);

		ll.addView(surfaceView);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		// capture 位于父 View 的底部，在父 View 中水平居中
		capture = new ImageButton(this);
		capture.setImageResource(R.drawable.capture);
		capture.setBaselineAlignBottom(true);
		ll.addView(capture, params);

		// lp3.addRule(RelativeLayout.BELOW, 1);
		// lp3.addRule(RelativeLayout.RIGHT_OF, 2);
		// lp3.addRule(RelativeLayout.ALIGN_RIGHT, 1);
		// // btn3 位于 btn1 的下方、btn2 的右方且其右边和 btn1 的右边对齐（要扩充）

		setContentView(ll);

	}
}
