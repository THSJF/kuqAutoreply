package com.meng.groupChat;

import java.io.File;
import java.util.HashMap;

public class RepeaterManager {

    private HashMap<Long, RepeaterBanner> repeaters = new HashMap<>();

    public RepeaterManager() {

    }

    public void addData(RepeaterBanner r) {
        repeaters.put(r.groupNumber, r);
    }

    // 遍历集合查看是否需要复读
    public boolean check(long group, long qq, String msg, File[] imageFiles) {
        RepeaterBanner repeaterBanner = repeaters.get(group);
        if (repeaterBanner == null) {
            repeaterBanner = new RepeaterBanner(group);
            addData(repeaterBanner);
        }
        return repeaterBanner.check(group, qq, msg, imageFiles);
    }
}
