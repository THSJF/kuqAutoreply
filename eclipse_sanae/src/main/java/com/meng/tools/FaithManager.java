package com.meng.tools;

import com.google.gson.reflect.*;
import com.meng.*;
import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.util.*;

public class FaithManager {
	private HashMap<Long, Integer> faithMap = new HashMap<>();
	private File file;

	public FaithManager() {
		file = new File(Autoreply.appDirectory + "properties\\faith.json");
		if (!file.exists()) {
			saveData();
		}
		Type type = new TypeToken<HashMap<Long, Integer>>() {
		}.getType();
		faithMap = Autoreply.gson.fromJson(Tools.FileTool.readString(file.getAbsolutePath()), type);
		Autoreply.instence.threadPool.execute(new Runnable() {
				@Override
				public void run() {
					backupData();
				}
			});
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
