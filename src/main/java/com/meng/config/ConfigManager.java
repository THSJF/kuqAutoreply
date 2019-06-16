package com.meng.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;

import com.google.gson.Gson;
import com.meng.Autoreply;
import com.meng.Methods;
import com.meng.config.javabeans.ConfigJavaBean;
import com.meng.config.javabeans.GroupConfig;
import com.meng.config.javabeans.PersonInfo;

public class ConfigManager {
	private ConfigJavaBean configJavaBean = new ConfigJavaBean();
	public Gson gson = new Gson();
	public HashSet<Long> qqAllowPass = new HashSet<>();
	public PortConfig portConfig;

	public ConfigManager() {
		try {
			portConfig = gson.fromJson(Methods.readFileToString(Autoreply.appDirectory + "grzxEditConfig.json"),
					PortConfig.class);
			File jsonBaseConfigFile = new File(Autoreply.appDirectory + "configV2.json");
			if (!jsonBaseConfigFile.exists()) {
				configJavaBean.groupConfigs.add(new GroupConfig());
				saveConfig();
			}
			configJavaBean = gson.fromJson(Methods.readFileToString(Autoreply.appDirectory + "configV2.json"),
					ConfigJavaBean.class);
			ListQQJavaBean listQQJavaBean = gson.fromJson(
					Methods.readFileToString(Autoreply.appDirectory + "configAllowPass.json"), ListQQJavaBean.class);
			for (long l : listQQJavaBean.qqList) {
				qqAllowPass.add(l);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		new SocketConfigManager(this).start();
		new SocketDicManager(this).start();
	}

	public ConfigJavaBean getConfigJavaBean() {
		return configJavaBean;
	}

	public ArrayList<GroupConfig> getGroupConfigList() {
		return configJavaBean.groupConfigs;
	}

	public ArrayList<Long> getQQNotReplyList() {
		return configJavaBean.QQNotReply;
	}

	public ArrayList<String> getWordNotReplyList() {
		return configJavaBean.wordNotReply;
	}

	public ArrayList<PersonInfo> getPersonInfoList() {
		return configJavaBean.personInfo;
	}

	public GroupConfig getGroupConfig(long fromGroup) {
		for (GroupConfig gc : configJavaBean.groupConfigs) {
			if (fromGroup == gc.groupNumber) {
				return gc;
			}
		}
		return null;
	}

	public boolean isNotReplyGroup(long fromGroup) {
		GroupConfig groupConfig = getGroupConfig(fromGroup);
		if (groupConfig == null || !groupConfig.reply) {
			return true;
		}
		return false;
	}

	public boolean isNotReplyQQ(long qq) {
		if (configJavaBean.QQNotReply.contains(qq)) {
			return true;
		}
		return false;
	}

	public boolean isNotReplyWord(String word) {
		for (String nrw : configJavaBean.wordNotReply) {
			if (word.contains(nrw)) {
				return true;
			}
		}
		return false;
	}

	public PersonInfo getPersonInfoFromQQ(long qq) {
		for (PersonInfo pi : configJavaBean.personInfo) {
			if (pi.qq == qq) {
				return pi;
			}
		}
		return null;
	}

	public PersonInfo getPersonInfoFromName(String name) {
		for (PersonInfo pi : configJavaBean.personInfo) {
			if (pi.name.equals(name)) {
				return pi;
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

	public String readFileToString() throws Exception {
		File file = new File(Autoreply.appDirectory + "grzxEditConfig.json");
		if (!file.exists()) {
			System.exit(0);
			file.createNewFile();
		}
		Long filelength = file.length();
		byte[] filecontent = new byte[filelength.intValue()];
		FileInputStream in = new FileInputStream(file);
		in.read(filecontent);
		in.close();
		return new String(filecontent, "UTF-8");
	}

}
