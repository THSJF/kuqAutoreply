package com.meng;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class IPGetter extends Thread {

	public long fromGroup = 0;
	public boolean running = true;
	public int port = 0;
	public HashSet<String> hSet = new HashSet<>();

	public IPGetter(long fromGroup, int port) {
		this.fromGroup = fromGroup;
		this.port = port;
	}

	@Override
	public void run() {
		try {
			check();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void check() throws IOException {
		try {
			// 1、创建一个服务器端Socket,即ServerSocket, 指定绑定的端口，并监听此端口
			ServerSocket serverSocket = new ServerSocket(4000 + port);
			Socket socket = null;
			// 记录客户端的数量
			System.out.println("***服务器即将启动，等待客户端的链接***");
			// 循环监听等待客户端的链接
			while (running) {
				socket = serverSocket.accept();
				ServerThread serverThread = new ServerThread(socket);
				serverThread.start();
				InetAddress address = socket.getInetAddress();
				System.out.println("当前客户端的IP ： " + address.getHostAddress());
				hSet.add(address.getHostAddress());
				System.out.println("客户端的数量: " + hSet.size());
			}
			serverSocket.close();
			interrupt();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
