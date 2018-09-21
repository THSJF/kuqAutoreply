package com.meng;

import java.util.HashMap;

public class DicReplyManager {

	private HashMap<Integer, DicReplyGroup> groupMap = new HashMap<Integer, DicReplyGroup>();
	private int mapFlag = 0;

	public DicReplyManager() {
		
	}

	public void addData(DicReplyGroup drp) {
		groupMap.put(mapFlag, drp);
		mapFlag++;
	}

	public boolean check(long group, String msg) {
		for (int i = 0; i < mapFlag; i++) {
			groupMap.get(i).checkMsg(group, msg);
		}
		return false;
	}

}
