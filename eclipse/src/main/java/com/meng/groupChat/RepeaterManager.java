package com.meng.groupChat;

import com.meng.*;
import com.meng.config.javabeans.*;
import com.meng.messageProcess.*;
import com.meng.tools.*;
import java.util.*;

public class RepeaterManager {

    private HashMap<Long, RepeaterBanner> repeaters = new HashMap<>();

    public RepeaterManager() {

    }

	public void addRepeater(long group) {
		repeaters.put(group, new RepeaterBanner(group));
	}

    public boolean check(long group, long qq, String msg) {
        RepeaterBanner repeaterBanner = repeaters.get(group);
        if (repeaterBanner == null) {
            repeaterBanner = new RepeaterBanner(group);
            addRepeater(group);
        }
        return repeaterBanner.check(group, qq, msg);
    }

	private class RepeaterBanner {
		private int repeatCount = 0;
		private int banCount = 6;
		private String lastMessageRecieved = "";
		private boolean lastStatus = false;
		private long groupNumber = 0;
		private WarnMessageProcessor warnMessageProcessor;

		public RepeaterBanner(long groupNumber) {
			this.groupNumber = groupNumber;
			warnMessageProcessor = new WarnMessageProcessor();
		}

		public boolean check(long fromGroup, long fromQQ, String msg) {
			GroupConfig groupConfig = Autoreply.instence.configManager.getGroupConfig(fromGroup);
			if (groupConfig == null) {
				return false;
			}
			if (warnMessageProcessor.check(fromGroup, fromQQ, msg)) {
				return true;
			}
			if (msg.contains("~转账") || msg.contains("～转账")) {
				return true;
			}
			if (msg.contains("禁言") || fromGroup != groupNumber) {
				return true;
			}
			boolean b = false;
			try {
				switch (groupConfig.repeatMode) {
					case 0:

						break;
					case 1:
						if (lastMessageRecieved.equals(msg)) {
							if (Autoreply.instence.random.nextInt() % banCount == 0) {
								int time = Autoreply.instence.random.nextInt(60) + 1;
								banCount = 6;
								if (Tools.CQ.ban(fromGroup, fromQQ, time)) {
									Autoreply.sendMessage(0, fromQQ, "你从“群复读轮盘”中获得了" + time + "秒禁言套餐");
								}
							}
						}
						break;
					case 2:
						if (lastMessageRecieved.equals(msg)) {
							int time = Autoreply.instence.random.nextInt(60) + 1;
							if (Tools.CQ.ban(fromGroup, fromQQ, time)) {
								Autoreply.sendMessage(0, fromQQ, "你因复读获得了" + time + "秒禁言套餐");
							}
						}
						lastMessageRecieved = msg;
						return true;
				}
				b = checkRepeatStatu(fromGroup, fromQQ, msg);
				lastMessageRecieved = msg;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return b;
		}

		private boolean checkRepeatStatu(long group, long qq, String msg) {
			boolean b = false;
			if (!lastStatus && lastMessageRecieved.equals(msg)) {
				b = repeatStart(group, qq, msg);
			}
			if (lastStatus && lastMessageRecieved.equals(msg)) {
				b = repeatRunning(group, qq, msg);
			}
			if (lastStatus && !lastMessageRecieved.equals(msg)) {
				b = repeatEnd(group, qq, msg);
			}
			lastStatus = lastMessageRecieved.equals(msg) ;
			return b;
		}
		private boolean repeatStart(long group, long qq, String msg) {
			banCount = 6;
			Autoreply.instence.useCount.incFudujiguanjia(qq);
			Autoreply.instence.groupCount.incFudu(group);
			if (!msg.contains("蓝") && !msg.contains("藍")) {
				Autoreply.sendMessage(group, 0, msg);
				++repeatCount;
				Autoreply.instence.useCount.incFudu(Autoreply.CQ.getLoginQQ());
			}
			return true;
		}
		private boolean repeatRunning(long group, long qq, String msg) {
			Autoreply.instence.useCount.incFudu(qq);
			Autoreply.instence.groupCount.incFudu(group);
			banCount--;
			return false;
		}
		private boolean repeatEnd(long group, long qq, String msg) {
			Autoreply.instence.useCount.incRepeatBreaker(qq);
			Autoreply.instence.groupCount.incRepeatBreaker(group);
			return false;
		}
	}
}
