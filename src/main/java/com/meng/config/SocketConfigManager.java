package com.meng.config;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketConfigManager extends Thread {

	public boolean running = true; 

	ConfigManager configManager;

	public SocketConfigManager(ConfigManager configManager) {
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
			ServerSocket serverSocket = new ServerSocket(9760);
			System.out.println("***9760即将启动，等待客户端的链接***"); 
			while (running) {
				SocketConfigThread receiveThread = new SocketConfigThread(configManager, serverSocket);
				receiveThread.start();
				sleep(10);
			}
			serverSocket.close();
			interrupt();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
