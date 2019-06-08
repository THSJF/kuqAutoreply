package com.meng.tip;

import java.util.Calendar;
import java.util.HashSet;

import com.meng.Autoreply;

public class TimeTipManager extends Thread {
	public HashSet<TimeTipItem> hashSet = new HashSet<>();

	@Override
	public void run() {
		while (true) {
			Calendar c = Calendar.getInstance();
			for (TimeTipItem timeTipItem : hashSet) {
				timeTipItem.needTip = c.get(Calendar.HOUR_OF_DAY) % timeTipItem.hour == 0
						&& c.get(Calendar.MINUTE) == 0;
				if (timeTipItem.needTip) {
					timeTipItem.runnable.run();
				//	Autoreply.sendMessage(timeTipItem.targetGroup, 0, timeTipItem.tipMsg);
				}
			}
			try {
				sleep(60001);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
