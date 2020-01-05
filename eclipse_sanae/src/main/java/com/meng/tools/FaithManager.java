package com.meng.tools;

import com.google.gson.*;
import com.google.gson.reflect.*;
import com.meng.*;
import com.meng.bilibili.*;
import com.meng.bilibili.main.*;
import com.meng.config.*;
import com.meng.tools.override.*;
import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.util.*;

public class FaithManager {
	public static FaithManager ins;
	private HashMap<Long, Integer> faithMap = new HashMap<>();
	private MyLinkedHashMap<String,String> store=new MyLinkedHashMap<>();
	private File file;

	public FaithManager() {
		file = new File(Autoreply.ins.appDirectory + "properties\\faith.json");
		if (!file.exists()) {
			saveData();
		}
		Type type = new TypeToken<HashMap<Long, Integer>>() {
		}.getType();
		faithMap = Autoreply.gson.fromJson(Tools.FileTool.readString(file.getAbsolutePath()), type);
		Autoreply.ins.threadPool.execute(new Runnable() {
				@Override
				public void run() {
					backupData();
				}
			});
		store.put("-关注b站 用户uid", "用户更新时和直播时会在这个群中提示(用户/1信仰/天)");
		store.put("-取消关注b站 用户uid", "取消关注");
		store.put("-赞我", "QQ名片赞10次(2信仰/10次)");
	}
	public boolean check(long fromGroup, long fromQQ, String msg) {
		if (msg.equals("-信仰商店")) {
			Autoreply.sendMessage(fromGroup, 0, store.toString());
			return true;
		}
		try {
			if (msg.startsWith("-关注b站 ")) {
				String uid=msg.substring(6);
				String name=new JsonParser().parse(Tools.Network.getSourceCode("https://api.bilibili.com/x/space/acc/info?mid=" + uid + "&jsonp=jsonp")).getAsJsonObject().get("data").getAsJsonObject().get("name").getAsString();
				BiliMaster bm=ConfigManager.ins.SanaeConfig.biliMaster.get(Integer.parseInt(uid));
				if (bm == null) {
					bm = new BiliMaster();
					bm.uid = Integer.parseInt(uid);
					SpaceToLiveJavaBean sjb = Autoreply.gson.fromJson(Tools.Network.getSourceCode("https://api.live.bilibili.com/room/v1/Room/getRoomInfoOld?mid=" + bm.uid), SpaceToLiveJavaBean.class);
					if (sjb.data.roomid == 0) {
						bm.roomID = -1;
					} else {
						bm.roomID = sjb.data.roomid;
					}
					ConfigManager.ins.SanaeConfig.biliMaster.put(bm.uid, bm);
				}
				bm.addFans(fromGroup, fromQQ);
				ConfigManager.ins.saveSanaeConfig();
				Autoreply.sendMessage(fromGroup, 0, String.format("关注%s(uid:%d)成功", name, bm.uid));
				return true;
			}
		} catch (Exception e) {
			Autoreply.sendMessage(fromGroup, 0, e.toString());
		}
		try {
			if (msg.startsWith("-取消关注b站 ")) {
				String uid=msg.substring(8);
				BiliMaster bm=ConfigManager.ins.SanaeConfig.biliMaster.get(Integer.parseInt(uid));
				if (bm == null) {
					Autoreply.sendMessage(fromGroup, 0, "未关注,无需此操作");
					return true;
				}
				String name=new JsonParser().parse(Tools.Network.getSourceCode("https://api.bilibili.com/x/space/acc/info?mid=" + uid + "&jsonp=jsonp")).getAsJsonObject().get("data").getAsJsonObject().get("name").getAsString();
				bm.removeFans(fromGroup, fromQQ);
				ConfigManager.ins.saveSanaeConfig();
				Autoreply.sendMessage(fromGroup, 0, String.format("取消关注%s(uid:%d)成功", name, bm.uid));
				return true;
			}
		} catch (Exception e) {
			Autoreply.sendMessage(fromGroup, 0, e.toString());
		}
		if (msg.equals("-赞我")) {
			if (FaithManager.ins.getFaith(fromQQ) > 1) {
				Autoreply.ins.getCoolQ().sendLike(fromQQ, 10);
				FaithManager.ins.subFaith(fromQQ, 2);
				Autoreply.sendMessage(fromGroup, fromQQ, "点赞完成");
			} else {
				Autoreply.sendMessage(fromGroup, fromQQ, "你的信仰不足以进行此操作");
			}
			return true;
		}
		return false;
	}

	public void addFaith(long fromQQ, int faith) {
		if (faith < 0) {
			return;
		}
		if (faithMap.get(fromQQ) != null) {
			int qqFaith=faithMap.get(fromQQ);
			qqFaith += faith;
			faithMap.put(fromQQ, qqFaith);
		} else {
			faithMap.put(fromQQ, faith);
		}
		saveData();
	}

	public boolean subFaith(long fromQQ, int faith) {
		if (faith < 0) {
			return false;
		}
		if (faithMap.get(fromQQ) != null) {
			int qqFaith=faithMap.get(fromQQ);
			if (qqFaith < faith) {
				return false;
			}
			qqFaith -= faith;
			faithMap.put(fromQQ, qqFaith);
			saveData();
			return true;
		}
		return false;
	}

	public int getFaith(long fromQQ) {
		if (faithMap.get(fromQQ) == null) {
			return 0;
		}
		return faithMap.get(fromQQ);
	}

	private void saveData() {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
			writer.write(Autoreply.gson.toJson(faithMap));
			writer.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void backupData() {
		while (true) {
			try {
				Thread.sleep(86400000);
				File backup = new File(file.getAbsolutePath() + ".bak" + System.currentTimeMillis());
				FileOutputStream fos = new FileOutputStream(backup);
				OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
				writer.write(Autoreply.gson.toJson(faithMap));
				writer.flush();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
