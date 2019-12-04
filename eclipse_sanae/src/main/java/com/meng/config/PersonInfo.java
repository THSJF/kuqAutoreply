package com.meng.config;

import java.util.*;

public class PersonInfo extends Object {
    public String name = "";
    public long qq = 0;
    public int bid = 0;
    public int bliveRoom = 0;
    public ArrayList<Long> tipIn = new ArrayList<>();
    public ArrayList<Boolean> tip = new ArrayList<>();

    public PersonInfo() {
        tip.add(true);//live
        tip.add(true);//video artical
        tip.add(false);//action
    }

    public boolean isTipLive() {
        return tip.get(0);
    }

    public boolean isTipVidoe() {
        return tip.get(1);
    }

    public boolean isTipAction() {
        return tip.get(2);
    }

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
