package com.meng.config;

import java.io.*;
import java.net.Socket;

import com.meng.Autoreply;

public class ReceiveThread extends Thread {
	Socket socket = null;
	ConfigManager configManager;

	public ReceiveThread(ConfigManager configManager, Socket socket) {
		this.socket = socket;
		this.configManager = configManager;
	}

	public void run() {
		if (!configManager.allowEdit) {
			return;
		}
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			is = socket.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
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
				configManager.jsonBaseConfig = json;
				configManager.configJavaBean = configManager.gson.fromJson(json, ConfigJavaBean.class);
				try {
					FileOutputStream fos = null;
					OutputStreamWriter writer = null;
					File file = new File(Autoreply.appDirectory + "config.json");
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
