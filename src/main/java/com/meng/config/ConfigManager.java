package com.meng.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import com.google.gson.Gson;
import com.meng.Autoreply;
import com.meng.Methods;
import com.meng.config.javabeans.ConfigJavaBean;
import com.meng.config.javabeans.GroupConfig;
import com.meng.config.javabeans.PersonInfo;

public class ConfigManager {
	public ConfigJavaBean configJavaBean = new ConfigJavaBean();
	public Gson gson = new Gson();

	public ConfigManager() {
		try {
			File jsonBaseConfigFile = new File(Autoreply.appDirectory + "configV2.json");
			if (!jsonBaseConfigFile.exists()) {
				configJavaBean.groupConfigs.add(new GroupConfig());
				saveConfig();
			}
			configJavaBean = gson.fromJson(Methods.readFileToString(Autoreply.appDirectory + "configV2.json"),
					ConfigJavaBean.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		new SocketConfigManager(this).start();
		new SocketDicManager(this).start();
	}

	public GroupConfig getGroupConfig(long fromGroup) {
		for (GroupConfig gc : configJavaBean.groupConfigs) {
			if (fromGroup == gc.groupNumber) {
				return gc;
			}
		}
		return null;
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
			FileOutputStream fos = null;
			OutputStreamWriter writer = null;
			File file = new File(Autoreply.appDirectory + "configV2.json");
			fos = new FileOutputStream(file);
			writer = new OutputStreamWriter(fos, "utf-8");
			writer.write(gson.toJson(configJavaBean));
			writer.flush();
			if (fos != null) {
				fos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getNameFromQQ(long qq) {
		for (PersonInfo p : configJavaBean.personInfo) {
			if (p.qq == qq) {
				return p.name;
			}
		}
		return null;
	}
}
