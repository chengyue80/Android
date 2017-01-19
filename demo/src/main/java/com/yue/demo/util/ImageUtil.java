package com.yue.demo.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.iflytek.android.framework.util.ImageUtils;
import com.iflytek.android.framework.util.StringUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import Decoder.BASE64Decoder;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

/**
 * 图片操作工具类
 * 
 * @author zgmao
 * 
 */
public class ImageUtil {

	private final static String TAG = ImageUtil.class.getSimpleName();

	/** 根目录 */
	private final String ROOT_FILE = "wjbz";
	/** 图片存放根目录 */
	private String rootPath = Environment.getExternalStorageDirectory()
			+ File.separator + ROOT_FILE + File.separator;
	/** 缓存图片存放地址 */
	private String catchImage;
	private static final int MAXWIDTH = 800;
	private static final int MAXHEIGHT = 480;
	/** 存放的图片格式 */
	private static String IMAGE_FORMAT = ".jpg";

	/**
	 * 构造函数，初始化相关变量
	 */
	public ImageUtil() {
		catchImage = rootPath + "image.jpg";
		File file = new File(rootPath);
		if (!file.exists()) {
			file.mkdir();
		}
	}

	/**
	 * 得到根目录
	 * 
	 * @return
	 */
	public String getRootPath() {
		return rootPath;
	}

	/**
	 * 得到临时图片目录
	 * 
	 * @return
	 */
	public String getCatchImage() {
		return catchImage;
	}

	/**
	 * 图片压缩处理:尺寸压缩
	 * 
	 * @param path
	 * @param outWidth
	 *            输出图片的宽
	 * @param outHeight
	 *            输出图片的高
	 * @return
	 * @throws Exception 
	 */
	public static Bitmap revitionImageSize(String srcPath, int outWidth,
			int outHeight) throws Exception {
		LogUtil.i(TAG, "srcPath : " + srcPath);
		Bitmap bitmap = null;
		try {
			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream(new File(srcPath)));
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
			newOpts.inJustDecodeBounds = true;
			bitmap = BitmapFactory.decodeStream(in, null, newOpts);
			// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
			in.close();
			int i = 0;
			while (true) {
				if ((newOpts.outWidth >> i <= outWidth)
						&& (newOpts.outHeight >> i <= outHeight)) {
					in = new BufferedInputStream(new FileInputStream(new File(
							srcPath)));
					newOpts.inSampleSize = (int) Math.pow(2.0D, i);
					newOpts.inJustDecodeBounds = false;
					// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
					if (in != null) {
						bitmap = BitmapFactory.decodeStream(in, null, newOpts);
						in.close();
					}
					break;
				}
				i += 1;
			}
			in.close();

			Log.d(TAG, "bitmap revitionImageSize w/h/i : " + bitmap.getWidth()
					+ "/" + bitmap.getHeight() + "/" + i);
		} catch (Exception e) {
			throw e;
		}
		return bitmap;
	}

	/**
	 * 图片压缩处理:尺寸压缩
	 * 
	 * @author chengyue
	 * @date 2015-6-2 下午2:29:20
	 * @version v1.0
	 * @param
	 * @return Bitmap
	 * @throws Exception
	 */
	public static Bitmap getimage(String srcPath) throws Exception {
		return compressImage(revitionImageSize(srcPath, 800, 800));// 压缩好比例大小后再进行质量压缩
	}

	/**
	 * 图片压缩：质量大小压缩
	 * 
	 * @author chengyue
	 * @date 2015-6-2 下午3:32:22
	 * @version v1.0
	 * @param
	 * @return Bitmap
	 * @throws Exception 
	 */
	private static Bitmap compressImage(Bitmap image) throws Exception {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
			int options = 100;
			int length = baos.toByteArray().length;

			Log.d(TAG, "bitmap getimage w/h/length : " + image.getWidth()
					+ "/" + image.getHeight() + "/" + length / 1024);

			while (length / 1024 > 500) { // 循环判断如果压缩后图片是否大于500kb,大于继续压缩
				baos.reset();// 重置baos即清空baos
				image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
				options -= 10;// 每次都减少10
				length = baos.toByteArray().length;
				if (options <= 0) {
					// options值得范围是0~100，如果小于0，则不再继续压缩
					length = 1014;
				}
			}
			ByteArrayInputStream isBm = new ByteArrayInputStream(
					baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
			Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
			return bitmap;
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	/**
	 * 将Base64位编码的图片进行解码，并保存到指定目录
	 * 
	 * @param base64
	 *            base64编码的图片信息
	 * @return
	 */
	public static void decodeBase64ToImage(String base64, String path,
			String imgName) {
		BASE64Decoder decoder = new BASE64Decoder();
		try {

			File file = new File(path);
			if (!file.exists()) {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			}
			File dir = new File(path + imgName);
			if (!dir.exists()) {
				// 在指定的文件夹中创建文件
				dir.createNewFile();
			}

			FileOutputStream write = new FileOutputStream(dir);
			byte[] decoderBytes = decoder.decodeBuffer(base64);
			write.write(decoderBytes);
			write.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ImageLoader imageLoader = ImageLoader.getInstance();

	/** 圆角半径大小(头像圆角处理) */
	public static final int CORNER_RADIUS_PIXELS = 40;

	/**
	 * 使用ImageLoader加载头像
	 * 
	 * @param imagePath
	 *            本地头像图片
	 * @param userImageUrl
	 *            网络头像图片
	 * @param context
	 */
	public static void loadRoundHead(Context context, int radius,
			String imagePath, String userImageUrl, ImageView imageView, int id) {
		if (imageLoader == null)
			imageLoader = ImageLoader.getInstance();
		// 圆角处理

		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(id)
				.showImageOnFail(id)
				.cacheInMemory(false)
				.cacheOnDisc(false)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(
						new RoundedBitmapDisplayer(DensityUtil.dip2px(context,
								radius)))
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();

		if (imagePath != null && new File(imagePath).exists()) {
			// 保存本地头像路径
			// 同步侧滑栏头像
			// new SlideMenuOneUtil(this).checkUser();
			String uri = "file://" + imagePath;

			imageLoader.displayImage(uri, imageView, options);
		} else {
			// 本地图片不存在，显示网络图片
			imageLoader.displayImage(userImageUrl, imageView, options);
		}
	}

	/**
	 * TODO 获取默认图片设置
	 * 
	 * @param displayImg
	 * @return
	 * @throw
	 * @return DisplayImageOptions
	 */
	public static DisplayImageOptions getImageOption(int displayImg) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(displayImg) // empty
				.showImageOnFail(displayImg) // 不是图片文件 显示图片
				.resetViewBeforeLoading(false) // default
				.cacheInMemory(false) // default 不缓存至内存
				.cacheOnDisc(false) // default 不缓存至手机SDCard
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)// default
				.bitmapConfig(Bitmap.Config.RGB_565) // default
				.build();

		return options;
	}
	
	
	/**
     * 判断系统中是否存在可以启动的相机应用
     *
     * @return 存在返回true，不存在返回false
     */
    public static boolean hasCamera(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
    
    /**
     * 获取图片的旋转角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照指定的角度进行旋转
     *
     * @param bitmap 需要旋转的图片
     * @param degree 指定的旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bitmap, int degree) {
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return newBitmap;
    }
}
