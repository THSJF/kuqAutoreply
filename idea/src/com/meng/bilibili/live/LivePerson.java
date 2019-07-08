package com.meng.bilibili.live;

public class LivePerson {

    public String name = "";
    public boolean autoTip = false;
    public int bid = 0;
    public int roomId = 0;
    public String liveUrl = "";
    public boolean living = false;
    public boolean needStartTip = true;
    public int flag = 0;
    public long liveStartTimeStamp = 0;
    // 0直到状态改变前都不需要提示 1正在直播需要提示 2正在直播不需要提示 3未直播需要提示 4未直播不需要提示

    public LivePerson(String name, int bid, int roomId, boolean autoTip) {
        this.name = name;
        this.bid = bid;
        this.autoTip = autoTip;
        this.roomId = roomId;
    }
}
