package com.meng.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meng.Autoreply;
import com.meng.tools.Methods;
import com.meng.config.javabeans.ConfigJavaBean;
import com.meng.config.javabeans.GroupConfig;
import com.meng.config.javabeans.PersonInfo;
import com.meng.config.javabeans.PortConfig;
import com.sobte.cqp.jcq.entity.Group;

public class ConfigManager {
    public ConfigJavaBean configJavaBean = new ConfigJavaBean();
    public Gson gson = new Gson();
    public PortConfig portConfig = new PortConfig();

    public ConfigManager() {
        portConfig = gson.fromJson(Methods.readFileToString(Autoreply.appDirectory + "grzxEditConfig.json"), PortConfig.class);
        File jsonBaseConfigFile = new File(Autoreply.appDirectory + "configV3.json");
        if (!jsonBaseConfigFile.exists()) {
            saveConfig();
        }
        Type type = new TypeToken<ConfigJavaBean>() {
        }.getType();
        configJavaBean = gson.fromJson(Methods.readFileToString(Autoreply.appDirectory + "configV3.json"), type);
        Autoreply.instence.threadPool.execute(new SocketConfigManager(this));
        Autoreply.instence.threadPool.execute(new SocketDicManager(this));
    }

    public boolean isMaster(long fromQQ) {
        return configJavaBean.masterList.contains(fromQQ);
    }

    public boolean isAdmin(long fromQQ) {
        return configJavaBean.adminList.contains(fromQQ) || configJavaBean.masterList.contains(fromQQ);
    }

    public boolean isGroupAutoAllow(long fromQQ) {
        return configJavaBean.groupAutoAllowList.contains(fromQQ) || configJavaBean.adminList.contains(fromQQ) || configJavaBean.masterList.contains(fromQQ);
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
        return groupConfig == null || !groupConfig.reply;
    }

    public boolean isNotReplyQQ(long qq) {
        return configJavaBean.QQNotReply.contains(qq) || configJavaBean.blackListQQ.contains(qq);
    }

    public boolean isBlackQQ(long qq) {
        return configJavaBean.blackListQQ.contains(qq);
    }

    public boolean isBlackGroup(long qq) {
        return configJavaBean.blackListGroup.contains(qq);
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

    public PersonInfo getPersonInfoFromBid(long bid) {
        for (PersonInfo pi : configJavaBean.personInfo) {
            if (pi.bid == bid) {
                return pi;
            }
        }
        return null;
    }
	
	public PersonInfo getPersonInfoFromLiveId(long lid) {
        for (PersonInfo pi : configJavaBean.personInfo) {
            if (pi.bliveRoom == lid) {
                return pi;
			  }
		  }
        return null;
	  }

    public void addBlack(long group, final long qq) {
        configJavaBean.blackListQQ.add(qq);
        configJavaBean.blackListGroup.add(group);
        for (GroupConfig groupConfig : configJavaBean.groupConfigs) {
            if (groupConfig.groupNumber == group) {
                configJavaBean.groupConfigs.remove(groupConfig);
                break;
            }
        }
        saveConfig();
        Autoreply.instence.threadPool.execute(new Runnable() {
            @Override
            public void run() {
            //    HashSet<Group> groups = Methods.findQQInAllGroup(qq);
             //   for (Group g : groups) {
                    // if (Methods.ban(g.getId(), qq, 300)) {
                    //    sendMessage(g.getId(), 0, "不要问为什么你会进黑名单，你干了什么自己知道");
                    //   }
            //    }
            }
        });
        Autoreply.sendMessage(1023432971, 0, "已将用户" + qq + "加入黑名单");
        Autoreply.sendMessage(1023432971, 0, "已将群" + group + "加入黑名单");
    }
	
	public void setOgg(long qqNum){
	  configJavaBean.ogg=qqNum;
	  saveConfig();
	}

    public void saveConfig() {
        try {
            File file = new File(Autoreply.appDirectory + "configV3.json");
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            writer.write(gson.toJson(configJavaBean));
            writer.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
