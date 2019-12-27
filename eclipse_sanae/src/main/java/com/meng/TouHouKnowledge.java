package com.meng;

import com.meng.gameData.TouHou.*;
import java.util.*;

public class TouHouKnowledge {
	public static TouHouKnowledge ins;
	public HashMap<Long,Boolean> userMap=new HashMap<>();
	public HashMap<String,Boolean> question=new HashMap<>();

	public TouHouKnowledge() {
		question.put("紫奥义「弹幕结界」自机无敌时符卡会暂停", true);
		question.put("「纯粹的疯狂」是角随固", true);
		question.put("「无双风神」是东方风神录HL难度下的符卡", false);
	}

	public boolean check(long fromGroup, long fromQQ, String msg) {
		if (msg.equals("-车万问答")) {
			String[] keys = question.keySet().toArray(new String[question.size()]);
			String randomKey = keys[Autoreply.ins.random.nextInt(keys.length)];
			userMap.put(fromQQ, question.get(randomKey));
			Autoreply.sendMessage(fromGroup, 0, randomKey);
			return true;
		} else if (userMap.get(fromQQ) != null) {
			if (msg.equalsIgnoreCase("-问答回答 t")) {
				Autoreply.sendMessage(fromGroup, fromQQ, userMap.get(fromQQ) ? "回答正确": "回答错误");
				userMap.remove(fromQQ);
			} else if (msg.equalsIgnoreCase("-问答回答 f")) {
				Autoreply.sendMessage(fromGroup, fromQQ, userMap.get(fromQQ) ? "回答错误": "回答正确");
				userMap.remove(fromQQ);
			}
			return true;
		}
		return false;
	}
}
