package com.meng.tip;

public class TimeTipItem {
	public long targetGroup = -1;
	public long targetQQ = -1;
	public int hour = -1;
	public boolean needTip = false;
	public Runnable runnable;

	public TimeTipItem(long targetGroup, long targetQQ, int hour, Runnable runnable) {
		this.targetGroup = targetGroup;
		this.targetQQ = targetQQ;
		this.hour = hour;
		this.runnable = runnable;
	}
}
