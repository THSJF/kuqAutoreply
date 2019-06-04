package com.meng.config;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.gson.Gson;
import com.meng.config.javabeans.GroupConfig;
import com.meng.config.javabeans.PersonInfo;

public class SocketConfigThread extends Thread {
	Socket socket = null;
	ConfigManager configManager;
	Gson gson = new Gson();

	public SocketConfigThread(ConfigManager configManager, ServerSocket serverSocket) {
		try {
			this.socket = serverSocket.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.configManager = configManager;
		InetAddress address = socket.getInetAddress();
		System.out.println("当前客户端的IP ： " + address.getHostAddress());
	}

	public void run() {

		try {
			InputStream inputStream = socket.getInputStream();
			DataInputStream dataInputStream = new DataInputStream(inputStream);
			String string = dataInputStream.readUTF();

			// string = new String(Base64.decryptBASE64(string), "utf-8");

			System.out.println("服务器读取客户端的：" + string);
			OutputStream outputStream = socket.getOutputStream();
			DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
			dataOutputStream.writeUTF(processText(string));
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String processText(String string) {

		if (string.equals("getFull")) {
			return configManager.gson.toJson(configManager.configJavaBean);
		}
		String type = string.substring(0, string.indexOf("."));
		String content = string.substring(string.indexOf(".") + 1);

		switch (NetworkType.valueOf(type)) {
		case addGroup:
			configManager.configJavaBean.groupConfigs.add(gson.fromJson(content, GroupConfig.class));
			break;
		case addNotReplyUser:
			configManager.configJavaBean.QQNotReply.add(Long.parseLong(content));
			break;
		case addNotReplyWord:
			configManager.configJavaBean.wordNotReply.add(content);
			break;
		case addPersonInfo:
			configManager.configJavaBean.personInfo.add(gson.fromJson(content, PersonInfo.class));
			break;
		case removeGroup:
			configManager.configJavaBean.groupConfigs.remove(Integer.parseInt(content));
			break;
		case removeNotReplyUser:
			configManager.configJavaBean.QQNotReply.remove(Integer.parseInt(content));
			break;
		case removeNotReplyWord:
			configManager.configJavaBean.wordNotReply.remove(Integer.parseInt(content));
			break;
		case removePersonInfo:
			configManager.configJavaBean.personInfo.remove((content));
			break;
		case setGroup:
			String[] split = content.split(" ");
			configManager.configJavaBean.groupConfigs.set(Integer.parseInt(split[0]),
					gson.fromJson(split[1], GroupConfig.class));
			break;
		case setNotReplyUser:
			String[] split2 = content.split(" ");
			configManager.configJavaBean.QQNotReply.set(Integer.parseInt(split2[0]), Long.parseLong(split2[1]));
			break;
		case setNotReplyWord:
			String[] split3 = content.split(" ");
			configManager.configJavaBean.wordNotReply.set(Integer.parseInt(split3[0]), split3[1]);
			break;
		case setPersonInfo:
			String[] split4 = content.split(" ");
			configManager.configJavaBean.personInfo.set(Integer.parseInt(split4[0]),
					gson.fromJson(split4[1], PersonInfo.class));
			break;
		default:
			return "fafafa";
		}
		configManager.saveConfig();
		return "ok";
	}

}
