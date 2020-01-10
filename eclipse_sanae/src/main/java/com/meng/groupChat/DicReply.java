package com.meng.groupChat;

import com.google.gson.reflect.*;
import com.meng.*;
import com.meng.config.*;
import com.meng.tools.*;
import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.util.*;
import java.util.regex.*;

public class DicReply {
	public static DicReply ins;
    private HashMap<Long, DicReplyGroup> groupMap = new HashMap<>();
	private HashMap<String, ArrayList<String>> dic = new HashMap<>();
	private boolean regexMode = false;
	public DicReply() {
		File dicFile = new File(Autoreply.appDirectory + "dic\\dic.json");
		if (!dicFile.exists()) {
			saveDic(dicFile, dic);
		}
		Type type = new TypeToken<HashMap<String, HashSet<String>>>() {
		}.getType();
		dic = Autoreply.gson.fromJson(Tools.FileTool.readString(dicFile), type);
		try {
			if (ConfigManager.ins.SanaeConfig.dicRegex.get(-1) == null) {
				regexMode = false;
			} else {
				regexMode = ConfigManager.ins.SanaeConfig.dicRegex.get(-1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setRegexMode(long fromGroup, boolean isReg) {
		ConfigManager.ins.SanaeConfig.dicRegex.put(fromGroup, isReg);
		groupMap.get(fromGroup).regexMode = isReg;
		ConfigManager.ins.saveSanaeConfig();
	}

	public void addKV(long group, String k, String... v) {
		DicReplyGroup drg = groupMap.get(group);	
		ArrayList<String> ans = drg.dic.get(k);
		if (ans == null) {
			ans = new ArrayList<>();
			drg.dic.put(k, ans);
		}
		for (String s:v) {
			ans.add(s);
		}
		saveDic(new File(Autoreply.appDirectory + "dic\\dic" + drg.groupNum + ".json"), drg.dic);
	}

	public void removeK(long group, String k) {
		DicReplyGroup drg = groupMap.get(group);
		if (drg == null) {
			return;
		}
		ArrayList<String> ans = drg.dic.get(k);
		if (ans == null) {
			return;
		}
		drg.dic.remove(k);
		saveDic(new File(Autoreply.appDirectory + "dic\\dic" + drg.groupNum + ".json"), drg.dic);
	}

	public void clear() {
		groupMap.clear();
	}

	public void addReply(long group) {
		groupMap.put(group, new DicReplyGroup(group));
	}

	public boolean check(long group, long qq, String msg) {
		if (checkPublicDic(group, qq, msg)) {
			return true;
		}
		DicReplyGroup drg=groupMap.get(group);
		if (drg == null) {
			drg = new DicReplyGroup(group);
			groupMap.put(group, drg);
		}
		return drg.checkMsg(group, qq, msg);
	}

	private boolean checkPublicDic(long group, long qq, String msg) {
		if (regexMode) {
			for (String key : dic.keySet()) {
				if (Pattern.matches(".*" + key + ".*", msg.replaceAll("\\s", ""))) {
					Autoreply.sendMessage(group, qq, dic.get(key));
					return true;
				}
			}
		} else {
			for (String key : dic.keySet()) {
				if (msg.contains(key.replaceAll("\\s", ""))) {
					Autoreply.sendMessage(group, qq, dic.get(key));
					return true;
				}
			}
		}
		return false;
	}

	private class DicReplyGroup {
		public long groupNum;
		public HashMap<String, ArrayList<String>> dic = new HashMap<>();
		public boolean regexMode=false;
		public DicReplyGroup(long group) {
			groupNum = group;
			File dicFile = new File(Autoreply.appDirectory + "dic\\dic" + group + ".json");
			if (!dicFile.exists()) {
				saveDic(dicFile, dic);
			}
			Type type = new TypeToken<HashMap<String, ArrayList<String>>>() {
			}.getType();
			dic = Autoreply.gson.fromJson(Tools.FileTool.readString(dicFile), type);
			if (ConfigManager.ins.SanaeConfig.dicRegex.get(groupNum) == null) {
				regexMode = false;
			} else {
				regexMode = ConfigManager.ins.SanaeConfig.dicRegex.get(groupNum);
			}
		}
		public boolean checkMsg(long group, long qq, String msg) {
			if (regexMode) {
				for (String key : dic.keySet()) {
					if (Pattern.matches(".*" + key + ".*", msg.replace(" ", "").trim())) {
						ArrayList<String> ans = dic.get(key);
						Autoreply.sendMessage(group, qq, ans.get(Autoreply.ins.random.nextInt(ans.size())));
						return true;
					}
				}
			} else {
				for (String key : dic.keySet()) {
					if (msg.contains(key.replaceAll("\\s", ""))) {
						ArrayList<String> ans = dic.get(key);
						Autoreply.sendMessage(group, qq, ans.get(Autoreply.ins.random.nextInt(ans.size())));
						return true;
					}
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
