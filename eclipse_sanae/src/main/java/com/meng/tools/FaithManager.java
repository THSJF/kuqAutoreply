package com.meng.tools;

import com.google.gson.reflect.*;
import com.meng.*;
import com.meng.tools.override.*;
import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.util.*;
import com.meng.config.*;

public class FaithManager {
	public static FaithManager ins;
	private HashMap<Long, Integer> faithMap = new HashMap<>();
	private MyLinkedHashMap<String,String> store=new MyLinkedHashMap<>();
	private File file;

	public FaithManager() {
		file = new File(Autoreply.appDirectory + "properties\\faith.json");
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
		store.put("点赞", "每天上午会为你点赞");
	}
	public boolean check(long fromGroup, long fromQQ, String msg) {
		if (msg.equals("-信仰商店")) {
			Autoreply.sendMessage(fromGroup, 0, store.toString());
			return true;
		}
		if (msg.startsWith("-信仰商店 兑换")) {
			String goods=msg.substring(9);
			int fun=0;
			switch (goods) {
				case "点赞":
					fun = FaithUser.zan;
					break;
			}
			ConfigManager.ins.addFunctionUse(fromQQ, fun);
			ConfigManager.ins.SanaeConfig.zanSet.add(fromQQ);
			ConfigManager.ins.saveSanaeConfig();
			Autoreply.sendMessage(fromGroup, 0, "成功兑换" + goods);
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

	public int getFaithCount(long fromQQ) {
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
