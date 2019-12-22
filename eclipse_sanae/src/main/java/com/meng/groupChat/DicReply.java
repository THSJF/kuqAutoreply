package com.meng.groupChat;

import com.google.gson.reflect.*;
import com.meng.*;
import com.meng.tools.*;
import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.util.*;
import java.util.regex.*;

public class DicReply {

    private HashMap<Long, DicReplyGroup> groupMap = new HashMap<>();
	private HashMap<String, ArrayList<String>> dic = new HashMap<>();

	public DicReply() {
		File dicFile = new File(Autoreply.appDirectory + "dic\\dic.json");
		if (!dicFile.exists()) {
			saveDic(dicFile, dic);
		}
		Type type = new TypeToken<HashMap<String, HashSet<String>>>() {
		}.getType();
		dic = Autoreply.gson.fromJson(Tools.FileTool.readString(dicFile), type);
	}

	public void clear() {
		groupMap.clear();
	}

	public void addDic(long group) {
		groupMap.put(group, new DicReplyGroup(group));
	}

	public boolean check(long group, long qq, String msg) {
		if (checkPublicDic(group, qq, msg)) {
			return true;
		}
		return groupMap.get(group).checkMsg(group, qq, msg);
	}

	private boolean checkPublicDic(long group, long qq, String msg) {
		for (String key : dic.keySet()) {
			if (Pattern.matches(".*" + key + ".*", msg.replaceAll("\\s", "").trim())) {
				ArrayList<String> ans=dic.get(key);
				Autoreply.sendMessage(group, qq, ans.get(Autoreply.instence.random.nextInt(ans.size())));
				return true;
			}
		}
		return false;
	}

	private class DicReplyGroup {
		private long groupNum;
		private HashMap<String, ArrayList<String>> dic = new HashMap<>();
		public DicReplyGroup(long group) {
			groupNum = group;
			File dicFile = new File(Autoreply.appDirectory + "dic\\dic" + group + ".json");
			if (!dicFile.exists()) {
				saveDic(dicFile, dic);
			}
			Type type = new TypeToken<HashMap<String, ArrayList<String>>>() {
			}.getType();
			dic = Autoreply.gson.fromJson(Tools.FileTool.readString(dicFile), type);
		}
		public boolean checkMsg(long group, long qq, String msg) {
			for (String key : dic.keySet()) {
				if (Pattern.matches(".*" + key + ".*", msg.replace(" ", "").trim())) {
					ArrayList<String> ans=dic.get(key);
					Autoreply.sendMessage(group, qq, ans.get(Autoreply.instence.random.nextInt(ans.size())));
					return true;
				}
			}
			return false;
		}
	}

	private void saveDic(File dicFile, HashMap<String, ArrayList<String>> dic) {
		try {
			FileOutputStream fos = new FileOutputStream(dicFile);
			OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
			writer.write(Autoreply.gson.toJson(dic));
			writer.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



}
