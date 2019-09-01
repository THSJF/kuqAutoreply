package com.meng;

import java.io.File;

public class MessageSender {
    public int subType = 0;
    public int msgId = 0;
    public int font = 0;
    public long fromGroup = 0;
    public long fromQQ = 0;
    public String msg = "";
    public long timeStamp = 0;
    public File[] imageFiles;

    public MessageSender(long fromGroup, long fromQQ, String msg, long timeStamp, int msgId, File[] imageFiles) {
        this.fromGroup = fromGroup;
        this.fromQQ = fromQQ;
        this.msg = msg;
        this.timeStamp = timeStamp;
        this.msgId = msgId;
        this.imageFiles = imageFiles;
    }
}
