package com.meng.bilibili;

import java.util.ArrayList;
import java.util.HashMap;

import com.meng.Autoreply;
import com.meng.Methods;
import com.meng.config.ConfigJavaBean;
import com.meng.config.ConfigManager;

public class LiveManager extends Thread {

	private ArrayList<LivePerson> liveData = new ArrayList<>();
	private final String liveString = "\"live_time\":\"0000-00-00 00:00:00\"";// 如果网页代码中包含这个字符串

	public static boolean liveStart = true;

	public LiveManager(ConfigManager configManager) {
		for (ConfigJavaBean.BiliUser cb : configManager.configJavaBean.mapBiliUser) {
			liveData.add(new LivePerson(cb.name, "https://live.bilibili.com/" + cb.bid));
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				if (liveStart) {
					for (int i = 0; i < liveData.size(); i++) {
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
				sleep(60000);
			} catch (Exception e) {
			}
		}
	}

	public LivePerson getPerson(int key) {
		return liveData.get(key);
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
		case "台长":
			Autoreply.sendMessage(859561731L, 0, "想看台混矫正器");
			break;
		}
	}

	private void tipFinish(LivePerson p) {
		switch (p.getName()) {
		case "记者":
			Autoreply.sendMessage(855927922L, 0, Autoreply.CC.at(1012539034) + "该更新了吧");
			break;
		}
	}

}
