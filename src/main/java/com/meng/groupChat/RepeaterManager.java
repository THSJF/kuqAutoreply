package com.meng.groupChat;

import java.util.HashSet;

public class RepeaterManager {

	private HashSet<RepeaterBanner> repeaters = new HashSet<>();

	public RepeaterManager() {

	}

	public void addData(RepeaterBanner r) {
		repeaters.add(r);
	} 

	// 遍历集合查看是否需要复读
	public boolean check(long group, long QQ, String msg) {
		boolean b = false;
		for (RepeaterBanner repeaterBanner : repeaters) {
			try {
				b = b | repeaterBanner.check(group, msg, QQ);
			} catch (Exception e) {
				return false;
			}
		}
		return b;
	}
}
