package com.meng;

import java.util.HashMap;

import com.sobte.cqp.jcq.message.CQCode;

public class RecoderManager {

	private HashMap<Integer, Recoder> recoderMap = new HashMap<Integer, Recoder>();
	private int mapFlag = 0;

	public RecoderManager() {

	}

	public void addData(Recoder r) {
		recoderMap.put(mapFlag, r);
		mapFlag++;
	}

	public boolean check(long group, String msg, CQCode CC) {
		boolean b=false;
		for (int i = 0; i < mapFlag; i++) {
			b=b|recoderMap.get(i).check(group, msg, CC);
		}
		return b;
	}
}
