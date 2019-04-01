package com.meng.config;

import java.io.*;
import java.net.Socket;

import com.meng.Autoreply;
import com.meng.bilibili.BilibiliUserJavaBean;

public class ReceiveThread extends Thread {
	// 和本线程相关的Socket
	Socket socket = null;
	ConfigManager configManager;

	public ReceiveThread(ConfigManager configManager, Socket socket) {
		this.socket = socket;
		this.configManager = configManager;
	}

	// 线程执行的操作，响应客户端的请求
	public void run() {
		if (!configManager.allowEdit) {
			return;
		}
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			is = socket.getInputStream();
			isr = new InputStreamReader(is); // 将字节流转化为字符流
			br = new BufferedReader(isr); // 添加缓冲
			String info = "";

			int bufferSize = 1024;
			char[] buffer = new char[bufferSize];
			StringBuilder out = new StringBuilder();
			Reader in = new InputStreamReader(is, "UTF-8");
			while (true) {
				int rsz = in.read(buffer, 0, buffer.length);
				if (rsz < 0)
					break;
				out.append(buffer, 0, rsz);
			}
			info = out.toString();

			try {
				String json = new String(Base64.decryptBASE64(info), "utf-8");
				configManager.jsonPersonInfo = json;
				configManager.bilibiliUserJavaBean = configManager.gson.fromJson(json, BilibiliUserJavaBean.class);
				try {
					FileOutputStream fos = null;
					OutputStreamWriter writer = null;
					File file = new File(Autoreply.appDirectory + "configPersonInfo.json");
					fos = new FileOutputStream(file);
					writer = new OutputStreamWriter(fos, "utf-8");
					writer.write(json);
					writer.flush();
					if (fos != null) {
						fos.close();
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
				if (isr != null)
					isr.close();
				if (br != null)
					br.close();
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				System.out.println(e.toString());
			}
		}
	}
}
