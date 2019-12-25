package com.meng.messageProcess;
import java.util.*;
import com.meng.*;

public class MessageWaitManager {
	private HashMap<Long,MessageWait> delayMsg=new HashMap<>();

	private void add(long InGroup, long toQQ, String msg) {
		delayMsg.put(toQQ, new MessageWait(InGroup, msg));
	}

	private void add(long toQQ, String msg) {
		delayMsg.put(toQQ, new MessageWait(-1, msg));
	}

	public boolean check(long fromGroup, long fromQQ, String msg) {
		if (delayMsg.keySet().size() == 0) {
			return false;
		}
		MessageWait mw = delayMsg.get(fromQQ);
		if (mw != null) {
			if (mw.g == -1) {
				Autoreply.sendMessage(fromGroup, 0, mw.c);
			} else if (mw.g == fromGroup) {
				Autoreply.sendMessage(fromGroup, 0, mw.c);
			}
		}
		return false;
	}
	private class MessageWait {
		public long g=0;
		public String c="";
		public MessageWait(long inGroup, String msg) {
			g = inGroup;
			c = msg;
		}
	}
}
