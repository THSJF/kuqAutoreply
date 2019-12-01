package com.meng.groupChat;

import java.util.*;

public class RepeaterManager {

    private HashMap<Long, Repeater> repeaters = new HashMap<>();

    public RepeaterManager() {

    }

    public void addData(Repeater r) {
        repeaters.put(r.groupNumber, r);
    }

    // 遍历集合查看是否需要复读
    public boolean check(long group, long qq, String msg) {
        Repeater repeaterBanner = repeaters.get(group);
        if (repeaterBanner == null) {
            repeaterBanner = new Repeater(group);
            addData(repeaterBanner);
        }
        return repeaterBanner.check(group, qq, msg);
    }
}
