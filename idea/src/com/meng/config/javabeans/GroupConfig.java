package com.meng.config.javabeans;

import java.util.ArrayList;

public class GroupConfig extends Object {
    public long groupNumber = 0;
    public boolean reply = true;
    private ArrayList<Boolean> booleans = new ArrayList<Boolean>(16);

    public int repeatMode = 0;
/*
	public boolean repeat = true;
	public boolean setu = false;
	public boolean pohai = false;
	public boolean dic = false;
	public boolean bilibiliCheck = false;
	public boolean cuigeng = false;
	public boolean searchPic = false;
	public boolean checkLink = false;
	public boolean roll = false;
	public boolean barcode = false;
	public boolean kuiping = false;
	public boolean cqCode = false;
	public boolean zan = false;
	public boolean moshenfusong = false;
*/

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

    public void setRepeat(boolean repeat) {
        booleans.set(0, repeat);
    }

    public boolean isRepeat() {
        return booleans.get(0);
    }

    public void setRepeatMode(int repeatMode) {
        this.repeatMode = repeatMode;
    }

    public int getRepeatMode() {
        return repeatMode;
    }

    public void setSetu(boolean setu) {
        booleans.set(1, setu);
    }

    public boolean isSetu() {
        return booleans.get(1);
    }

    public void setPohai(boolean pohai) {
        booleans.set(2, pohai);
    }

    public boolean isPohai() {
        return booleans.get(2);
    }

    public void setDic(boolean dic) {
        booleans.set(3, dic);
    }

    public boolean isDic() {
        return booleans.get(3);
    }

    public void setBilibiliCheck(boolean bilibiliCheck) {
        booleans.set(4, bilibiliCheck);
    }

    public boolean isBilibiliCheck() {
        return booleans.get(4);
    }

    public void setCuigeng(boolean cuigeng) {
        booleans.set(5, cuigeng);
    }

    public boolean isCuigeng() {
        return booleans.get(5);
    }

    public void setSearchPic(boolean searchPic) {
        booleans.set(6, searchPic);
    }

    public boolean isSearchPic() {
        return booleans.get(6);
    }

    public void setCheckLink(boolean checkLink) {
        booleans.set(7, checkLink);
    }

    public boolean isCheckLink() {
        return booleans.get(7);
    }

    public void setRoll(boolean roll) {
        booleans.set(8, roll);
    }

    public boolean isRoll() {
        return booleans.get(8);
    }

    public void setBarcode(boolean barcode) {
        booleans.set(9, barcode);
    }

    public boolean isBarcode() {
        return booleans.get(9);
    }

    public void setKuiping(boolean kuiping) {
        booleans.set(10, kuiping);
    }

    public boolean isKuiping() {
        return booleans.get(10);
    }

    public void setCqCode(boolean cqCode) {
        booleans.set(11, cqCode);
    }

    public boolean isCqCode() {
        return booleans.get(11);
    }

    public void setZan(boolean zan) {
        booleans.set(12, zan);
    }

    public boolean isZan() {
        return booleans.get(12);
    }

    public void setMoshenfusong(boolean moshenfusong) {
        booleans.set(13, moshenfusong);
    }

    public boolean isMoshenfusong() {
        return booleans.get(13);
    }

    public void setCheHuiMoTu(boolean cheHui) {
        booleans.set(14, cheHui);
    }

    public boolean isCheHuiMoTu() {
        return booleans.get(14);
    }

}
