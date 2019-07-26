package com.meng.tools;

import com.meng.Autoreply;

import java.io.File;

public class CleanRunnable implements Runnable {
    @Override
    public void run() {
        while (true) {
            File[] tmpFiles = new File(Autoreply.appDirectory + "downloadImages/").listFiles();
            for (File f : tmpFiles) {
                f.delete();
            }
            System.out.println("正在删除临时文件" + new File(Autoreply.appDirectory + "downloadImages/").getAbsolutePath());
            try {
                Thread.sleep(86400000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
