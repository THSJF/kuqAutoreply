package com.meng.tools;

import com.meng.Autoreply;
import com.meng.tools.Methods;

import java.io.File;

public class MoShenFuSong implements Runnable {
    private long fromGroup;
    private long fromQQ;
    private int flag;

    public MoShenFuSong(long fromGroup, long fromQQ, int flag) {
        this.fromGroup = fromGroup;
        this.flag = flag;
        this.fromQQ = fromQQ;
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
                for (int i = 0; i < 24; ++i) {
                    Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
                    sleeps(100);
                }
                break;
            case 5:
                if (Autoreply.instence.configManager.isMaster(fromQQ)) {
                    for (int i = 0; i < 68; ++i) {
                        Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
                    }
                }
                break;
            case 6:
                for (int i = 0; i < 5; ++i) {
                    Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(filesFFF)));
                }
                break;
        }
    }

    private void sleeps(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
