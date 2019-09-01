package com.meng.config;

import com.meng.Autoreply;

import java.net.ServerSocket;

public class SocketConfigManager implements Runnable {

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

    private void check() {
        try {
            ServerSocket serverSocket = new ServerSocket(configManager.portConfig.configPort);
            System.out.println("configPort启动");
            while (true) {
                Autoreply.instence.threadPool.execute(new SocketConfigRunnable(configManager, serverSocket));
                Thread.sleep(10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
