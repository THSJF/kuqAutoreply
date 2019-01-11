package com.meng.groupChat;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.meng.Autoreply;
import com.meng.Methods;

public class DicReplyManager {

	private HashMap<Integer, DicReplyGroup> groupMap = new HashMap<Integer, DicReplyGroup>();
	private HashMap<Integer, String> replyPool = new HashMap<Integer, String>();
	private int mapFlag = 0;
	private JsonParser parser;
	private JsonObject obj;
	private String jsonString;
	@SuppressWarnings("rawtypes")
	private Iterator it;

	public DicReplyManager() {
		parser = new JsonParser();
		try {
			jsonString = Methods.readFileToString(Autoreply.appDirectory + "dic.json");
			File propFile = new File(Autoreply.appDirectory + "setu.properties");
			if (!propFile.exists()) {
				propFile.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addData(DicReplyGroup drp) {
		groupMap.put(mapFlag, drp);
		mapFlag++;
	}

	public boolean check(long group, long qq, String msg) {
		boolean b = false;
		if (msg.contains("色图")) {
			Autoreply.useCount.incSetu(qq);
		}
		// 查找公用词库（完全相同才会触发）
		try {
			b = b | checkPublicDic(group, qq, msg);
		} catch (IOException e1) {
			return false;
		}
		if (b) {
			return true;
		}
		// 查找群专用词库（触发条件为匹配正则表达式）
		for (int i = 0; i < mapFlag; i++) {
			try {
				b = b | groupMap.get(i).checkMsg(group, qq, msg);
			} catch (IOException e) {
				return false;
			}
		}
		return b;
	}

	@SuppressWarnings("rawtypes")
	private boolean checkPublicDic(long group, long qq, String msg) throws IOException {
		obj = parser.parse(jsonString).getAsJsonObject(); // 谷歌的GSON对象
		it = obj.entrySet().iterator();
		while (it.hasNext()) {// 遍历集合查找符合要求的key
			Entry entry = (Entry) it.next();
			if (((String) entry.getKey()).equalsIgnoreCase(msg)) {
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
					Autoreply.sendMessage(group, qq, replyPool.get(Autoreply.random.nextInt(2147483647) % k));
					replyPool.clear();
					return true;
				}
			}

		}
		return false;
	}

}
