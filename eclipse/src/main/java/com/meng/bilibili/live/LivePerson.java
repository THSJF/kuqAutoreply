package com.meng.bilibili.live;

import com.meng.config.javabeans.PersonInfo;

public class LivePerson {

    public String liveUrl = "";
    public boolean lastStatus = false;
    public boolean needTip = false;
    public long liveStartTimeStamp = 0;
    // 0直到状态改变前都不需要提示 1正在直播需要提示 2正在直播不需要提示 3未直播需要提示 4未直播不需要提示

}
