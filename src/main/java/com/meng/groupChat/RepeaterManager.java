package com.meng.groupChat;

import java.util.HashMap;

public class RepeaterManager {

	private HashMap<Integer, RepeaterBanner> recoderMap = new HashMap<Integer, RepeaterBanner>();
	private int mapFlag = 0;

	public RepeaterManager() {

	}

	public void addData(RepeaterBanner r) {
		recoderMap.put(mapFlag, r);
		mapFlag++;
	}

	// 遍历集合查看是否需要复读
	public boolean check(long group, long QQ, String msg) {
		boolean b = false;
		for (int i = 0; i < mapFlag; i++) {
			try {
				b = b | recoderMap.get(i).check(group, msg, QQ);
			} catch (Exception e) {
				return false;
			}
		}
		return b;
	}
}
