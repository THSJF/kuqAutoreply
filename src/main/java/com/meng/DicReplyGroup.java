package com.meng;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DicReplyGroup {

	private JsonParser parser;
	private JsonObject obj;
	@SuppressWarnings("rawtypes")
	private Iterator it;
	private long groupNum;
	private String filePath;
	private HashMap<Integer, String> replyPool = new HashMap<Integer, String>();

	public DicReplyGroup(long group, String filePath) {
		groupNum = group;
		parser = new JsonParser();
		this.filePath = filePath;
	}

	@SuppressWarnings("rawtypes")
	public boolean checkMsg(long group,long qq, String msg) throws IOException {
		if (group == groupNum) {
			obj = parser.parse(Methods.readToString(filePath)).getAsJsonObject();
			it = obj.entrySet().iterator();
			while (it.hasNext()) {
				Entry entry = (Entry) it.next();
				if (Pattern.matches(".*" + (String) entry.getKey() + ".*", msg.replace(" ", "").trim())) {
					JsonArray array = (JsonArray) entry.getValue();
					int arraySize = array.size();
					if (arraySize != 0) {
						int k = 0;
						for (; k < arraySize; k++) {
							String string = Methods.removeCharAt(array.get(k).toString(), 0);
							replyPool.put(k, Methods.removeCharAt(string, string.length() - 1));
						}
						Autoreply.sendGroupMessage(group,qq, replyPool.get(Autoreply.random.nextInt(k)));
						replyPool.clear();
						return true;
					}
				}
			}
		}
		return false;
	}

}
