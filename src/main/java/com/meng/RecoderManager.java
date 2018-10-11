package com.meng;

import java.io.IOException;
import java.util.HashMap;

import com.sobte.cqp.jcq.message.CQCode;

public class RecoderManager {

	private HashMap<Integer, RecordBanner> recoderMap = new HashMap<Integer, RecordBanner>();
	private int mapFlag = 0;

	public RecoderManager() {

	}

	public void addData(RecordBanner r) {
		recoderMap.put(mapFlag, r);
		mapFlag++;
	}

	public boolean check(long group, String msg, CQCode CC, String appdirectory, long QQ) throws IOException {
		boolean b = false;
		for (int i = 0; i < mapFlag; i++) {
			b = b | recoderMap.get(i).check(group, msg, CC, appdirectory, QQ);
		}
		return b;
	}
}
