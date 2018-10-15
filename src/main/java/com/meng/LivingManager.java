package com.meng;

import java.util.HashMap;

public class LivingManager extends Thread {

	private HashMap<Integer, LivingPerson> liveData = new HashMap<Integer, LivingPerson>();
	private int mapFlag = 0;
	private final String liveString = "\"live_time\":\"0000-00-00 00:00:00\"";

	public LivingManager() {
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				for (int i = 0; i < mapFlag; i++) {
					LivingPerson lPerson = liveData.get(i);
					String htmlData = Methods.open(lPerson.getLiveUrl());
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
				sleep(20000);
			} catch (Exception e) {
			}
		}

	}

	public int getMapFlag() {
		return mapFlag;
	}

	public LivingPerson getPerson(int key) {
		return liveData.get(key);
	}

	public void addData(LivingPerson person) {
		liveData.put(mapFlag, person);
		mapFlag++;
	}

	private void sendMsg(LivingPerson p) {
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
			// String tmp = p.getName() + "直播开始啦大家快去奶" + p.getLiveUrl();
			// Autoreply.sendGroupMessage(312342896L, tmp);// 学习
			// Autoreply.sendGroupMessage(855927922L, tmp);// 最速
			// Autoreply.sendGroupMessage(859561731L, tmp);// 东芳直播间
			// Autoreply.sendGroupMessage(807242547L, tmp);// c5
			// Autoreply.sendGroupMessage(826536230L, tmp);// stg闲聊群
			// Autoreply.sendGroupMessage(348595763L, tmp);// 沙苗のSTG群
			// Autoreply.sendGroupMessage(857548607L, tmp);// 恋萌萌粉丝群
			p.setNeedStartTip(false);
		case 2:
			p.setLiving(true);
			break;
		case 3:
			// String tmp2 = p.getName() + "直播被奶死莉";
			// Autoreply.sendGroupMessage(312342896L, tmp2);// 学习
			// Autoreply.sendGroupMessage(855927922L, tmp2);// 最速
			// Autoreply.sendGroupMessage(859561731L, tmp2);// 东芳直播间
			// Autoreply.sendGroupMessage(807242547L, tmp2);// 东芳直播间
			// Autoreply.sendGroupMessage(826536230L, tmp2);// stg闲聊群
			// Autoreply.sendGroupMessage(348595763L, tmp2);// 沙苗のSTG群
			// Autoreply.sendGroupMessage(857548607L, tmp2);// 恋萌萌粉丝群
			p.setNeedStartTip(true);
		case 4:
			p.setLiving(false);
			break;
		}
	}

}
