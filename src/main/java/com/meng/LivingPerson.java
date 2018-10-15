package com.meng;

public class LivingPerson {
	private String name = "";
	private int number = 0;
	private String liveUrl = "";
	private boolean isLiving = false;
	private boolean needStartTip = true;
	private int flag = 0;
	// 0直到状态改变前都不需要提示 1正在直播需要提示 2正在直播不需要提示 3未直播需要提示 4未直播不需要提示

	public LivingPerson(String name, String url) {
		this.name = name;
		number = Integer.parseInt(url.substring(url.indexOf("com") + 4));
		liveUrl = url;
	}

	public int getNumber() {
		return number;
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
