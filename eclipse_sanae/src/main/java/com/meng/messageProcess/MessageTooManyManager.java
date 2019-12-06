package com.meng.messageProcess;
import java.util.concurrent.*;
import com.meng.*;

public class MessageTooManyManager {
	public ConcurrentHashMap<Long,MessageTooManyBean> msgMap=new ConcurrentHashMap<>();

	public MessageTooManyManager() {
		Autoreply.instence.threadPool.execute(new Runnable(){

				@Override
				public void run() {
					while (true) {
						for (MessageTooManyBean mb:msgMap.values()) {
							mb.lastSeconedMsgs = 0;
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {}
					}
				}
			});
	}
	
	public boolean checkMsgTooMany(long fromGroup, long fromQQ, String msg) {
		MessageTooManyBean mtmb=msgMap.get(fromQQ);
		//发言间隔过短
		if (System.currentTimeMillis() - mtmb.lastSpeakTimeStamp < 500) {
			++mtmb.timeSubLowTimes;
		} else {
			mtmb.timeSubLowTimes = 0;
		}
		if (mtmb.timeSubLowTimes > 5) {
			return true;
		}
		//重复次数过多
		mtmb.lastSpeakTimeStamp = System.currentTimeMillis();
		if (mtmb.lastMsg.equals(msg)) {
			++mtmb.repeatTime;
		} else {
			mtmb.repeatTime = 0;
		}
		if (mtmb.repeatTime > 5) {
			return true;
		}
		//一秒内消息过多
		++mtmb.lastSeconedMsgs;
		if (mtmb.lastSeconedMsgs > 4) {
			return true;
		}
		return false;
	}
}
