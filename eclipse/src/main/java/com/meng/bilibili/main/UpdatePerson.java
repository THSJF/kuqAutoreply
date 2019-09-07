package com.meng.bilibili.main;

public class UpdatePerson {

    public String name = "";
    public int bid = 0;
    public long lastVideo = 0;
    public long lastArtical = 0;
    public boolean needTipArtical = false;
    public boolean needTipVideo = false;

    public UpdatePerson(String name, int bid) {
        this.name = name;
        this.bid = bid;
    }

}