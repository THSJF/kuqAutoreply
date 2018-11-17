package com.meng.bilibili;

import java.util.HashMap;

import com.meng.Autoreply;
import com.meng.Methods;

public class LiveManager extends Thread {

	private HashMap<Integer, LivePerson> liveData = new HashMap<Integer, LivePerson>();
	private int mapFlag = 0;
	private final String liveString = "\"live_time\":\"0000-00-00 00:00:00\"";// 如果网页代码中包含这个字符串
																				// 则一定没有开播

	public static boolean liveStart = true;

	public LiveManager() {

	}

	@Override
	public void run() {
		while (true) {
			try {
				// 遍历hashmap检测是否直播
				if (liveStart) {
					for (int i = 0; i < mapFlag; i++) {
						LivePerson lPerson = liveData.get(i);
						String htmlData = Methods.getSourceCode(lPerson.getLiveUrl());
						boolean living = !htmlData.contains(liveString);
						lPerson.setLiving(living);
						if (lPerson.getFlag() != 0) {
							if (living && lPerson.isNeedStartTip()) {
								lPerson.setFlag(1);
							} else if (living && !lPerson.isNeedStartTip()) {
								lPerson.setFlag(2);
							} else if (!living && !lPerson.isNeedStartTip()) {
								lPerson.setFlag(3);
							} else if (!living && lPerson.isNeedStartTip()) {
								lPerson.setFlag(4);
							}
						}
						sendMsg(lPerson);
						sleep(2000);
					}
				}
				sleep(50000);
			} catch (Exception e) {
			}
		}

	}

	public int getMapFlag() {
		return mapFlag;
	}

	public LivePerson getPerson(int key) {
		return liveData.get(key);
	}

	public void addData(LivePerson person) {
		liveData.put(mapFlag, person);
		mapFlag++;
	}

	private void sendMsg(LivePerson p) throws Exception {
		switch (p.getFlag()) {
		case 0:
			if (p.isLiving()) {
				p.setNeedStartTip(false);
				p.setFlag(2);
			} else {
				p.setNeedStartTip(true);
				p.setFlag(4);
			}
			break;
		case 1:
			tipStart(p);
			p.setNeedStartTip(false);
			// 勿添加break;
		case 2:
			p.setLiving(true);
			break;
		case 3:
			tipFinish(p);
			p.setNeedStartTip(true);
			// 勿添加break;
		case 4:
			p.setLiving(false);
			break;
		}
	}

	private void tipStart(LivePerson p) {
		switch (p.getName()) {
		case "沙苗":
			Autoreply.sendGroupMessage(348595763L, "red:发发发");
			break;
		case "台长":
			Autoreply.sendGroupMessage(859561731L, "red:发发发");
			break;
		}
	}

	private void tipFinish(LivePerson p) {
		switch (p.getName()) {
		case "记者":
			Autoreply.sendGroupMessage(855927922L, Autoreply.CC.at(1012539034) + "该更新了吧");
			break;
		}
	}

}
