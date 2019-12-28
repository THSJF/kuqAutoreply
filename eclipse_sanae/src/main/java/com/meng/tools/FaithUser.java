package com.meng.tools;

public class FaithUser {

	public static final int zan=0;
	public static final int biliVideo=1 ;
	public static final int biliLive=2;

	private int a1=0;

	public boolean isUsing(int function) {
		return (a1 & (1 << function)) != 0;
	}

	public void addUse(int function) {
		a1 = a1 | (1 << function);
	}

	public void subUse(int function) {
		if (!isUsing(function)) {
			return;
		}
		a1 = a1 - (1 << function);
	}
}
