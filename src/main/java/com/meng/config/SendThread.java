package com.meng.config;

import java.io.*;
import java.net.Socket;

public class SendThread extends Thread {
	Socket socket = null;
	ConfigManager configManager;

	public SendThread(ConfigManager configManager, Socket socket) {
		this.socket = socket;
		this.configManager = configManager;
	}

	public void run() {
		OutputStream os = null;
		PrintWriter pw = null;
		try {
			os = socket.getOutputStream();
			pw = new PrintWriter(os); // 包装为打印流
			String outstr = "";
			try {
				outstr = Base64.encryptBASE64(configManager.jsonBaseConfig.getBytes("utf-8"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			pw.write(outstr);
			pw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
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
