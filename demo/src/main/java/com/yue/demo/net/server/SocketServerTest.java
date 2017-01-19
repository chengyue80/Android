package com.yue.demo.net.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SocketServerTest {

	public static void main(String[] args) {
		try {
			ServerSocket ss = new ServerSocket(8888);

			while (true) {
				Socket s = ss.accept();

				OutputStream os = s.getOutputStream();

				Date date = new Date(System.currentTimeMillis());
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String dateString = format.format(date);
				os.write(("当前时间  ： " + dateString).getBytes("utf-8"));

				os.close();
				s.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
