package com.yue.demo.media;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.BoringLayout.Metrics;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yue.demo.MainActivity;
import com.yue.demo.util.ConstantUtil;
import com.yue.demo.util.FileUtil;
import com.yue.demo.util.LogUtil;

public class MyCameraActivity extends Activity {
	private String TAG = MyCameraActivity.class.getSimpleName();

	protected static final int REQUEST_CODE_CAMERA = 1;
	private static final int REQUEST_CODE_ALBUM = 2;
	private static final int REQUEST_CODE_CROP = 3;
	/** Called when the activity is first created. */
	private Button camera, album;
	private ImageView imageView;

	private String imageOriginName = ConstantUtil.ANDROID_IMAGE_CAMERA_DIR
			+ "original.jpg";
	private String imageCropName = ConstantUtil.ANDROID_IMAGE_CAMERA_DIR
			+ "crop.jpg";
	private String imageRotateName = ConstantUtil.ANDROID_IMAGE_CAMERA_DIR
			+ "rotate.jpg";
	Bitmap photo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LinearLayout ll = new LinearLayout(this);
		ll.setLayoutParams(MainActivity.layoutParams_mm);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setGravity(Gravity.CENTER_HORIZONTAL);
		setContentView(ll);
		camera = new Button(this);
		camera.setLayoutParams(MainActivity.layoutParams_mw);
		camera.setText("调用系统相机");
		ll.addView(camera);

		camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getCaptureImg();
			}

		});
		album = new Button(this);
		album.setLayoutParams(MainActivity.layoutParams_mw);
		album.setText("调用系统相册");
		ll.addView(album);

		album.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_PICK);
				intent.setType("image/*");
				startActivityForResult(intent, REQUEST_CODE_ALBUM);
			}
		});

		Button button2 = new Button(this);
		button2.setLayoutParams(MainActivity.layoutParams_mw);
		button2.setText("相机自动对焦");
		ll.addView(button2);
		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MyCameraActivity.this,
						CaptureImage.class));

			}
		});
		imageView = new ImageView(this);
		imageView.setLayoutParams(MainActivity.layoutParams_mm);
		ll.addView(imageView);

		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					if (photo != null) {
						LogUtil.d(TAG, "=========");
						FileOutputStream outputStream = new FileOutputStream(
								imageRotateName);
						Matrix matrix = new Matrix();
						matrix.postRotate(90);
						imageView.setImageMatrix(matrix);
						photo = Bitmap.createBitmap(photo, 0, 0,
								photo.getWidth(), photo.getHeight(), matrix,
								true);
						photo.compress(Bitmap.CompressFormat.JPEG, 100,
								outputStream);
						imageView.setImageBitmap(photo);

					} else {
						LogUtil.d(TAG, "--------------");

					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {

			switch (requestCode) {
			case REQUEST_CODE_CAMERA:
				if (!FileUtil.isExistSDCard()) { // 检测sd是否可用
					Log.i("TestFile",
							"SD card is not avaiable/writeable right now.");
					return;
				}
				if (data == null) {
					File file = new File(imageOriginName);
					getCropImg(Uri.fromFile(file));
				} else {
					Bundle bundle = data.getExtras();
					// 获取相机返回的数据，并转换为Bitmap图片格式
					Bitmap bitmap = bundle.getParcelable("data");
					FileOutputStream b = null;
					// 创建文件夹
					try {
						FileUtil.createFile(imageOriginName);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
//					FileUtil.checkFile(imageOriginName, true);

					try {
						b = new FileOutputStream(imageOriginName);
						bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);//
						imageView.setImageBitmap(bitmap);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} finally {
						try {
							b.flush();
							b.close();
							return;
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

				// Bitmap bitmap = BitmapFactory
				// .decodeFile(ConstantUtil.ANDROID_IMAGE_CAMERA_DIR
				// + imageName);
				// imageView.setImageBitmap(bitmap);// 将图片显示在ImageView里
				break;

			case REQUEST_CODE_ALBUM:

				Uri selectedImage = data.getData();
				getCropImg(selectedImage);

				// Mlog.d("selectedImage path : " + selectedImage.getPath());
				// String picturePath = "";
				// if (!StringUtils.isEmpty(selectedImage.getAuthority())) {
				// String[] filePathColumns = { MediaStore.Images.Media.DATA };
				// Cursor cursor = this.getContentResolver().query(
				// selectedImage, filePathColumns, null, null, null);
				// if (null == cursor) {
				// return;
				// }
				// cursor.moveToFirst();
				// int columnIndex = cursor.getColumnIndex(filePathColumns[0]);
				// picturePath = cursor.getString(columnIndex);
				// cursor.close();
				// } else {
				// picturePath = selectedImage.getPath();
				// }

				break;
			case REQUEST_CODE_CROP:
				if (data != null) {
					setImageToView(data);
				}
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 调用系统相机获取图片
	 */
	private void getCaptureImg() {
		// 选择图片输出路径
		try {
			FileUtil.createFile(imageOriginName);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 设置这个当返回时data就会为空
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(imageOriginName)));
		intent.putExtra("type", "0");
		startActivityForResult(intent, REQUEST_CODE_CAMERA);
	}

	/**
	 * 调用系统裁剪图片
	 * 
	 * @param uri
	 */
	private void getCropImg(Uri uri) {
		LogUtil.d(TAG, "uri ： " + uri);
		if (uri == null) {
			LogUtil.i(TAG, "The uri is not exist.");
			return;
		}
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(imageCropName)));
		intent.putExtra("return-data", true);
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("scale", true);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

		intent.putExtra("noFaceDetection", true);
		this.startActivityForResult(intent, REQUEST_CODE_CROP);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setImageToView(Intent data) {

		Bundle extras = data.getExtras();
		if (extras != null) {
			photo = extras.getParcelable("data");
			LogUtil.d(TAG,
					"crop result w/h : " + photo.getWidth() + "/"
							+ photo.getHeight());
			imageView.setImageBitmap(photo);

		}
	}

}
