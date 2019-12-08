package com.meng.groupChat;

import com.google.gson.*;
import com.google.gson.reflect.*;
import com.meng.*;
import com.meng.tools.*;
import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.util.*;
import java.util.regex.*;

public class DicReplyManager {

    private HashMap<Long, DicReplyGroup> groupMap = new HashMap<>();
    private HashMap<String, HashSet<String>> dic = new HashMap<>();

    public DicReplyManager() {
        File dicFile = new File(Autoreply.appDirectory + "dic\\dic.json");
        if (!dicFile.exists()) {
            saveDic(dicFile, dic);
        }
        Type type = new TypeToken<HashMap<String, HashSet<String>>>() {
        }.getType();
        dic = new Gson().fromJson(Methods.readFileToString(dicFile.getAbsolutePath()), type);
    }

    public void clear() {
        groupMap.clear();
    }
	
	public void addData(long group) {
        groupMap.put(group, new DicReplyGroup(group));
    }

    public boolean check(long group, long qq, String msg) {
        // 查找公用词库（完全相同才会触发）
        if (checkPublicDic(group, qq, msg)) {
            return true;
        }
        // 查找群专用词库（触发条件为匹配正则表达式）
        return groupMap.get(group).checkMsg(group, qq, msg);
    }

    private boolean checkPublicDic(long group, long qq, String msg) {
        for (String key : dic.keySet()) {
            if (key.equals(msg)) {
                Autoreply.sendMessage(group, qq, (String) dic.get(key).toArray()[Autoreply.instence.random.nextInt(dic.get(key).size())]);
                return true;
            }
        }
        return false;
    }

    static void saveDic(File dicFile, HashMap<String, HashSet<String>> dic) {
        try {
            FileOutputStream fos = new FileOutputStream(dicFile);
            OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            writer.write(new Gson().toJson(dic));
            writer.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	class DicReplyGroup {

		public long groupNum;
		private HashMap<String, HashSet<String>> dic = new HashMap<>();

		public DicReplyGroup(long group) {
			groupNum = group;
			File dicFile = new File(Autoreply.appDirectory + "dic\\dic" + group + ".json");
			if (!dicFile.exists()) {
				DicReplyManager.saveDic(dicFile, dic);
			}
			Type type = new TypeToken<HashMap<String, HashSet<String>>>() {
			}.getType();
			dic = Autoreply.gson.fromJson(Methods.readFileToString(dicFile.getAbsolutePath()), type);
		}

		public boolean checkMsg(long group, long qq, String msg) {
			if (group != groupNum) {
				return false;
			}
			for (String key : dic.keySet()) {
				if (Pattern.matches(".*" + key + ".*", msg.replace(" ", "").trim())) {
					Autoreply.sendMessage(group, qq, (String) dic.get(key).toArray()[Autoreply.instence.random.nextInt(dic.get(key).size())]);
					return true;
				}
			}
			return false;
		}

	}
}
