package com.meng.config.javabeans;

public class PersonInfo extends Object {
    public String name = "";
    public long qq = 0;
    public int bid = 0;
    public int bliveRoom = 0;
    public boolean autoTip = false;

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
