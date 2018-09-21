package com.meng;

import com.sobte.cqp.jcq.message.CQCode;

public class Recoder {

	private int repeatCount = 0;
	private String lastMessage = "";
	private String lastReply = "";
	private long groupNum = 0;

	public Recoder(long group) {
		groupNum = group;
	}

	public boolean check(long group, String msg, CQCode CC) {
		boolean b=false;
		if (group == groupNum) {
			if (lastMessage.equals(msg) && 
					(!lastReply.equals(msg)) && 
					(CC.getAt(msg) != 1620628713L)) {
				if (repeatCount < 3) {
					Autoreply.sendGroupMessage(group, msg);
					lastReply = msg;
					repeatCount++;
				} else {
					Autoreply.sendGroupMessage(group, "你群天天复读");
					repeatCount = 0;
				}
				b=true;
			}
			if (!lastMessage.equals(msg)) {
				lastReply = "";
			}
			lastMessage = msg;
		}
		return b;

	}

}
