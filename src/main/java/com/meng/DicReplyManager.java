package com.meng;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sobte.cqp.jcq.message.CQCode;

public class DicReplyManager {

	private HashMap<Integer, DicReplyGroup> groupMap = new HashMap<Integer, DicReplyGroup>();
	private HashMap<Integer, String> replyPool = new HashMap<Integer, String>();
	private int mapFlag = 0;

	private JsonParser parser;
	private JsonObject obj;
	private Iterator it;
	private String filePath;

	public DicReplyManager(String filePath) {
		parser = new JsonParser();
		this.filePath = filePath;
	}

	public void addData(DicReplyGroup drp) {
		groupMap.put(mapFlag, drp);
		mapFlag++;
	}

	public boolean check(long group, long qq, String msg,CQCode CC) throws IOException {
		if (group == 210341365L && msg.indexOf("迫害") != -1) {
			Autoreply.sendGroupMessage(group,CC.at(qq)+"你迫害你"+CC.emoji(128052)+"呢");
			return false;
		}
		boolean b = false;
		b = b | checkPublicDic(group, qq, msg);
		if (b) {
			return true;
		}
		for (int i = 0; i < mapFlag; i++) {
			b = b | groupMap.get(i).checkMsg(group, qq, msg);
		}
		return b;
	}

	private boolean checkPublicDic(long group, long qq, String msg) throws IOException {
		obj = parser.parse(MainSwitch.readToString(filePath)).getAsJsonObject();
		it = obj.entrySet().iterator();
		while (it.hasNext()) {
			Entry entry = (Entry) it.next();
			if (((String) entry.getKey()).equalsIgnoreCase(msg.replace(" ", "").replace("\n", "").trim())) {
				JsonArray array = (JsonArray) entry.getValue();
				int arraySize = array.size();
				if (arraySize != 0) {
					int k = 0;
					for (; k < arraySize; k++) {
						String string = MainSwitch.removeCharAt(array.get(k).toString(), 0);
						replyPool.put(k, MainSwitch.removeCharAt(string, string.length() - 1));
					}
					Autoreply.sendGroupMessage(group, qq, replyPool.get(Autoreply.random.nextInt(k)));
					replyPool.clear();
					return true;
				}
			}

		}
		return false;
	}

}
