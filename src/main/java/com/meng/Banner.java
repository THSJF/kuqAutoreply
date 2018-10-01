package com.meng;

import com.sobte.cqp.jcq.entity.CoolQ;

public class Banner {
	CoolQ CQ;

	public Banner(CoolQ cq) {
		CQ = cq;
	}

	public boolean checkBan(long manager, long group, String msg) {
		if (manager == 2856986197L || manager == 943486447L || manager == 183889179L || manager == 350795616L) {
			if (msg.equalsIgnoreCase("wholeban")) {
				CQ.setGroupWholeBan(group, true);
			}
			if (msg.equalsIgnoreCase("wholerelease")) {
				CQ.setGroupWholeBan(group, false);
			}
			String[] strings = msg.split("\\.");
			if (strings.length == 3 && strings[0].equalsIgnoreCase("ban")) {
				CQ.setGroupBan(group, Long.parseLong(strings[1]), Integer.parseInt(strings[2]));
			}
		}
		String[] st = msg.split("\\.");
		if (st.length == 3 && st[0].equalsIgnoreCase("sleep")) {
			int times = 1;
			switch (st[1]) {
			case "s":
				times = 1;
				break;
			case "min":
				times = 60;
				break;
			case "h":
				times = 3600;
				break;
			case "d":
				times = 86400;
				break;
			case "w":
				times = 604800;
				break;
			case "m":
				times = 2592000;
				break;
			}
			int sleepTime = Integer.parseInt(st[2]) * times;
			if (sleepTime < 0) {
				sleepTime = 0;
			}
			if (sleepTime > 2592000) {
				sleepTime = 2592000;
			}
			CQ.setGroupBan(group, manager, sleepTime);
			System.out.println("群" + group + "的用户" + manager + "获得了" + sleepTime + "秒睡眠套餐");
		}
		return false;
	}

}
