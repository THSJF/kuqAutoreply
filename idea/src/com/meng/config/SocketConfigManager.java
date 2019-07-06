package com.meng.config;

import java.net.ServerSocket;

public class SocketConfigManager extends Thread {

    private boolean running = true;

    private ConfigManager configManager;

    SocketConfigManager(ConfigManager configManager) {
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
        ServerSocket serverSocket = new ServerSocket(configManager.portConfig.configPort);
        System.out.println("***configPort即将启动，等待客户端的链接***");
        while (running) {
            SocketConfigThread receiveThread = new SocketConfigThread(configManager, serverSocket);
            receiveThread.start();
            sleep(10);
        }
        serverSocket.close();
        interrupt();
    }
}