package com.meng.config;

import com.meng.Autoreply;

import java.net.ServerSocket;

public class SocketDicManager implements Runnable {

    private ConfigManager configManager;

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

    private void check() {
        try {
            ServerSocket serverSocket = new ServerSocket(configManager.portConfig.dicPort);
            while (true) {
                Autoreply.instence.threadPool.execute(new SocketDicRunnable(configManager, serverSocket));
                Thread.sleep(10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
