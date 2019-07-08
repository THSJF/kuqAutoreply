package com.meng.tip;

import java.io.File;
import java.util.Calendar;
import java.util.HashSet;

import com.meng.Autoreply;

public class TimeTipManager implements Runnable {
    private HashSet<TimeTipItem> hashSet = new HashSet<>();

    public TimeTipManager() {
        hashSet.add(new TimeTipItem(233861874L, -1, 1, () -> Autoreply.sendMessage(233861874L, 0, Autoreply.instence.CC.image(new File(Autoreply.appDirectory + "pic\\提醒\\jiemo.jpg")))));
        hashSet.add(new TimeTipItem(233861874L, -1, 1, () -> Autoreply.sendMessage(233861874L, 0, Autoreply.instence.CC.image(new File(Autoreply.appDirectory + "pic\\提醒\\jieli.jpg")))));
    }

    @Override
    public void run() {
        while (true) {
            Calendar c = Calendar.getInstance();
            for (TimeTipItem timeTipItem : hashSet) {
                timeTipItem.needTip = c.get(Calendar.HOUR_OF_DAY) % timeTipItem.hour == 0 && c.get(Calendar.MINUTE) == 0;
                if (timeTipItem.needTip) {
                    timeTipItem.runnable.run();
                    //	Autoreply.sendMessage(timeTipItem.targetGroup, 0, timeTipItem.tipMsg);
                }
            }
            try {
                Thread.sleep(60001);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
