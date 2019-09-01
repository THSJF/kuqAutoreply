package com.meng.lookGroup;

import com.meng.Autoreply;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class IPGetter implements Runnable {

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
            e.printStackTrace();
        }
    }

    private void check() {
        try {
            ServerSocket serverSocket = new ServerSocket(4000 + port);
            Socket socket;
            System.out.println("***服务器即将启动，等待客户端的链接***");
            // 循环监听等待客户端的链接
            while (running) {
                socket = serverSocket.accept();
                Autoreply.instence.threadPool.execute(new ServerRunnable(socket));
                InetAddress address = socket.getInetAddress();
                System.out.println("当前客户端的IP ： " + address.getHostAddress());
                hSet.add(address.getHostAddress());
                System.out.println("客户端的数量: " + hSet.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
