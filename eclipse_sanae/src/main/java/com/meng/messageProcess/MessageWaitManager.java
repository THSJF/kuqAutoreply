package com.meng.messageProcess;
import java.util.*;
import com.meng.*;

public class MessageWaitManager {
	private boolean noTip=true;
	public void addTip(long InGroup, long toQQ, String msg) {
		Autoreply.instence.configManager.SanaeConfig.delayMsg.add(new MessageWait(InGroup, toQQ, msg));
		noTip = false;
		Autoreply.instence.configManager.saveSanaeConfig();
	}

	public void addTip(long toQQ, String msg) {
		Autoreply.instence.configManager.SanaeConfig.delayMsg.add(new MessageWait(-1, toQQ, msg));
		noTip = false;
		Autoreply.instence.configManager.saveSanaeConfig();
	}

	public void check(long fromGroup, long fromQQ) {
		if (noTip) {
			return;
		}
		Iterator<MessageWait> iter=Autoreply.instence.configManager.SanaeConfig.delayMsg.iterator();
		while (iter.hasNext()) {
			MessageWait mw=iter.next();
			if (mw.q == fromQQ) {
				if (mw.g == -1) {
					Autoreply.sendMessage(fromGroup, 0, mw.c);
					iter.remove();
				} else if (mw.g == fromGroup) {
					Autoreply.sendMessage(fromGroup, 0, mw.c);
					iter.remove();
				}
			}
		}
		if (Autoreply.instence.configManager.SanaeConfig.delayMsg.size() == 0) {
			noTip = true;
		}
		Autoreply.instence.configManager.saveSanaeConfig();
		return;
	}
	public class MessageWait {
		public long g=0;
		public long q=0;
		public String c="";
		public MessageWait(long inGroup, long toQQ, String msg) {
			g = inGroup;
			q = toQQ;
			c = msg;
		}
	}
}
