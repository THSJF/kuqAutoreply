package com.meng.bilibili;

public class LivePerson {

	private String name = "";
	public boolean autoTip = false;
	private int uid = 0;
	public int roomId = 0;
	public String liveUrl = "";
	private boolean isLiving = false;
	private boolean needStartTip = true;
	private int flag = 0;
	// 0直到状态改变前都不需要提示 1正在直播需要提示 2正在直播不需要提示 3未直播需要提示 4未直播不需要提示

	public LivePerson(String name, int uid, int roomId, boolean autoTip) {
		this.name = name;
		this.uid = uid;
		this.autoTip = autoTip;
		this.roomId = roomId;
	}

	public int getUid() {
		return uid;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getFlag() {
		return flag;
	}

	public String getName() {
		return name;
	}

	public boolean isNeedStartTip() {
		return needStartTip;
	}

	public void setNeedStartTip(boolean tip) {
		needStartTip = tip;
	}

	public String getLiveUrl() {
		return liveUrl;
	}

	public boolean isLiving() {
		return isLiving;
	}

	public void setLiving(boolean b) {
		isLiving = b;
	}

}
