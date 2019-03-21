package com.meng.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.meng.Autoreply;
import com.meng.Methods;

public class ConfigManager {
	public ConfigJavaBean configJavaBean = new ConfigJavaBean();

	private String jsonString = "";
	private Gson gson = new Gson();

	public ConfigManager() {
		try {
			File jsonFile = new File(Autoreply.appDirectory + "config.json");
			if (!jsonFile.exists()) {
				jsonString = gson.toJson(configJavaBean);
				saveConfig();
			}
			jsonString = Methods.readFileToString(Autoreply.appDirectory + "config.json");
			gson = new Gson();
			configJavaBean = gson.fromJson(jsonString, ConfigJavaBean.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void checkSetConfig(long fromGroup, String msg) {

		if (msg.startsWith(".addgroupreply.")) {
			addGroupReply(msg.replace(".addgroupreply.", ""));
			saveConfig();
			Autoreply.sendMessage(fromGroup, 0, "已启用" + msg.replace(".addgroupreply.", "") + "群回复");
		}
		if (msg.startsWith(".removegroupreply.")) {
			String groupID = msg.replace(".removegroupreply.", "");
			for (int i = 0; i < configJavaBean.mapGroupReply.size(); i++) {
				if (configJavaBean.mapGroupReply.get(i).equals(groupID)) {
					configJavaBean.mapGroupReply.remove(i);
					break;
				}
			}
			saveConfig();
			Autoreply.sendMessage(fromGroup, 0, "已停用" + groupID + "群回复");
		}

		if (msg.startsWith(".blockuser")) {
			long qqId = Autoreply.CC.getAt(msg.replace(".blockuser", ""));
			addQQNotReply(String.valueOf(qqId));
			saveConfig();
			Autoreply.sendMessage(fromGroup, 0, "已将" + qqId + "加入屏蔽列表");
		}
		if (msg.startsWith(".unblockuser")) {
			String qqId = String.valueOf(Autoreply.CC.getAt(msg.replace(".unblockuser", "")));
			for (int i = 0; i < configJavaBean.mapQQNotReply.size(); i++) {
				if (configJavaBean.mapQQNotReply.get(i).equals(qqId)) {
					configJavaBean.mapQQNotReply.remove(i);
					break;
				}
			}
			saveConfig();
			Autoreply.sendMessage(fromGroup, 0, "已将" + qqId + "解除屏蔽");
		}

		if (msg.startsWith(".blockword.")) {
			addQQNotReply(msg.replace(".blockword.", ""));
			saveConfig();
			Autoreply.sendMessage(fromGroup, 0, "已将" + msg.replace(".blockword.", "") + "加入屏蔽列表");
		}
		if (msg.startsWith(".blockword.")) {
			String word = msg.replace(".blockword.", "");
			for (int i = 0; i < configJavaBean.mapQQNotReply.size(); i++) {
				if (configJavaBean.mapQQNotReply.get(i).equals(word)) {
					configJavaBean.mapQQNotReply.remove(i);
					break;
				}
			}
			saveConfig();
			Autoreply.sendMessage(fromGroup, 0, "已将" + word + "解除屏蔽");
		}

		if (msg.startsWith(".addrepeater")) {
			addGroupRepeater(msg.replace(".addrepeater.", ""));
			saveConfig();
			Autoreply.sendMessage(fromGroup, 0, "已启用" + msg.replace(".addrepeater.", "") + "复读机");
		}
		if (msg.startsWith(".removerepeater.")) {
			String qqId = msg.replace(".removerepeater.", "");
			for (int i = 0; i < configJavaBean.mapGroupRepeater.size(); i++) {
				if (configJavaBean.mapGroupRepeater.get(i).equals(qqId)) {
					configJavaBean.mapGroupRepeater.remove(i);
					break;
				}
			}
			saveConfig();
			Autoreply.sendMessage(fromGroup, 0, "已停用" + msg.replace(".removerepeater.", "") + "复读机");
		}

	}

	public ArrayList<String> getMapGroupReply() {
		return configJavaBean.mapGroupReply;
	}

	public ArrayList<String> getMapQQNotReply() {
		return configJavaBean.mapQQNotReply;
	}

	public ArrayList<String> getMapWordNotReply() {
		return configJavaBean.mapWordNotReply;
	}

	public ArrayList<String> getMapGroupRepeater() {
		return configJavaBean.mapGroupRepeater;
	}

	public ArrayList<String> getMapGroupDicReply() {
		return configJavaBean.mapGroupDicReply;
	}

	public ArrayList<ConfigJavaBean.BiliUser> getMapBiliUp() {
		return configJavaBean.mapBiliUser;
	}

	public void addGroupReply(String groupNumber) {
		configJavaBean.mapGroupReply.add(groupNumber);
	}

	public void addQQNotReply(String QQnumber) {
		configJavaBean.mapQQNotReply.add(QQnumber);
	}

	public void addWordNotReply(String content) {
		configJavaBean.mapWordNotReply.add(content);
	}

	public void addGroupRepeater(String QQnumber) {
		configJavaBean.mapGroupRepeater.add(QQnumber);
	}

	public void addBilibiliUser(String name, String qq, String bid) {
		ConfigJavaBean.BiliUser cb = configJavaBean.new BiliUser(name, qq, bid);
		configJavaBean.mapBiliUser.add(cb);
	}

	public void saveConfig() {
		try {
			jsonString = gson.toJson(configJavaBean);
			File file = new File(Autoreply.appDirectory + "config.json");
			FileWriter fw = new FileWriter(file);
			fw.write(jsonString);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
