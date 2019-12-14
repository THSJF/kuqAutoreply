package com.meng.config.javabeans;

import java.util.*;

public class GroupConfig extends Object {
    public long groupNumber = 0;
    public boolean reply = true;
    private ArrayList<Boolean> booleans = new ArrayList<Boolean>(16);

    public int repeatMode = 0;

    @Override
    public int hashCode() {
        return (int) (groupNumber / 10);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GroupConfig)) {
            return false;
        }
        GroupConfig p = (GroupConfig) obj;
        return this.groupNumber == p.groupNumber && this.reply == p.reply && this.repeatMode == p.repeatMode;
    }

    public boolean isRepeat() {
        return booleans.get(0);
    }

    public boolean isSetu() {
        return booleans.get(1);
    }

    public boolean isPohai() {
        return booleans.get(2);
    }

    public boolean isDic() {
        return booleans.get(3);
    }

    public boolean isBilibiliCheck() {
        return booleans.get(4);
    }

    public boolean isCuigeng() {
        return booleans.get(5);
    }

    public boolean isSearchPic() {
        return booleans.get(6);
    }

    public boolean isSleep() {
        return booleans.get(7);
    }

    public boolean isRoll() {
        return booleans.get(8);
    }

    public boolean isBarcode() {
        return booleans.get(9);
    }

    public boolean isKuiping() {
        return booleans.get(10);
    }

    public boolean isCqCode() {
        return booleans.get(11);
    }

    public boolean isNvZhuang() {
        return booleans.get(12);
    }

    public boolean isMoshenfusong() {
        return booleans.get(13);
    }

    public boolean isCheHuiMoTu() {
        return booleans.get(14);
    }

}
