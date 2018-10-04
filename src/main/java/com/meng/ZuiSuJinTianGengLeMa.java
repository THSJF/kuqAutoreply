package com.meng;

import java.util.Calendar;

import com.sobte.cqp.jcq.message.CQCode;

public class ZuiSuJinTianGengLeMa extends Thread {
	Calendar c;
	long group = 855927922L;
	long jizhe = 1012539034L;
	boolean tiped = false;

	public ZuiSuJinTianGengLeMa() {
	}

	@Override
	public void run() {
		while (true) {
			c = Calendar.getInstance();
			if (c.get(Calendar.MINUTE) == 0) {
				if (c.get(Calendar.HOUR_OF_DAY) % 2 == 0) {
					tiped = false;
					Autoreply.sendGroupMessage(group, "最速更了吗");
				}
			}
			try {
				sleep(60000);
			} catch (InterruptedException e) {
			}
		}
	}

	public boolean check(long fromGroup, long fromQQ, CQCode CC) {
		if (!tiped && fromGroup == group && fromQQ == jizhe) {
			Autoreply.sendGroupMessage(group, CC.at(jizhe) + "今天更新了吗");
			tiped = true;
			return true;
		}
		return false;
	}
}
