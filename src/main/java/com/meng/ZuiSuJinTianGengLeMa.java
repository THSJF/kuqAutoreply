package com.meng;

import java.util.Calendar;

public class ZuiSuJinTianGengLeMa extends Thread {
	// 每两个小时内最速说第一句话时催更
	Calendar c;
	long group = 855927922L;
	long jizhe = 1012539034L;
	boolean tiped = true;

	public ZuiSuJinTianGengLeMa() {
	}

	@Override
	public void run() {
		while (true) {
			c = Calendar.getInstance();
			if (c.get(Calendar.MINUTE) == 0) {
				if (c.get(Calendar.HOUR_OF_DAY) == 0) {
				}
				if (c.get(Calendar.HOUR_OF_DAY) % 2 == 0) {
					tiped = false;// 每两个小时重置提醒标记
					// Autoreply.sendGroupMessage(group, "最速更了吗");
				}
			}
			try {
				sleep(30000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public boolean check(long fromGroup, long fromQQ) {
		if (!tiped && fromGroup == group && fromQQ == jizhe) {
			Autoreply.sendGroupMessage(group, Autoreply.CC.at(jizhe) + "今天更新了吗");
			tiped = true;
			return true;
		}
		return false;
	}

}
