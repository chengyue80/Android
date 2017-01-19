package com.yue.demo.util;

import android.os.Environment;

/**
 * 常量工具类
 * 
 * @author chengyue
 * @date 2015-5-30 上午10:30:34
 * @version v1.0
 */
public class ConstantUtil {

	/** 根目录 */
	public static final String ROOT_DIR = Environment
			.getExternalStorageDirectory().getPath();

	/** log日志存储目录 */
	public static final String LOG_DIR = ROOT_DIR + "/log/";

	/** android 工程 */
	public static final String DEMO_ANDROID = ROOT_DIR + "/myAndroidDemo/";

	/** android 图片文件 */
	public static final String ANDROID_IMAGE_DIR = DEMO_ANDROID + "image/";

	/** android 相机图册 */
	public static final String ANDROID_IMAGE_CAMERA_DIR = ANDROID_IMAGE_DIR
			+ "camera/";

	/** 多媒体 media */
	public static final String MEDIA = DEMO_ANDROID + "media/";

}
