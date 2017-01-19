package com.yue.demo.net.multidown;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import com.yue.demo.util.LogUtil;

public class DownLoadUtil {

	// 定义下载资源的路径
	private String path;
	// 指定所下载的文件的保存位置
	private String targetFile;
	// 定义需要使用多少线程下载资源
	private int threadNum;
	// 定义下载的线程对象
	private DownThread[] threads;
	// 定义下载的文件的总大小
	private int fileSize;

	public DownLoadUtil(String path, String targetFile, int threadNum) {
		super();
		this.path = path;
		this.targetFile = targetFile;
		threads = new DownThread[threadNum];
		this.threadNum = threadNum;
	}

	public void download() {

		LogUtil.d(MultiThreadDown.tag, "download is run");
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(20 * 1000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty(
					"accept",
					"image/gif, image/jpeg, image/pjpeg, image/pjpeg, "
							+ "application/x-shockwave-flash, application/xaml+xml, "
							+ "application/vnd.ms-xpsdocument, application/x-ms-xbap, "
							+ "application/x-ms-application, application/vnd.ms-excel, "
							+ "application/vnd.ms-powerpoint, application/msword, */*");
			conn.setRequestProperty("Accept-Language", "zh-CN");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Connection", "Keep-Alive");

			fileSize = conn.getContentLength();

			LogUtil.d(MultiThreadDown.tag, "fileSize : " + fileSize);

			conn.disconnect();

			int currentPartSize = fileSize / threadNum + 1;
			LogUtil.d(MultiThreadDown.tag, "currentPartSize : " + currentPartSize);

			RandomAccessFile accessFile = new RandomAccessFile(targetFile, "rw");
			accessFile.setLength(fileSize);
			accessFile.close();

			for (int i = 0; i < threadNum; i++) {
				// 计算每条线程的下载的开始位置
				int startPos = i * currentPartSize;
				// 每个线程使用一个RandomAccessFile进行下载
				RandomAccessFile currentPart = new RandomAccessFile(targetFile,
						"rw");
				// 定位该线程的下载位置
				currentPart.seek(startPos);

				threads[i] = new DownThread(startPos, currentPartSize,
						currentPart);
				LogUtil.d(MultiThreadDown.tag, "thread " + i + " is start");
				threads[i].start();

			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取下载的完成百分比
	 * 
	 * @return
	 */
	public double getCompleteRate() {
		// 统计多条线程已经下载的总大小
		int sumSize = 0;
		for (int i = 0; i < threadNum; i++) {
			sumSize += threads[i].length;
		}
		// 返回已经完成的百分比
		return sumSize * 1.0 / fileSize;
	}

	private class DownThread extends Thread {

		// 当前线程的下载位置
		private int startPos;
		// 定义当前线程负责下载的文件大小
		private int currentPartSize;
		// 当前线程需要下载的文件块
		private RandomAccessFile currentPart;
		// 定义已经该线程已下载的字节数
		public int length;

		public DownThread(int startPos, int currentPartSize,
				RandomAccessFile currentPart) {
			super();
			this.startPos = startPos;
			this.currentPartSize = currentPartSize;
			this.currentPart = currentPart;
		}

		@Override
		public void run() {

			try {
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setConnectTimeout(5 * 1000);
				conn.setRequestMethod("GET");
				conn.setRequestProperty(
						"Accept",
						"image/gif, image/jpeg, image/pjpeg, image/pjpeg, "
								+ "application/x-shockwave-flash, application/xaml+xml, "
								+ "application/vnd.ms-xpsdocument, application/x-ms-xbap, "
								+ "application/x-ms-application, application/vnd.ms-excel, "
								+ "application/vnd.ms-powerpoint, application/msword, */*");
				conn.setRequestProperty("Accept-Language", "zh-CN");
				conn.setRequestProperty("Charset", "UTF-8");

				conn.getResponseMessage();

				LogUtil.d(MultiThreadDown.tag,
						"conn.getResponseCode() : " + conn.getResponseCode());
				if (conn.getResponseCode() == 200) {

					InputStream is = conn.getInputStream();
					// 跳过startPos个字节，表明该线程只下载自己负责哪部分文件。
					is.skip(startPos);
					BufferedReader br = new BufferedReader(
							new InputStreamReader(is));
					String content = null;
					while (length < currentPartSize
							&& (content = br.readLine()) != null) {
						currentPart.write(content.getBytes("utf-8"));
						length += content.getBytes().length;

					}
					currentPart.close();
					is.close();

				} else {
					LogUtil.d(MultiThreadDown.tag, "请求出错！");
				}
			} catch (ProtocolException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
