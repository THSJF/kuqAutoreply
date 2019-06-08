package com.meng.bilibili;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.gson.Gson;
import com.meng.Autoreply;
import com.meng.Methods;
import com.meng.bilibili.spaceToLive.SpaceToLiveJavaBean;
import com.meng.config.ConfigManager;
import com.meng.config.javabeans.PersonInfo;

public class LiveManager extends Thread {

	private ArrayList<LivePerson> liveData = new ArrayList<>();

	public static boolean liveStart = true;
	public HashMap<String, String> liveTimeMap = new HashMap<>();

	public LiveManager(ConfigManager configManager) {
		ArrayList<PersonInfo> list = configManager.getPersonInfoList();
		for (PersonInfo cb : list) {
			if (cb.bliveRoom == 0) {
				continue;
			}
			liveData.add(new LivePerson(cb.name, cb.bid, cb.bliveRoom, cb.autoTip));
		}
		File liveTimeFile = new File(Autoreply.appDirectory + "liveTime.json");
		if (!liveTimeFile.exists()) {
			saveConfig();
		}
		try {
			liveTimeMap = new Gson().fromJson(Methods.readFileToString(liveTimeFile.getAbsolutePath()), HashMap.class);
			System.out.println(liveTimeMap);
		} catch (Exception e) {
			e.printStackTrace();
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
						lPerson.liveUrl = sjb.data.url;
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
				p.liveStartTimeStamp = System.currentTimeMillis() / 1000;
				p.setNeedStartTip(false);
				p.setFlag(2);
			} else {
				p.setNeedStartTip(true);
				p.setFlag(4);
			}
			break;
		case 1:
			tipStart(p);
			p.liveStartTimeStamp = System.currentTimeMillis() / 1000;
			p.setNeedStartTip(false);
			// 勿添加break;
		case 2:
			p.setLiving(true);
			break;
		case 3:
			tipFinish(p);
			p.setNeedStartTip(true);
			if (liveTimeMap.get(p.getName()) == null) {
				liveTimeMap.put(p.getName(), String.valueOf(System.currentTimeMillis() / 1000 - p.liveStartTimeStamp));
				p.liveStartTimeStamp = 0;
			} else {
				long time = Long.parseLong(liveTimeMap.get(p.getName()));
				time += System.currentTimeMillis() / 1000 - p.liveStartTimeStamp;
				liveTimeMap.put(p.getName(), String.valueOf(time));
			}
			saveConfig();
			// 勿添加break;
		case 4:
			p.setLiving(false);
			break;
		}
	}

	private void tipStart(LivePerson p) {

		switch (p.getName()) {
		case "台长":
			// Autoreply.sendMessage(859561731L, 0, "想看台混矫正器");
			break;
		}
		// try {
		// if (p.autoTip) {
		// Autoreply.instence.naiManager.sendDanmaku(p.roomId,
		// Autoreply.instence.naiManager.cookieSunny, "发发发");
		// Autoreply.instence.naiManager.sendDanmaku(p.roomId,
		// Autoreply.instence.naiManager.cookieLuna, "发发发");
		// Autoreply.instence.naiManager.sendDanmaku(p.roomId,
		// Autoreply.instence.naiManager.cookieStar, "发发发");
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		Autoreply.sendMessage(0, 2856986197L, p.getName() + "开始直播");
	}

	private void tipFinish(LivePerson p) {
		switch (p.getName()) {
		case "台长":
			// Autoreply.sendMessage(859561731L, 0, "呜呜呜");
			break;
		}
		// try {
		// if (p.autoTip) {
		// Autoreply.instence.naiManager.sendDanmaku(p.roomId,
		// Autoreply.instence.naiManager.cookieSunny, "呜呜呜");
		// Autoreply.instence.naiManager.sendDanmaku(p.roomId,
		// Autoreply.instence.naiManager.cookieLuna, "呜呜呜");
		// Autoreply.instence.naiManager.sendDanmaku(p.roomId,
		// Autoreply.instence.naiManager.cookieStar, "呜呜呜");
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		Autoreply.sendMessage(0, 2856986197L, p.getName() + "直播结束");
	}

	public int getliveCount() {
		return liveData.size();
	}

	public void saveConfig() {
		try {
			FileOutputStream fos = null;
			OutputStreamWriter writer = null;
			File file = new File(Autoreply.appDirectory + "liveTime.json");
			fos = new FileOutputStream(file);
			writer = new OutputStreamWriter(fos, "utf-8");
			writer.write(new Gson().toJson(liveTimeMap));
			writer.flush();
			if (fos != null) {
				fos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
