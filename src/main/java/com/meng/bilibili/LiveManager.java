package com.meng.bilibili;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.meng.Autoreply;
import com.meng.Methods;
import com.meng.bilibili.spaceToLive.SpaceToLiveJavaBean;
import com.meng.config.ConfigManager;
import com.meng.config.javabeans.PersonInfo;

public class LiveManager extends Thread {

	private ArrayList<LivePerson> liveData = new ArrayList<>();

	public static boolean liveStart = true;

	public LiveManager(ConfigManager configManager) {
		for (PersonInfo cb : configManager.configJavaBean.personInfo) {
			if (cb.bliveRoom == 0) {
				continue;
			}
			liveData.add(new LivePerson(cb.name, cb.bid, cb.bliveRoom, false));
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				if (liveStart) {
					for (int i = 0; i < liveData.size(); i++) {
						LivePerson lPerson = liveData.get(i);
						SpaceToLiveJavaBean sjb = new Gson().fromJson(Methods.getSourceCode(
								"https://api.live.bilibili.com/room/v1/Room/getRoomInfoOld?mid=" + lPerson.getUid()),
								SpaceToLiveJavaBean.class);
						boolean living = sjb.data.liveStatus == 1;
						if (lPerson.liveUrl == "") {
							lPerson.liveUrl = sjb.data.url;
						}
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
		try {
			if (p.autoTip == true) {
				Autoreply.instence.naiManager.sendDanmaku(p.roomId, Autoreply.instence.naiManager.cookieSunny, "发发发");
				Autoreply.instence.naiManager.sendDanmaku(p.roomId, Autoreply.instence.naiManager.cookieLuna, "发发发");
				Autoreply.instence.naiManager.sendDanmaku(p.roomId, Autoreply.instence.naiManager.cookieStar, "发发发");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void tipFinish(LivePerson p) {
		switch (p.getName()) {
		case "台长":
			Autoreply.sendMessage(859561731L, 0, "呜呜呜");
			break;
		}
		try {
			if (p.autoTip == true) {
				Autoreply.instence.naiManager.sendDanmaku(p.roomId, Autoreply.instence.naiManager.cookieSunny, "呜呜呜");
				Autoreply.instence.naiManager.sendDanmaku(p.roomId, Autoreply.instence.naiManager.cookieLuna, "呜呜呜");
				Autoreply.instence.naiManager.sendDanmaku(p.roomId, Autoreply.instence.naiManager.cookieStar, "呜呜呜");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getliveCount() {
		return liveData.size();
	}
}
