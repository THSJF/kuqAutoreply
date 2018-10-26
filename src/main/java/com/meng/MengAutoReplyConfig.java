package com.meng;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.meng.bilibili.BiliUp;

public class MengAutoReplyConfig {

	private HashMap<Integer, Long> mapGroupNotReply = new HashMap<Integer, Long>();
	private HashMap<Integer, Long> mapQQNotReply = new HashMap<Integer, Long>();
	private HashMap<Integer, String> mapWordNotReply = new HashMap<Integer, String>();
	private HashMap<Integer, String> mapGroupRecorder = new HashMap<Integer, String>();
	private HashMap<Integer, Long> mapGroupDicReply = new HashMap<Integer, Long>();
	private HashMap<String, String> mapLiveTip = new HashMap<String, String>();
	private HashMap<Integer, BiliUp> mapBiliUp = new HashMap<Integer, BiliUp>();

	private JsonParser parser;

	public MengAutoReplyConfig() {
		parser = new JsonParser();
		try {
			load();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean load() {
		JsonObject obj = null;
		try {
			obj = parser.parse(Methods.readFileToString(Autoreply.appDirectory + "config.json")).getAsJsonObject();
		} catch (JsonSyntaxException | IOException e) {
			Autoreply.sendPrivateMessage(2856986197L, e.toString());
		} // 谷歌的GSON对象
		Iterator it = obj.entrySet().iterator();
		while (it.hasNext()) {// 遍历
			Entry entry = (Entry) it.next();
			JsonArray array = (JsonArray) entry.getValue();
			int arraySize = array.size();
			switch ((String) entry.getKey()) {
			case "mapGroupNotReply":
				for (int k = 0; k < arraySize; k++) {
					mapGroupNotReply.put(k, Long.parseLong(Methods.removeCharAtStartAndEnd(array.get(k).toString())));
				}
				break;
			case "mapQQNotReply":
				for (int k = 0; k < arraySize; k++) {
					mapQQNotReply.put(k, Long.parseLong(Methods.removeCharAtStartAndEnd(array.get(k).toString())));
				}
				break;
			case "mapGroupRecorder":
				for (int k = 0; k < arraySize; k++) {
					mapGroupRecorder.put(k, Methods.removeCharAtStartAndEnd(array.get(k).toString()));
				}
				break;
			case "mapGroupDicReply":
				for (int k = 0; k < arraySize; k++) {
					mapGroupDicReply.put(k, Long.parseLong(Methods.removeCharAtStartAndEnd(array.get(k).toString())));
				}
				break;
			case "mapWordNotReply":
				for (int k = 0; k < arraySize; k++) {
					mapWordNotReply.put(k, Methods.removeCharAtStartAndEnd(array.get(k).toString().replace("\"", "")));
				}
				break;
			case "mapLiveTip":
				for (int k = 0; k < arraySize; k += 2) {
					try {
						mapLiveTip.put(Methods.removeCharAtStartAndEnd(array.get(k).toString()),
								Methods.removeCharAtStartAndEnd(array.get(k + 1).toString()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			case "mapBiliUp":
				for (int k = 0; k < arraySize; k += 3) {
					mapBiliUp.put(k,
							new BiliUp(Methods.removeCharAtStartAndEnd(array.get(k).toString()),
									Long.parseLong(Methods.removeCharAtStartAndEnd(array.get(k + 1).toString())),
									Long.parseLong(Methods.removeCharAtStartAndEnd(array.get(k + 2).toString()))));
				}
				break;
			}

		}
		return false;
	}

	public HashMap<Integer, Long> getMapGroupNotReply() {
		return mapGroupNotReply;
	}

	public HashMap<Integer, Long> getMapQQNotReply() {
		return mapQQNotReply;
	}

	public HashMap<Integer, String> getMapWordNotReply() {
		return mapWordNotReply;
	}

	public HashMap<Integer, String> getMapGroupRecorder() {
		return mapGroupRecorder;
	}

	public HashMap<Integer, Long> getMapGroupDicReply() {
		return mapGroupDicReply;
	}

	public HashMap<String, String> getMapLiveTip() {
		return mapLiveTip;
	}

	public HashMap<Integer, BiliUp> getMapBiliUp() {
		return mapBiliUp;
	}
}
