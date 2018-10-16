package com.meng;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DicReplyManager {

	private HashMap<Integer, DicReplyGroup> groupMap = new HashMap<Integer, DicReplyGroup>();
	private HashMap<Integer, String> replyPool = new HashMap<Integer, String>();
	private int mapFlag = 0;
	private JsonParser parser;
	private JsonObject obj;
	@SuppressWarnings("rawtypes")
	private Iterator it;

	public DicReplyManager() {
		parser = new JsonParser();
	}

	public void addData(DicReplyGroup drp) {
		groupMap.put(mapFlag, drp);
		mapFlag++;
	}

	public boolean check(long group, long qq, String msg) throws IOException {
		boolean b = false;
		// 查找公用词库（完全相同才会触发）
		b = b | checkPublicDic(group, qq, msg);
		if (b) {
			return true;
		}
		// 查找群专用词库（触发条件为匹配正则表达式）
		for (int i = 0; i < mapFlag; i++) {
			b = b | groupMap.get(i).checkMsg(group, qq, msg);
		}
		return b;
	}

	@SuppressWarnings("rawtypes")
	private boolean checkPublicDic(long group, long qq, String msg) throws IOException {
		obj = parser.parse(Methods.readToString(Autoreply.appDirectory + "dic.json")).getAsJsonObject(); // 谷歌的GSON对象
		it = obj.entrySet().iterator();
		while (it.hasNext()) {// 遍历集合查找符合要求的key
			Entry entry = (Entry) it.next();
			if (((String) entry.getKey()).equalsIgnoreCase(msg.replace(" ", "").trim())) {
				JsonArray array = (JsonArray) entry.getValue();
				int arraySize = array.size();
				if (arraySize != 0) {
					int k = 0;
					for (; k < arraySize; k++) {
						// 读取出来的数据是带有引号的
						// 将引号去掉并将对象放入Hashmap中
						replyPool.put(k, Methods.removeCharAtStartAndEnd(array.get(k).toString()));
					}
					// 从所有的回答中随机选择一个
					Autoreply.sendGroupMessage(group, qq, replyPool.get(Autoreply.random.nextInt(k)));
					replyPool.clear();
					return true;
				}
			}

		}
		return false;
	}

}
