package com.meng;

public class ArchievementBean {
	private int a1=0;

	public static final int th6All=1;
	public static final int th7All=2;
	public static final int th8All=3;
	public static final int th10All=4;
	public static final int th11All=5;
	public static final int th12All=6;
	public static final int th13All=7;
	public static final int th14All=8;
	public static final int th15All=9;
	public static final int th16All=10;
	public static final int th17All=11;

	public boolean isArchievementGot(int archievement) {
		return (a1 & (1 << archievement)) != 0;
	}

	public void addArchievement(int Archievement) {
		a1 = a1 & (1 << Archievement);
	}
}
