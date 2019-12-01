package com.meng.messageProcess;

import com.meng.*;

import static com.meng.Autoreply.sendMessage;

public class PrivateMsgRunnable implements Runnable {
    private int subType = 0;
    private int msgId = 0;
    private long fromGroup = 0;
    private long fromQQ = 0;
    private String fromAnonymous = "";
    private String msg = "";
    private int font = 0;
    private long timeStamp = 0;

    public PrivateMsgRunnable(MessageSender ms) {
        font = ms.font;
        fromGroup = ms.fromGroup;
        fromQQ = ms.fromQQ;
        msg = ms.msg;
        msgId = ms.msgId;
        subType = ms.subType;
        timeStamp = ms.timeStamp;
    }

    @Override
    public synchronized void run() {
        if (msg.equals("查看统计")) {
            sendMessage(fromGroup, fromQQ, Autoreply.instence.useCount.getMyCount(fromQQ));
            return;
        } else if (msg.equals("查看排行")) {
            sendMessage(fromGroup, fromQQ, Autoreply.instence.useCount.getTheFirst());
            return ;
        }
    }
}
