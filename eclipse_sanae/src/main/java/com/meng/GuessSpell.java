package com.meng;
import java.util.*;
import com.meng.gameData.TouHou.*;

public class GuessSpell {
	public static GuessSpell ins;
	public HashMap<Long,String> userMap=new HashMap<>();
	public HashMap<String,String> spellDescribe=new HashMap<>();

	public GuessSpell() {
		spellDescribe.put("紫奥义", "自机无敌时符卡会暂停");
		spellDescribe.put("战栗的寒冷之星", "简单(?)的左右左右");
	}

	public boolean check(long fromGroup, long fromQQ, String msg) {
		if (msg.equals("-猜符卡")) {
			String[] keys = spellDescribe.keySet().toArray(new String[spellDescribe.size()]);
			String randomKey=  keys[Autoreply.ins.random.nextInt(keys.length)];
			String randomValue = spellDescribe.get(randomKey);
			userMap.put(fromQQ, TouHouDataManager.ins.getSpellCard(randomKey).n);
			Autoreply.sendMessage(fromGroup, 0, randomValue);
			return true;
		} else if (msg.startsWith("-猜符卡回答 ")) {
			if (userMap.get(fromQQ) != null) {
				String result=TouHouDataManager.ins.getSpellCard(msg.substring(7)).n;
				if (userMap.get(fromQQ).equals(result)) {
					Autoreply.sendMessage(fromGroup, fromQQ, "回答正确\n" + result);
				} else {
					Autoreply.sendMessage(fromGroup, fromQQ, "回答错误\n" + result);
				}
				userMap.remove(fromQQ);
			}
			return true;
		}
		return false;
	}
}
