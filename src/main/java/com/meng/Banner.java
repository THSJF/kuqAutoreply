package com.meng;

public class Banner {

	public Banner() {
	}
	//manager发出指令的qq号 group收到消息的群 msg收到的消息
	public boolean checkBan(long manager, long group, String msg) {
		if (manager == 2856986197L||manager == 1594703250L) {
			//字符串无法转换为数字会NumberFormatException
			try {
				if (msg.equalsIgnoreCase("wholeban")) {
					Autoreply.CQ.setGroupWholeBan(group, true);
					return true;
				}
				if (msg.equalsIgnoreCase("wholerelease")) {
					Autoreply.CQ.setGroupWholeBan(group, false);
					return true;
				}
				String[] strs = msg.split("\\.");
				switch (strs.length) {
				case 2:
					if (strs[0].equalsIgnoreCase("wholeban")) {
						Autoreply.CQ.setGroupWholeBan(Long.parseLong(strs[1]), true);
						return true;
					} else if (strs[0].equalsIgnoreCase("wholerelease")) {
						Autoreply.CQ.setGroupWholeBan(Long.parseLong(strs[1]), false);
						return true;
					}
				case 3:
					if (strs[0].equalsIgnoreCase("ban")) {
						int sleepTime = Long.parseLong(strs[1]) == 2856986197L ? 1 : Integer.parseInt(strs[2]);
						Autoreply.CQ.setGroupBan(group, Long.parseLong(strs[1]), sleepTime);
						return true;
					}
					break;
				case 4:
					if (strs[0].equalsIgnoreCase("ban")) {
						int sleepTime = Long.parseLong(strs[2]) == 2856986197L ? 1 : Integer.parseInt(strs[3]);
						Autoreply.CQ.setGroupBan(Long.parseLong(strs[1]), Long.parseLong(strs[2]), sleepTime);
						return true;
					}
					break;
				}
			} catch (NumberFormatException e) {
				return true;
			}
		} else {
			try {
				String[] strings = msg.split("\\.");
				if (strings.length == 3 && strings[0].equalsIgnoreCase("ban")) {
					int sleepTime = Integer.parseInt(strings[2]);
					sleepTime = sleepTime > 120 ? 120 : sleepTime;
					sleepTime = sleepTime < 1 ? 1 : sleepTime;
					if (Long.parseLong(strings[1]) == 2856986197L) {
						Autoreply.CQ.setGroupBan(group, manager, sleepTime);
						return true;
					}
					if (Autoreply.random.nextInt() % 2 == 0) {
						Autoreply.CQ.setGroupBan(group, Long.parseLong(strings[1]), sleepTime);
					} else {
						if (manager == 1592608126L) {
							return true;
						}
						Autoreply.CQ.setGroupBan(group, manager, sleepTime);
					}
					return true;
				}
			} catch (NumberFormatException e) {
				return true;
			}
		}
		try {
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
				sleepTime = sleepTime > 2592000 ? 2592000 : sleepTime;
				sleepTime = sleepTime < 0 ? 0 : sleepTime;

				if (manager == 2856986197L || manager == 1592608126L || manager == 943486447L || manager == 183889179L
						|| manager == 350795616L) {
					sleepTime = 0;
				}
				Autoreply.CQ.setGroupBan(group, manager, sleepTime);
			}
		} catch (NumberFormatException e) {
			return true;
		}
		return false;
	}
}
