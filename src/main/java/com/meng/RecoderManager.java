package com.meng;

import java.util.HashMap;

public class RecoderManager {

	private HashMap<Integer, RecordBanner> recoderMap = new HashMap<Integer, RecordBanner>();
	private int mapFlag = 0;

	public RecoderManager() {

	}

	public void addData(RecordBanner r) {
		recoderMap.put(mapFlag, r);
		mapFlag++;
	}

	// 遍历集合查看是否需要复读
	public boolean check(long group, long QQ, String msg){
		boolean b = false;
		for (int i = 0; i < mapFlag; i++) {
			try {
				b = b | recoderMap.get(i).check(group, msg, QQ);
			} catch (Exception e) {return false;
			}
		}
		return b;
	}
}
