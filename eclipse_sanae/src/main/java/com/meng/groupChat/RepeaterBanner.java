package com.meng.groupChat;


import com.meng.*;
import com.meng.config.javabeans.*;
import com.meng.messageProcess.*;
import com.meng.tools.*;
import java.io.*;

public class RepeaterBanner {
    private int repeatCount = 0;
    private int banCount = 6;
    private String lastMessageRecieved = "";
    private boolean lastStatus = false;
    long groupNumber = 0;
    private WarnMessageProcessor warnMessageProcessor;

    public RepeaterBanner(long groupNumber) {
        this.groupNumber = groupNumber;
        warnMessageProcessor = new WarnMessageProcessor();
    }

    public boolean check(long fromGroup, long fromQQ, String msg, File[] imageFiles) {
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
        boolean b = false;
        try {
            if (msg.contains("禁言") || fromGroup != groupNumber) {
                return true;
            }
			switch (groupConfig.repeatMode) {
                case 0:

                    break;
                case 1:
                    if (lastMessageRecieved.equals(msg)) { // 上一条消息和当前消息相同或两张图片相似度过高都是复读
                        if (Autoreply.instence.random.nextInt() % banCount == 0) {
                            int time = Autoreply.instence.random.nextInt(60) + 1;
                            banCount = 6;
                            if (Methods.ban(fromGroup, fromQQ, time)) {
                                Autoreply.sendMessage(0, fromQQ, "你从“群复读轮盘”中获得了" + time + "秒禁言套餐");
                            }
                        }
                    }
                    break;
                case 2:
                    if (lastMessageRecieved.equals(msg)) {
                        int time = Autoreply.instence.random.nextInt(60) + 1;
                        if (Methods.ban(fromGroup, fromQQ, time)) {
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

    // 复读状态
    private boolean checkRepeatStatu(long group, long qq, String msg) {
        boolean b = false;
        if (!lastStatus && (lastMessageRecieved.equals(msg))) {
            b = repeatStart(group, qq, msg);
        }
        if (lastStatus && (lastMessageRecieved.equals(msg))) {
            b = repeatRunning(group, qq, msg);
        }
        if (lastStatus && !lastMessageRecieved.equals(msg)) {
            b = repeatEnd(group, qq, msg);
        }
        lastStatus = lastMessageRecieved.equals(msg);
        return b;
    }

    private boolean repeatEnd(long group, long qq, String msg) {
        Autoreply.instence.useCount.incRepeatBreaker(qq);
        Autoreply.instence.groupCount.incRepeatBreaker(group);
        return false;
    }

    private boolean repeatRunning(long group, long qq, String msg) {
        Autoreply.instence.useCount.incFudu(qq);
        Autoreply.instence.groupCount.incFudu(group);
        banCount--;
        return false;
    }

    private boolean repeatStart(final long group, final long qq, final String msg) {
        banCount = 6;
        Autoreply.instence.useCount.incFudujiguanjia(qq);
        Autoreply.instence.groupCount.incFudu(group);
        Autoreply.instence.threadPool.execute(new Runnable() {
				@Override
				public void run() {
					reply(group, qq, msg);
				}
			});
        Autoreply.instence.useCount.incFudu(Autoreply.CQ.getLoginQQ());
        return true;
    }

    // 回复
    private boolean reply(long group, long qq, String msg) {
        if (msg.contains("蓝") || msg.contains("藍")) {
            return true;
        }
		replyText(group, qq, msg);
        return true;
    }

    // 如果是文本复读
    private void replyText(Long group, long qq, String msg) {
    	if (msg.contains("此生无悔入东方")) {
    		Autoreply.sendMessage(group, 0, msg);
    		return;
    	}
        if (repeatCount < 3) {
            Autoreply.sendMessage(group, 0, msg);
            repeatCount++;
        } else if (repeatCount == 3) {
            Autoreply.sendMessage(group, 0, "你群天天复读");
            repeatCount++;
        } else {
            String newmsg = new StringBuilder(msg).reverse().toString();
            Autoreply.sendMessage(group, 0, newmsg.equals(msg) ? newmsg + " " : newmsg);
            repeatCount = 0;
        }
    }
}
