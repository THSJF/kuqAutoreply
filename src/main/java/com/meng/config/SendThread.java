package com.meng.config;

import java.io.*;
import java.net.Socket;

public class SendThread extends Thread {
	// 和本线程相关的Socket
	Socket socket = null;
	ConfigManager configManager;

	public SendThread(ConfigManager configManager, Socket socket) {
		this.socket = socket;
		this.configManager = configManager;
	}

	// 线程执行的操作，响应客户端的请求
	public void run() {
		OutputStream os = null;
		PrintWriter pw = null;
		try {
			os = socket.getOutputStream();
			pw = new PrintWriter(os); // 包装为打印流
			String outstr = "";
			try {
				outstr = Base64.encryptBASE64(configManager.jsonPersonInfo.getBytes("utf-8"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			pw.write(outstr);
			pw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭资源
				if (pw != null)
					pw.close();
				if (os != null)
					os.close();
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				System.out.println(e.toString());
			}
		}
	}
}
