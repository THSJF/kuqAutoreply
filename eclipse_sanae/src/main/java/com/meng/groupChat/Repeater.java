package com.meng.groupChat;

import com.meng.*;
import com.meng.config.*;
import com.meng.messageProcess.*;

public class Repeater {
    private String lastMessageRecieved = "";
    private boolean lastStatus = false;
    long groupNumber = 0;

    public Repeater(long groupNumber) {
        this.groupNumber = groupNumber;
	}

    public boolean check(long fromGroup, long fromQQ, String msg) {
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
		Autoreply.instence.configManager.send(SanaeDataPack.encode(SanaeDataPack._20incRepeatBreak).write(group).write(qq));
        return false;
    }

    private boolean repeatRunning(long group, long qq, String msg) {
		Autoreply.instence.configManager.send(SanaeDataPack.encode(SanaeDataPack._18incRepeat).write(group).write(qq));
        return false;
    }

    private boolean repeatStart(long group,  long qq,  String msg) {
		Autoreply.instence.configManager.send(SanaeDataPack.encode(SanaeDataPack._19incRepeatStart).write(qq));
		Autoreply.sendMessage(group, 0, msg);
        return true;
    }
}
