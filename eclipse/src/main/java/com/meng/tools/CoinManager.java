package com.meng.tools;

import com.google.gson.*;
import com.google.gson.reflect.*;
import com.meng.*;
import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.util.*;

public class CoinManager {
	private HashMap<String, Integer> countMap = new HashMap<>();
	private File file;

	public CoinManager() {
		file = new File(Autoreply.appDirectory + "properties\\coins.json");
		if (!file.exists()) {
			saveData();
		}
		Type type = new TypeToken<HashMap<String, Integer>>() {
		}.getType();
		countMap = new Gson().fromJson(Methods.readFileToString(file.getAbsolutePath()), type);
		Autoreply.instence.threadPool.execute(new Runnable() {
				@Override
				public void run() {
					backupData();
				}
			});
	}

	public void addCoins(long fromQQ, int coins){
		if (countMap.get(String.valueOf(fromQQ)) != null){
			int qqCoin=countMap.get(String.valueOf(fromQQ));
			qqCoin += coins;
			countMap.put(String.valueOf(fromQQ), qqCoin);
		}else{
			countMap.put(String.valueOf(fromQQ), coins);
		}
		saveData();
	}

	public boolean subCoins(long fromQQ, int coins) {
		if (countMap.get(String.valueOf(fromQQ)) != null){
			int qqCoin=countMap.get(String.valueOf(fromQQ));
			if (qqCoin < coins){
				return false;
			}
			qqCoin -= coins;
			countMap.put(String.valueOf(fromQQ), qqCoin);
			saveData();
			return true;
		}
		return false;
	}

	public int getCoinsCount(long fromQQ){
		if (countMap.get(String.valueOf(fromQQ)) == null){
			return 0;
		}
		return countMap.get(String.valueOf(fromQQ));
	}

	public void exchangeCoins(long fromGroup, long fromQQ, int coins){
		if (subCoins(fromQQ, coins)){
			Autoreply.sendMessage(1023432971L, 0, "~addcoins " + coins + " " + fromQQ);
			Autoreply.sendMessage(fromGroup, 0, "兑换" + coins + "个幻币至小律影");
		}else{
			Autoreply.sendMessage(fromGroup, 0, "兑换失败");
		}
	}

	private void saveData() {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
			writer.write(new Gson().toJson(countMap));
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
				writer.write(new Gson().toJson(countMap));
				writer.flush();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
