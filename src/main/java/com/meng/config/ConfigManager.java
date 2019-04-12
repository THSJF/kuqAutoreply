package com.meng.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.meng.Autoreply;
import com.meng.Methods;
import com.meng.config.javabeans.BilibiliUser;
import com.meng.config.javabeans.ConfigJavaBean;
import com.meng.config.javabeans.GroupRepeater;
import com.meng.config.javabeans.GroupReply;

public class ConfigManager {
	public ConfigJavaBean configJavaBean = new ConfigJavaBean();
	public String jsonBaseConfig = "";
	public Gson gson = new Gson();
	public SocketConfigManager socketConfigManager;
	public SocketDicManager socketDicManager;
	public boolean allowEdit = false;

	public ConfigManager() {
		try {
			File jsonBaseConfigFile = new File(Autoreply.appDirectory + "config.json");
			if (!jsonBaseConfigFile.exists()) {
				jsonBaseConfig = gson.toJson(configJavaBean);
				saveConfig();
			}
			jsonBaseConfig = Methods.readFileToString(Autoreply.appDirectory + "config.json");
			configJavaBean = gson.fromJson(jsonBaseConfig, ConfigJavaBean.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		socketConfigManager = new SocketConfigManager(this);
		socketConfigManager.start();
		socketDicManager = new SocketDicManager(this);
		socketDicManager.start();
	}

	public void checkSetConfig(long fromGroup, String msg) {

		if (msg.startsWith(".blockuser")) {
			long qqId = Autoreply.instence.CC.getAt(msg.replace(".blockuser", ""));
			addQQNotReply(qqId);
			saveConfig();
			Autoreply.sendMessage(fromGroup, 0, "已将" + qqId + "加入屏蔽列表");
		}
		if (msg.startsWith(".unblockuser")) {
			long qqId = Autoreply.instence.CC.getAt(msg.replace(".unblockuser", ""));
			for (int i = 0; i < configJavaBean.QQNotReply.size(); i++) {
				if (configJavaBean.QQNotReply.get(i) == qqId) {
					configJavaBean.QQNotReply.remove(i);
					break;
				}
			}
			saveConfig();
			Autoreply.sendMessage(fromGroup, 0, "已将" + qqId + "解除屏蔽");
		}

	}

	public ArrayList<GroupReply> getMapGroupReply() {
		return configJavaBean.groupReply;
	}

	public ArrayList<Long> getMapQQNotReply() {
		return configJavaBean.QQNotReply;
	}

	public ArrayList<String> getMapWordNotReply() {
		return configJavaBean.wordNotReply;
	}

	public ArrayList<GroupRepeater> getMapGroupRepeater() {
		return configJavaBean.groupRepeater;
	}

	public ArrayList<Long> getMapGroupDicReply() {
		return configJavaBean.groupDicReply;
	}

	public ArrayList<BilibiliUser> getPersonInfo() {
		return configJavaBean.personInfo;
	}

	public void addGroupReply(Long groupNumber) {
		for (GroupReply groupReply : configJavaBean.groupReply) {
			if (groupReply.groupNum == groupNumber) {
				return;
			}
		}
		GroupReply groupReply = new GroupReply();
		configJavaBean.groupReply.add(groupReply);
	}

	public void addQQNotReply(Long QQnumber) {
		for (long iterable_element : configJavaBean.QQNotReply) {
			if (iterable_element == QQnumber) {
				return;
			}
		}
		configJavaBean.QQNotReply.add(QQnumber);
	}

	public void addWordNotReply(String content) {
		for (String iterable_element : configJavaBean.wordNotReply) {
			if (iterable_element.equals(content)) {
				return;
			}
		}
		configJavaBean.wordNotReply.add(content);
	}

	public void saveConfig() {
		try {
			jsonBaseConfig = gson.toJson(configJavaBean);
			FileOutputStream fos = null;
			OutputStreamWriter writer = null;
			File file = new File(Autoreply.appDirectory + "config.json");
			fos = new FileOutputStream(file);
			writer = new OutputStreamWriter(fos, "utf-8");
			writer.write(jsonBaseConfig);
			writer.flush();
			if (fos != null) {
				fos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
