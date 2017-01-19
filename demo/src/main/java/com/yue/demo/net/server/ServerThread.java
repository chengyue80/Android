package com.yue.demo.net.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerThread implements Runnable {

	// 定义当前线程所处理的Socket
	Socket s = null;
	// 该线程所处理的Socket所对应的输入流
	BufferedReader br = null;

	public ServerThread(Socket s) throws IOException {
		this.s = s;
		br = new BufferedReader(new InputStreamReader(s.getInputStream(),
				"utf-8"));
	}

	@Override
	public void run() {

		String content = null;

		try {
			while ((content = readFromClient()) != null) {

				System.out.println("content : "+content);
				Date date = new Date(System.currentTimeMillis());
				SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
				String dateString = format.format(date);
				// 遍历socketList中的每个Socket，
				// 将读到的内容向每个Socket发送一次
				for (Socket socket : MultiServer.socketList) {

					OutputStream os = socket.getOutputStream();
					os.write((dateString + "  " + content + "\n")
							.getBytes("utf-8"));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 定义读取客户端数据的方法
	private String readFromClient() {
		try {
			return br.readLine();
		}
		// 如果捕捉到异常，表明该Socket对应的客户端已经关闭
		catch (IOException e) {
			// 删除该Socket。
			MultiServer.socketList.remove(s); // ①
		}
		return null;
	}

}