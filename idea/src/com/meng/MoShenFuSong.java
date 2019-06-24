package com.meng;

import java.io.File;

public class MoShenFuSong extends Thread {
    private long fromGroup = 0;
    private int flag = 0;

    public MoShenFuSong(long fromGroup, int flag) {
        this.fromGroup = fromGroup;
        this.flag = flag;
    }

    @Override
    public void run() {
        File[] files = (new File(Autoreply.appDirectory + "膜神复诵/")).listFiles();
        File[] filesFFF = (new File(Autoreply.appDirectory + "发发发/")).listFiles();
        switch (flag) {
            case 0:
                for (int i = 0; i < 4; ++i) {
                    Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
                    sleeps(2000);
                }
                break;
            case 1:
                for (int i = 0; i < 5; ++i) {
                    Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
                    sleeps(1000);
                }
                break;
            case 2:
                for (int i = 0; i < 6; ++i) {
                    Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
                    sleeps(500);
                }
                break;
            case 3:
                for (int i = 0; i < 8; ++i) {
                    Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
                    sleeps(100);
                }
                break;
            case 4:
                if (fromGroup == 822438633L) {
                    for (int i = 0; i < 68; ++i) {
                        Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
                    }
                }
                break;
            case 5:
                for (int i = 0; i < 5; ++i) {
                    Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(filesFFF)));
                }
                break;
        }
    }

    private void sleeps(long ms) {
        try {
            sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
