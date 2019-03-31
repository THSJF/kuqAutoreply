package com.meng.config;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SendManager extends Thread {

	public boolean running = true; 

	ConfigManager configManager;

	public SendManager(ConfigManager configManager) {
		this.configManager = configManager;
	}

	@Override
	public void run() {
		try {
			check();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void check() throws Exception {
		try { 
			ServerSocket serverSocket = new ServerSocket(9961);
			Socket socket = null; 
			System.out.println("***服务器即将启动，等待客户端的链接***"); 
			while (running) {
				socket = serverSocket.accept();
				SendThread sendThread = new SendThread(configManager, socket);
				sendThread.start();
				InetAddress address = socket.getInetAddress();
				System.out.println("当前客户端的IP ： " + address.getHostAddress()); 
				sleep(10);
			}
			serverSocket.close();
			interrupt();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
