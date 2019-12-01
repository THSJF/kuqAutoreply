package com.meng.groupChat;


import com.meng.*;
import com.meng.config.javabeans.*;
import com.meng.messageProcess.*;

public class Repeater {
    private String lastMessageRecieved = "";
    private boolean lastStatus = false;
    long groupNumber = 0;
    private WarnMessageProcessor warnMessageProcessor;

    public Repeater(long groupNumber) {
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
        boolean b = false; 
		b = checkRepeatStatu(fromGroup, fromQQ, msg);
		lastMessageRecieved = msg;
        return b;
    }

    // 复读状态
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
        return false;
    }

    private boolean repeatStart(long group,  long qq,  String msg) {
        Autoreply.instence.useCount.incFudujiguanjia(qq);
        Autoreply.instence.groupCount.incFudu(group);
		Autoreply.sendMessage(group, 0, msg);
        Autoreply.instence.useCount.incFudu(Autoreply.CQ.getLoginQQ());
        return true;
    }
}
