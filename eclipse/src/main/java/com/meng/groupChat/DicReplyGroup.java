package com.meng.groupChat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.meng.Autoreply;
import com.meng.Methods;

public class DicReplyGroup {

	private JsonParser parser;
	private JsonObject obj;
	@SuppressWarnings("rawtypes")
	private Iterator it;
	public long groupNum;
	private String jsonString;
	private HashMap<Integer, String> replyPool = new HashMap<Integer, String>();// 存放所有可能出现的回答

	public DicReplyGroup(long group) {
		groupNum = group;
		parser = new JsonParser();
		try {
			File dicFile = new File(Autoreply.appDirectory + "dic\\dic" + group + ".json");
			if (dicFile.exists()) {
				jsonString = Methods.readFileToString(dicFile.getAbsolutePath());
			} else {
				dicFile.createNewFile();
				try {
					FileOutputStream fos = null;
					OutputStreamWriter writer = null;
					fos = new FileOutputStream(dicFile);
					writer = new OutputStreamWriter(fos, "utf-8");
					writer.write("{}");
					writer.flush();
					if (fos != null) {
						fos.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				jsonString = "{}";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	public boolean checkMsg(long group, long qq, String msg) {
		if (group == groupNum) {
			try {
				obj = parser.parse(jsonString).getAsJsonObject();// 谷歌的GSON对象
				it = obj.entrySet().iterator();
				while (it.hasNext()) {// 遍历集合
					Entry entry = (Entry) it.next();
					if (Pattern.matches(".*" + (String) entry.getKey() + ".*", msg.replace(" ", "").trim())) { // 使用了正则表达式查找要进行的回复
						JsonArray array = (JsonArray) entry.getValue(); // 根据词库特点，一个key对应一个数组
						int arraySize = array.size();
						if (arraySize != 0) {
							int k = 0;
							for (; k < arraySize; k++) {
								// 读取出来的数据是带有引号的
								// 将引号去掉并将对象放入Hashmap中
								replyPool.put(k, Methods.removeCharAtStartAndEnd(array.get(k).toString()));
							}
							// 从所有的回答中随机选择一个
							Autoreply.sendMessage(group, qq,
									replyPool.get(Autoreply.instence.random.nextInt(2147483647) % k));
							replyPool.clear();
							return true;
						}
					}
				}
			} catch (Exception e) {
			}
		}
		return false;
	}

}
