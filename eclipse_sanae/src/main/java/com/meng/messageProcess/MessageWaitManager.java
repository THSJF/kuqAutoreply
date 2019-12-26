package com.meng.messageProcess;
import com.meng.*;
import com.meng.config.*;
import java.util.*;

public class MessageWaitManager {
	private boolean noTip=true;
	public void addTip(long InGroup, long toQQ, String msg) {
		ConfigManager.ins.SanaeConfig.delayMsg.add(new MessageWait(InGroup, toQQ, msg));
		noTip = false;
		ConfigManager.ins.saveSanaeConfig();
	}

	public void addTip(long toQQ, String msg) {
		ConfigManager.ins.SanaeConfig.delayMsg.add(new MessageWait(-1, toQQ, msg));
		noTip = false;
		ConfigManager.ins.saveSanaeConfig();
	}

	public void check(long fromGroup, long fromQQ) {
		if (noTip) {
			return;
		}
		Iterator<MessageWait> iter=ConfigManager.ins.SanaeConfig.delayMsg.iterator();
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
		if (ConfigManager.ins.SanaeConfig.delayMsg.size() == 0) {
			noTip = true;
		}
		ConfigManager.ins.saveSanaeConfig();
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
