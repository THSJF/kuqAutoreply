package com.meng.messageProcess;
import java.util.*;
import com.meng.*;

public class MessageWaitManager {
	private ArrayList<MessageWait> delayMsg=new ArrayList<>();
	private boolean noTip=true;
	public void addTip(long InGroup, long toQQ, String msg) {
		delayMsg.add(new MessageWait(InGroup, toQQ, msg));
		noTip = false;
	}

	public void addTip(long toQQ, String msg) {
		delayMsg.add(new MessageWait(-1, toQQ, msg));
		noTip = false;
	}

	public boolean check(long fromGroup, long fromQQ) {
		boolean ret = false;
		if (noTip) {
			return ret;
		}
		Iterator<MessageWait> iter=delayMsg.iterator();
		while (iter.hasNext()) {
			MessageWait mw=iter.next();
			if (mw.q == fromQQ) {
				if (mw.g == -1) {
					Autoreply.sendMessage(fromGroup, 0, mw.c);
					iter.remove();
					ret = true;
				} else if (mw.g == fromGroup) {
					Autoreply.sendMessage(fromGroup, 0, mw.c);
					iter.remove();
					ret = true;
				}
			}
		}
		if (delayMsg.size() == 0) {
			noTip = true;
		}
		return ret;
	}
	private class MessageWait {
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
