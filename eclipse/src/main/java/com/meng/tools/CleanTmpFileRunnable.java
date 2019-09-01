package com.meng.tools;

import java.io.File;

public class CleanTmpFileRunnable implements Runnable{
    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(86400000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            File[] files=new File("").listFiles();

        }
    }
}
