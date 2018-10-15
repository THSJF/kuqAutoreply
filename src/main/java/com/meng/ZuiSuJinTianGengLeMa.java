package com.meng;

public class ZuiSuJinTianGengLeMa {
	//Calendar c;
	long group = 855927922L;
	long jizhe = 1012539034L;
	boolean tiped = false;

	public ZuiSuJinTianGengLeMa() {
	}

	/*@Override
	public void run() {
		while (true) {
			c = Calendar.getInstance();
			if (c.get(Calendar.MINUTE) == 0) {
				if (c.get(Calendar.HOUR_OF_DAY) == 0) {
				}
				if (c.get(Calendar.HOUR_OF_DAY) % 2 == 0) {
					tiped = false;
					// Autoreply.sendGroupMessage(group, "最速更了吗");
				}
			}
		}
	}*/

	public boolean check(long fromGroup, long fromQQ) {
		if (!tiped && fromGroup == group && fromQQ == jizhe) {
			Autoreply.sendGroupMessage(group, Autoreply.CC.at(jizhe) + "今天更新了吗");
			tiped = true;
			return true;
		}
		return false;
	}

}
