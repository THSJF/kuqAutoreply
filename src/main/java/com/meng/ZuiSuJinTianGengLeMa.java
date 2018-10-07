package com.meng;

import java.util.Calendar;

import com.sobte.cqp.jcq.message.CQCode;

public class ZuiSuJinTianGengLeMa extends Thread {
	Calendar c;
	long group = 855927922L;
	long jizhe = 1012539034L;
	boolean tiped = false;
	CQCode CC;

	public ZuiSuJinTianGengLeMa(CQCode CC) {
		this.CC = CC;
	}

	@Override
	public void run() {
		while (true) {
			c = Calendar.getInstance();
			if (c.get(Calendar.MINUTE) == 0) {
				if (c.get(Calendar.HOUR_OF_DAY) == 0) {
					Autoreply.sendGroupMessage(857548607L, "签到");
					Autoreply.sendGroupMessage(857548607L, "泡茶");
					Autoreply.sendGroupMessage(857548607L, "泡茶");
					Autoreply.sendGroupMessage(857548607L, "牵手");
					Autoreply.sendGroupMessage(857548607L, "索求膝枕");
					Autoreply.sendGroupMessage(857548607L, "摸头");
					Autoreply.sendGroupMessage(857548607L, "摸头发");
					Autoreply.sendGroupMessage(857548607L, "恋萌萌");
					try {
						sleep(60000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Autoreply.sendPrivateMessage(1905073992L, "抽奖");
					Autoreply.sendPrivateMessage(1905073992L, "抽奖");
					Autoreply.sendPrivateMessage(1905073992L, "抽奖");
				}
				if (c.get(Calendar.HOUR_OF_DAY) % 2 == 0) {
					tiped = false;
					// Autoreply.sendGroupMessage(group, "最速更了吗");
				}
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

	public String getThing(String msg) {
		// 抽奖获得物品：蜂蜜蛋糕,消耗8pt
		String result = msg.substring(msg.indexOf("：") + 1, msg.indexOf(","));
		if (result.equals("昏睡红茶") || result.equals("野兽先辈ky") || result.equals("鬼杀酒")) {
			Autoreply.sendGroupMessage(857548607L, CC.at(2198634315L) + "赠送" + result);
			return "";
		}
		return "赠送" + result;
	}
}
