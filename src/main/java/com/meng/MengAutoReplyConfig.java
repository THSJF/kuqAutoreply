package com.meng;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class MengAutoReplyConfig {

	public HashMap<Integer, Long> mapGroupNotReply = new HashMap<Integer, Long>();
	public HashMap<Integer, Long> mapQQNotReply = new HashMap<Integer, Long>();
	public HashMap<Integer, String> mapWordNotReply = new HashMap<Integer, String>();
	public HashMap<Integer, Long> mapGroupRecorder = new HashMap<Integer, Long>();
	public HashMap<Integer, Long> mapGroupDicReply = new HashMap<Integer, Long>();
	public HashMap<String, String> mapLiveTip = new HashMap<String, String>();
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
			int k = 0;
			for (; k < arraySize; k++) {
				switch ((String) entry.getKey()) {
				case "mapGroupNotReply":
					mapGroupNotReply.put(k, Methods.parseLong(array.get(k).toString()));
					break;
				case "mapQQNotReply":
					mapQQNotReply.put(k, Methods.parseLong(array.get(k).toString()));
					break;
				case "mapGroupRecorder":
					mapGroupRecorder.put(k, Methods.parseLong(array.get(k).toString()));
					break;
				case "mapGroupDicReply":
					mapGroupDicReply.put(k, Methods.parseLong(array.get(k).toString()));
					break;
				case "mapWordNotReply":
					mapWordNotReply.put(k, Methods.removeCharAtStartAndEnd(array.get(k).toString()));
					break;
				case "mapLiveTip":
					try {
						mapLiveTip.put(Methods.removeCharAtStartAndEnd(array.get(2 * k).toString()),
								Methods.removeCharAtStartAndEnd(array.get(2 * k + 1).toString()));
					} catch (Exception e) {
						Autoreply.sendPrivateMessage(2856986197L, e.toString());
					}
					break;
				}
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

	public HashMap<Integer, Long> getMapGroupRecorder() {
		return mapGroupRecorder;
	}

	public HashMap<Integer, Long> getMapGroupDicReply() {
		return mapGroupDicReply;
	}

	public HashMap<String, String> getMapLiveTip() {
		return mapLiveTip;
	}

}
