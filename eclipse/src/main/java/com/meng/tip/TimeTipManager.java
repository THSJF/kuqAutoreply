package com.meng.tip;

import com.meng.*;
import java.io.*;
import java.util.*;

public class TimeTipManager implements Runnable {
    private HashSet<TimeTipItem> hashSet = new HashSet<>();

    public TimeTipManager() {
        hashSet.add(new TimeTipItem(233861874L, -1, 1, new Runnable() {
							@Override
							public void run() {
								Autoreply.sendMessage(233861874L, 0, Autoreply.instence.CC.image(new File(Autoreply.appDirectory + "pic\\提醒\\jiemo.jpg")));
							}
						}));
        hashSet.add(new TimeTipItem(233861874L, -1, 1, new Runnable() {
							@Override
							public void run() {
								Autoreply.sendMessage(233861874L, 0, Autoreply.instence.CC.image(new File(Autoreply.appDirectory + "pic\\提醒\\jieli.jpg")));
							}
						}));
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
	
	private class TimeTipItem {
		public long targetGroup = -1;
		public long targetQQ = -1;
		public int hour = -1;
		public boolean needTip = false;
		public Runnable runnable;

		public TimeTipItem(long targetGroup, long targetQQ, int hour, Runnable runnable) {
			this.targetGroup = targetGroup;
			this.targetQQ = targetQQ;
			this.hour = hour;
			this.runnable = runnable;
		}
	}
}
