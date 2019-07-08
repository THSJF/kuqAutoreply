package com.meng.bilibili.main;

public class UpdatePerson {

    public String name = "";
    public int uid = 0;
    public long lastVideo = 0;
    public long lastArtical = 0;
    public boolean needTipArtical = false;
    public boolean needTipVideo = false;

    public UpdatePerson(String name, int uid) {
        this.name = name;
        this.uid = uid;
    }

}
