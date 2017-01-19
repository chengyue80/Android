package com.yue.demo.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.util.Base64;

import com.iflytek.android.framework.util.StringUtils;

/**
 * 文件工具类
 * 
 * @author qyjiang
 * @datetime 2014年12月4日下午8:29:11
 */
public class FileUtil {

	private final static String TAG = FileUtil.class.getSimpleName();

	/**
	 * SD卡是否存在
	 * 
	 * @return
	 */
	public static boolean isExistSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

	/**
	 * SD卡剩余空间
	 * 
	 * @return
	 */
	public static long getSDFreeSize() {
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 空闲的数据块的数量
		long freeBlocks = sf.getAvailableBlocks();
		// 返回SD卡空闲大小
		// return freeBlocks * blockSize; //单位Byte
		// return (freeBlocks * blockSize)/1024; //单位KB
		return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
	}

	/**
	 * 检测指定路径文件是否存在，不存在则创建
	 * 
	 * @param filePath
	 * @throws IOException
	 */
	public static void createFile(String filePath) throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				if (file.getParentFile().mkdirs()) {
					System.out.println("文件夹‘" + file.getParentFile().getPath()
							+ "’不存在，已创建");
				} else {
					System.out.println("创建文件夹‘"
							+ file.getParentFile().getPath() + "’失败");
				}
				if (file.createNewFile()) {
					System.out.println("文件‘" + filePath + "’不存在，已创建");
				} else {
					System.out.println("创建文件‘" + filePath + "’失败");
				}
			}
		}
	}

	/**
	 * 检测指定文件夹是否存在，不存在则创建
	 * 
	 * @param filePath
	 * @throws IOException
	 */
	public static void createDir(String filePath) throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		} else if (file.isDirectory()) {
			file.delete();
			file.mkdirs();
		}
	}

	/**
	 * 文件是否是图片 doc docx
	 * 
	 * @param path
	 *            文件类型
	 * @return 文件是否满足条件
	 */
	public static boolean fileIsOk(String path) {
		if (StringUtils.isBlank(path)) {
			return false;
		}
		if (path.endsWith(".jpg") || path.endsWith(".png")
				|| path.endsWith(".doc") || path.endsWith(".docx")
				|| path.endsWith(".JPG") || path.endsWith(".PNG")
				|| path.endsWith(".DOC") || path.endsWith(".DOCX")) {
			return true;
		}
		return false;
	}

	/**
	 * 获取文件名
	 * 
	 * @return 文件名
	 */
	public static String getFileName(String fileURI) {
		String fileName = fileURI.substring(fileURI.lastIndexOf("/") + 1);
		return fileName;
	}

	/**
	 * 获取文件大小
	 * 
	 * @param fileURI
	 *            文件地址
	 * @return 文件大小
	 */
	public static long getFileSize(String fileURI) {
		File file = new File(fileURI);
		return file.length();
	}

	/**
	 * 获取文件类型
	 * 
	 * @param fileURI
	 *            文件地址
	 * @return 文件类型
	 */
	public static String getFileType(String fileURI) {
		String fileType = fileURI.substring(fileURI.lastIndexOf("."));
		return fileType;
	}

	/**
	 * 获取文件二进制流
	 * 
	 * @param fileURI
	 *            文件地址
	 * @return 文件二进制
	 */
	public static String getFileContent(final String fileURI) {

		try {

			FileInputStream inputFile = null;
			ByteArrayOutputStream baos = null;
			String uploadBuffer = "";
			baos = new ByteArrayOutputStream();
			File file = new File(fileURI);
			inputFile = new FileInputStream(file);
			baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = inputFile.read(buffer)) >= 0) {
				baos.write(buffer, 0, count);
			}

			uploadBuffer = new String(Base64.encode(baos.toByteArray(),
					Base64.DEFAULT));
			inputFile.close();
			baos.close();
			return uploadBuffer;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 由bitmap转化成二进制流
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static String getImageFileContent(final String path)
			throws Exception {

		try {
			final Bitmap bitmap = ImageUtil.getimage(path);

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			LogUtil.d(TAG, "bitmap width/height/size : " + bitmap.getWidth() + "/"
					+ bitmap.getHeight() + "/" + out.size() / 1024);

			out.flush();
			out.close();
			byte[] bu = out.toByteArray();
			String uploadBuffer = "";
			uploadBuffer = new String(Base64.encode(bu, Base64.DEFAULT));

			return uploadBuffer;
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 得到系统文件路径
	 * 
	 * @param context
	 * @param uri
	 * @return
	 */
	public static String getPath(Context context, Uri uri) {
		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = { "_data" };
			Cursor cursor = null;
			try {
				cursor = context.getContentResolver().query(uri, projection,
						null, null, null);
				int column_index = cursor.getColumnIndexOrThrow("_data");
				if (cursor.moveToFirst()) {
					return cursor.getString(column_index);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (null != cursor) {
					cursor.close();
				}
			}
		} else if ("file".equalsIgnoreCase(uri.getScheme())) {

			return uri.getPath();
		}
		return null;
	}

	/****
	 * 解压
	 * 
	 * @param zipPath
	 *            zip文件路径
	 * @param destinationPath
	 *            解压的目的路径
	 * @param encode
	 *            文件名的编码字符集
	 */
	public static void unZip(String zipPath, String destinationPath,
			String encode) {

		File zipFile = new File(zipPath);
		if (!zipFile.exists()) {
			return;
		}

		Project proj = new Project();
		Expand expand = new Expand();
		expand.setProject(proj);
		expand.setTaskType("unzip");
		expand.setTaskName("unzip");
		expand.setSrc(zipFile);
		expand.setDest(new File(destinationPath));
		expand.setEncoding(encode);
		expand.execute();
		System.out.println("unzip done!!!");
	}

	/**
	 * 将asset下的文件复制到sdcard文件夹下
	 * 
	 * @param context
	 * @param desDir
	 *            目标文件目录
	 * @param fileName
	 *            文件名
	 */
	public static void moveFileFromAsset(Context context, String desDir,
			String fileName) {
		// // 创建根目录
		// File rootFile = new File(ConstantUtil.DEMO_ANDROID);
		// if (!rootFile.exists()) {
		// rootFile.mkdirs();
		// } else if (!rootFile.isDirectory()) {
		// rootFile.delete();
		// rootFile.mkdirs();
		// }
		InputStream in = null;
		OutputStream outputStream = null;
		try {
			// 创建目标存储目录
			createDir(desDir);
			AssetManager am = context.getAssets();
			File desFile = new File(desDir + fileName);
			LogUtil.d(TAG, "file path : " + desDir + fileName);
			// sdcard上是否存在已存在该文件
			if (!desFile.exists()) {
				in = am.open(fileName);
				outputStream = new FileOutputStream(desFile);
				byte[] buffer = new byte[1024];
				int length = 0;
				while ((length = in.read(buffer)) > 0) {
					outputStream.write(buffer, 0, length);
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != outputStream) {
				try {
					outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

	}

	/**
	 * 递归删除文件夹
	 * 
	 * @author chengyue
	 * @date 2015-5-22 下午1:09:45
	 * @version v1.0
	 * @param 要删除的文件夹
	 * @return void
	 */
	public static void deleteFile(File file) {

		if (file == null || !file.exists()) {
			return;
		} else if (file.isFile()) {
			file.delete();
		} else if (file.isDirectory()) {
			File[] childFiles = file.listFiles();

			if (childFiles == null || childFiles.length == 0) {
				file.delete();
			} else {
				for (File child : childFiles) {
					deleteFile(child);
				}
			}

			file.delete();
		}
	}

}
