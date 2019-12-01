package com.meng.tools;

import com.google.gson.reflect.*;
import com.meng.*;
import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.util.*;

public class CoinManager {
	private HashMap<Long, Integer> coinsMap = new HashMap<>();
	private File file;

	public CoinManager() {
		file = new File(Autoreply.appDirectory + "properties\\coins.json");
		if (!file.exists()) {
			saveData();
		}
		Type type = new TypeToken<HashMap<Long, Integer>>() {
		}.getType();
		coinsMap = Autoreply.gson.fromJson(Methods.readFileToString(file.getAbsolutePath()), type);
		Autoreply.instence.threadPool.execute(new Runnable() {
				@Override
				public void run() {
					backupData();
				}
			});
	}

	public void addCoins(long fromQQ, int coins) {
		if (coins < 0) {
			return;
		}
		if (coinsMap.get(fromQQ) != null) {
			int qqCoin=coinsMap.get(fromQQ);
			qqCoin += coins;
			coinsMap.put(fromQQ, qqCoin);
		} else {
			coinsMap.put(fromQQ, coins);
		}
		saveData();
	}

	public boolean subCoins(long fromQQ, int coins) {
		if (coins < 0) {
			return false;
		}
		if (coinsMap.get(fromQQ) != null) {
			int qqCoin=coinsMap.get(fromQQ);
			if (qqCoin < coins) {
				return false;
			}
			qqCoin -= coins;
			coinsMap.put(fromQQ, qqCoin);
			saveData();
			return true;
		}
		return false;
	}

	public int getCoinsCount(long fromQQ) {
		if (coinsMap.get(fromQQ) == null) {
			return 0;
		}
		return coinsMap.get(fromQQ);
	}
	
	private void saveData() {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
			writer.write(Autoreply.gson.toJson(coinsMap));
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
				writer.write(Autoreply.gson.toJson(coinsMap));
				writer.flush();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
