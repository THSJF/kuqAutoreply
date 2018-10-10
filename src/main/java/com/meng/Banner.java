package com.meng;

import com.sobte.cqp.jcq.entity.CoolQ;

public class Banner {
	CoolQ CQ;

	public Banner(CoolQ cq) {
		CQ = cq;
	}

	public boolean checkBan(long manager, long group, String msg) {
		if (manager == 2856986197L || manager == 1592608126L || manager == 943486447L || manager == 183889179L
				|| manager == 350795616L) {
			if (msg.equalsIgnoreCase("wholeban")) {
				CQ.setGroupWholeBan(group, true);
				return true;
			}
			if (msg.equalsIgnoreCase("wholerelease")) {
				CQ.setGroupWholeBan(group, false);
				return true;
			}
			String[] strings = msg.split("\\.");
			if (strings.length == 2 && strings[0].equalsIgnoreCase("wholeban")) {
				CQ.setGroupWholeBan(Long.parseLong(strings[1]), true);
				return true;
			}
			if (strings.length == 2 && strings[0].equalsIgnoreCase("wholerelease")) {
				CQ.setGroupWholeBan(Long.parseLong(strings[1]), false);
				return true;
			}
			if (strings.length == 3 && strings[0].equalsIgnoreCase("ban")) {
				int sleepTime = Integer.parseInt(strings[2]);
				if (Long.parseLong(strings[1]) == 2856986197L || Long.parseLong(strings[1]) == 1592608126L
						|| Long.parseLong(strings[1]) == 943486447L || Long.parseLong(strings[1]) == 183889179L
						|| Long.parseLong(strings[1]) == 350795616L) {
					sleepTime = 0;
				}
				CQ.setGroupBan(group, Long.parseLong(strings[1]), sleepTime);
				return true;
			}
			if (strings.length == 4 && strings[0].equalsIgnoreCase("ban")) {
				int sleepTime = Integer.parseInt(strings[3]);
				if (Long.parseLong(strings[2]) == 2856986197L || Long.parseLong(strings[2]) == 1592608126L
						|| Long.parseLong(strings[2]) == 943486447L || Long.parseLong(strings[2]) == 183889179L
						|| Long.parseLong(strings[2]) == 350795616L) {
					sleepTime = 0;
				}
				CQ.setGroupBan(Long.parseLong(strings[1]), Long.parseLong(strings[2]), sleepTime);
				return true;
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
			if (manager == 2856986197L || manager == 1592608126L || manager == 943486447L || manager == 183889179L
					|| manager == 350795616L) {
				sleepTime = 0;
			}
			CQ.setGroupBan(group, manager, sleepTime);
			System.out.println("群" + group + "的用户" + manager + "获得了" + sleepTime + "秒睡眠套餐");
		}
		return false;
	}

}
