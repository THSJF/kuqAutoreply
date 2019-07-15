package com.meng.config.javabeans;

import java.util.ArrayList;

public class PersonInfo extends Object {
    public String name = "";
    public long qq = 0;
    public int bid = 0;
    public int bliveRoom = 0;
    public boolean autoTip = false;
    public ArrayList<Long> tipIn = new ArrayList<>();

    @Override
    public int hashCode() {
        return (int) (qq / 10);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PersonInfo)) {
            return false;
        }
        PersonInfo p = (PersonInfo) obj;
        return name.equals(p.name) && qq == p.qq && bid == p.bid && bliveRoom == p.bliveRoom;
    }
}
