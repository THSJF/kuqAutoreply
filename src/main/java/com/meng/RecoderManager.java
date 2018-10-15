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

	public boolean check(long group, long QQ, String msg) throws Exception {
		boolean b = false;
		for (int i = 0; i < mapFlag; i++) {
			b = b | recoderMap.get(i).check(group, msg, Autoreply.CC, Autoreply.appDirectory, QQ);
		}
		return b;
	}
}
