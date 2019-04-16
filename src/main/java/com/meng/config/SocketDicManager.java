package com.meng.config;

import java.net.ServerSocket;

public class SocketDicManager extends Thread {

	public boolean running = true; 

	ConfigManager configManager;

	public SocketDicManager(ConfigManager configManager) {
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
			ServerSocket serverSocket = new ServerSocket(9999);
			System.out.println("***9999dic即将启动，等待客户端的链接***"); 
			while (running) {
				SocketDicThread receiveThread = new SocketDicThread(configManager, serverSocket);
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
