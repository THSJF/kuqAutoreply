package com.meng.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.gson.Gson;
import com.meng.Autoreply;
import com.meng.Methods;
import com.meng.bilibili.BilibiliUserJavaBean;
import com.meng.bilibili.BilibiliUserJavaBean.BilibiliUser;

public class ConfigManager {
	public ConfigJavaBean configJavaBean = new ConfigJavaBean();
	public BilibiliUserJavaBean bilibiliUserJavaBean = new BilibiliUserJavaBean();
	private String jsonBaseConfig = "";
	private String jsonPersonInfo = "";
	private Gson gson = new Gson();

	public ConfigManager() {
		try {
			File jsonBaseConfigFile = new File(Autoreply.appDirectory + "config.json");
			if (!jsonBaseConfigFile.exists()) {
				jsonBaseConfig = gson.toJson(configJavaBean);
				saveConfig();
			}
			jsonBaseConfig = Methods.readFileToString(Autoreply.appDirectory + "config.json");
			configJavaBean = gson.fromJson(jsonBaseConfig, ConfigJavaBean.class);

			File jsonPersonFile = new File(Autoreply.appDirectory + "configPersonInfo.json");
			if (!jsonPersonFile.exists()) {
				jsonPersonInfo = gson.toJson(bilibiliUserJavaBean);
				try {
					File file = new File(Autoreply.appDirectory + "configPersonInfo.json");
					FileWriter fw = new FileWriter(file);
					fw.write(jsonPersonInfo);
					fw.flush();
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				jsonPersonInfo = Methods.readFileToString(Autoreply.appDirectory + "configPersonInfo.json");
				bilibiliUserJavaBean = gson.fromJson(jsonPersonInfo, BilibiliUserJavaBean.class);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void checkSetConfig(long fromGroup, String msg) {

		if (msg.startsWith(".addgroupreply.")) {
			addGroupReply(Long.parseLong(msg.replace(".addgroupreply.", "")));
			saveConfig();
			Autoreply.sendMessage(fromGroup, 0, "已启用" + msg.replace(".addgroupreply.", "") + "群回复");
		}
		if (msg.startsWith(".removegroupreply.")) {
			long groupID = Long.parseLong(msg.replace(".removegroupreply.", ""));
			for (int i = 0; i < configJavaBean.mapGroupReply.size(); i++) {
				if (configJavaBean.mapGroupReply.get(i) == groupID) {
					configJavaBean.mapGroupReply.remove(i);
					break;
				}
			}
			saveConfig();
			Autoreply.sendMessage(fromGroup, 0, "已停用" + groupID + "群回复");
		}

		if (msg.startsWith(".blockuser")) {
			long qqId = Autoreply.CC.getAt(msg.replace(".blockuser", ""));
			addQQNotReply(qqId);
			saveConfig();
			Autoreply.sendMessage(fromGroup, 0, "已将" + qqId + "加入屏蔽列表");
		}
		if (msg.startsWith(".unblockuser")) {
			long qqId = Autoreply.CC.getAt(msg.replace(".unblockuser", ""));
			for (int i = 0; i < configJavaBean.mapQQNotReply.size(); i++) {
				if (configJavaBean.mapQQNotReply.get(i) == qqId) {
					configJavaBean.mapQQNotReply.remove(i);
					break;
				}
			}
			saveConfig();
			Autoreply.sendMessage(fromGroup, 0, "已将" + qqId + "解除屏蔽");
		}

		if (msg.startsWith(".blockword.")) {
			addWordNotReply(msg.replace(".blockword.", ""));
			saveConfig();
			Autoreply.sendMessage(fromGroup, 0, "已将" + msg.replace(".blockword.", "") + "加入屏蔽列表");
		}
		if (msg.startsWith(".unblockword.")) {
			String word = msg.replace(".unblockword.", "");
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

	public ArrayList<Long> getMapGroupReply() {
		return configJavaBean.mapGroupReply;
	}

	public ArrayList<Long> getMapQQNotReply() {
		return configJavaBean.mapQQNotReply;
	}

	public ArrayList<String> getMapWordNotReply() {
		return configJavaBean.mapWordNotReply;
	}

	public ArrayList<String> getMapGroupRepeater() {
		return configJavaBean.mapGroupRepeater;
	}

	public ArrayList<Long> getMapGroupDicReply() {
		return configJavaBean.mapGroupDicReply;
	}

	public ArrayList<BilibiliUser> getMapBiliUp() {
		return bilibiliUserJavaBean.mapBiliUser;
	}

	public void addGroupReply(Long groupNumber) {
		for (long l : configJavaBean.mapGroupReply) {
			if (l == groupNumber) {
				return;
			}
		}
		configJavaBean.mapGroupReply.add(groupNumber);
	}

	public void addQQNotReply(Long QQnumber) {
		for (long iterable_element : configJavaBean.mapQQNotReply) {
			if (iterable_element == QQnumber) {
				return;
			}
		}
		configJavaBean.mapQQNotReply.add(QQnumber);
	}

	public void addWordNotReply(String content) {
		for (String iterable_element : configJavaBean.mapWordNotReply) {
			if (iterable_element.equals(content)) {
				return;
			}
		}
		configJavaBean.mapWordNotReply.add(content);
	}

	public void addGroupRepeater(String QQnumber) {
		for (String iterable_element : configJavaBean.mapGroupRepeater) {
			if (iterable_element.equals(QQnumber)) {
				return;
			}
		}
		configJavaBean.mapGroupRepeater.add(QQnumber);
	}

	public void saveConfig() {
		try {
			jsonBaseConfig = gson.toJson(configJavaBean);
			File file = new File(Autoreply.appDirectory + "config.json");
			FileWriter fw = new FileWriter(file);
			fw.write(jsonBaseConfig);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
