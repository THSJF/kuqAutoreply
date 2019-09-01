package com.meng.diaoXiongJiHua;

import com.meng.Autoreply;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class XiongIPGetter implements Runnable {

    public long fromQQ = 0;
    public boolean running = true;
    public int port = 0;
    public HashSet<String> hSet = new HashSet<>();

    public XiongIPGetter(long fromQQ, int port) {
        this.fromQQ = fromQQ;
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
            while (running) {
                socket = serverSocket.accept();
                InetAddress address = socket.getInetAddress();
                System.out.println("当前客户端的IP ： " + address.getHostAddress());
                if (!hSet.contains(address.getHostAddress())) {
                    Autoreply.instence.threadPool.execute(new XiongServerThread(socket, fromQQ, "新的连接 ： " + address.getHostAddress() + "\n"));
                    hSet.add(address.getHostAddress());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
