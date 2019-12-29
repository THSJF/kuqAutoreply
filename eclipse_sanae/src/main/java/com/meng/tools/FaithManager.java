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
		store.put("-关注b站 用户uid", "用户更新时和直播时会在这个群中提示(用户/1信仰/天)");
		store.put("-赞我", "QQ名片赞10次(2信仰/10次)");
	}
	public boolean check(long fromGroup, long fromQQ, String msg) {
		if (msg.equals("-信仰商店")) {
			Autoreply.sendMessage(fromGroup, 0, store.toString());
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
