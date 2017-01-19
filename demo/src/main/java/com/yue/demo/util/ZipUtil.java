package com.yue.demo.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipOutputStream;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.zip.ZipEntry;

/**
 * zip文件压缩、解压工具类
 * 
 * @author chengyue
 * @date 2015-5-12
 * @version 1.0
 */
public class ZipUtil {

	/**
	 * @param srcFilePath
	 *            源文件的路径
	 * @param zipFilePath
	 *            目标压缩文件的路径
	 * @param zipFileName
	 *            目标压缩文件的名称
	 * @param encode
	 *            字符编码
	 */
	public static void zip(String srcFilePath, String zipFilePath,
			String zipFileName, String encode) {

		File inputFile = new File(srcFilePath);
		String path = zipFilePath + File.separator + zipFileName;
		if (!inputFile.exists())
			throw new RuntimeException("原始文件不存在!!!");
		File basetarZipFile = new File(path).getParentFile();
		if (!basetarZipFile.exists() && !basetarZipFile.mkdirs())
			throw new RuntimeException("目标文件无法创建!!!");
		BufferedOutputStream bos = null;
		FileOutputStream out = null;
		ZipOutputStream zOut = null;
		try {
			// 创建文件输出对象out,提示:注意中文支持
			out = new FileOutputStream(new String(path.getBytes(encode)));
			bos = new BufferedOutputStream(out);
			// 將文件輸出ZIP输出流接起来
			zOut = new ZipOutputStream(bos);
			zip(zOut, inputFile, inputFile.getName());
			CloseIoUtil.closeAll(zOut, bos, out);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void zip(ZipOutputStream zOut, File file, String base) {

		try {
			// 如果文件句柄是目录
			if (file.isDirectory()) {
				// 获取目录下的文件
				File[] listFiles = file.listFiles();
				// 建立ZIP条目
				zOut.putNextEntry(new ZipEntry(base + "/"));
				base = (base.length() == 0 ? "" : base + "/");
				if (listFiles != null && listFiles.length > 0)
					// 遍历目录下文件
					for (File f : listFiles)
						// 递归进入本方法
						zip(zOut, f, base + f.getName());
			}
			// 如果文件句柄是文件
			else {
				if (base == "") {
					base = file.getName();
				}
				// 填入文件句柄
				zOut.putNextEntry(new ZipEntry(base));
				// 开始压缩
				// 从文件入流读,写入ZIP 出流
				writeFile(zOut, file);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void writeFile(ZipOutputStream zOut, File file)
			throws IOException {

		FileInputStream in = null;
		BufferedInputStream bis = null;
		in = new FileInputStream(file);
		bis = new BufferedInputStream(in);
		int len = 0;
		byte[] buff = new byte[2048];
		while ((len = bis.read(buff)) != -1)
			zOut.write(buff, 0, len);
		zOut.flush();
		CloseIoUtil.closeAll(bis, in);
	}

	/****
	 * 解压
	 * 
	 * @param zipPath
	 *            zip文件路径
	 * @param destinationPath
	 *            解压的目的地点
	 * @param encode
	 *            文件名的编码字符集
	 */
	public static void unZip(String zipPath, String destinationPath,
			String encode) {

		File zipFile = new File(zipPath);
		if (!zipFile.exists())
			throw new RuntimeException("zip file " + zipPath
					+ " does not exist.");

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

	// public static void main(String[] args) {
	//
	// String srcPath = new String("E:/aa/iflytek/");
	// File file = new File(srcPath);
	// zip(srcPath, file.getParent(), "压缩文件.zip", "utf-8");
	// // String dir = new String("E:/aa");
	// unZip("E:/BZFamily.zip", "E:/aa", "gbk");
	// }

	static class CloseIoUtil {

		/***
		 * 关闭IO流
		 * 
		 * @param cls
		 */
		public static void closeAll(Closeable... cls) {

			if (cls != null) {
				for (Closeable cl : cls) {
					try {
						if (cl != null)
							cl.close();
					} catch (Exception e) {

					} finally {
						cl = null;
					}
				}
			}
		}
	}
}