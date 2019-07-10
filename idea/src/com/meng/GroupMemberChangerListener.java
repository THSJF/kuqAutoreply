package com.meng;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meng.config.javabeans.GroupConfig;
import com.meng.config.javabeans.PersonInfo;
import com.meng.tools.Methods;
import com.sobte.cqp.jcq.entity.QQInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;

import static com.meng.Autoreply.sendMessage;
import static com.meng.Autoreply.sendToMaster;

public class GroupMemberChangerListener {

    private HashSet<Long> hashSetGroup = new HashSet<>();
    private HashSet<Long> hashSetQQ = new HashSet<>();

    private String configPathGroup = Autoreply.appDirectory + "configV3_black_group.json";
    private String configPathQQ = Autoreply.appDirectory + "configV3_black_qq.json";

    public GroupMemberChangerListener() {
        File jsonBaseConfigFile = new File(configPathGroup);
        if (!jsonBaseConfigFile.exists()) {
            saveConfig();
        }
        Type type = new TypeToken<HashSet<Long>>() {
        }.getType();
        try {
            hashSetGroup = new Gson().fromJson(Methods.readFileToString(configPathGroup), type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File jsonBaseConfigFile2 = new File(configPathQQ);
        if (!jsonBaseConfigFile2.exists()) {
            saveConfig();
        }
        Type type2 = new TypeToken<HashSet<Long>>() {
        }.getType();
        try {
            hashSetQQ = new Gson().fromJson(Methods.readFileToString(configPathQQ), type2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkIncrease(int subtype, int sendTime, long fromGroup, long fromQQ, long beingOperateQQ) {
        if (qqInBlack(beingOperateQQ)) {
            return;
        }
        PersonInfo personInfo = Autoreply.instence.configManager.getPersonInfoFromQQ(beingOperateQQ);
        if (personInfo != null) {
            sendMessage(fromGroup, 0, "欢迎" + personInfo.name);
        } else {
            sendMessage(fromGroup, 0, "欢迎新大佬");
        }
        Autoreply.instence.banListener.checkSleep(fromGroup, beingOperateQQ);
        if (fromGroup == 859561731L) { // 台长群
            sendMessage(859561731L, 0, "芳赛服务器炸了");
            /*
             * try { sendMessage(859561731L, 0, CC.image(new File(appDirectory +
             * "pic/sjf9961.jpg"))); } catch (IOException e) {
             * e.printStackTrace(); }
             */
        }
    }

    public void checkDecrease(int subtype, int sendTime, long fromGroup, long fromQQ, long beingOperateQQ) {
        if (subtype == 1) {
            QQInfo qInfo = Autoreply.CQ.getStrangerInfo(beingOperateQQ);
            PersonInfo personInfo = Autoreply.instence.configManager.getPersonInfoFromQQ(beingOperateQQ);
            sendMessage(fromGroup, 0, (personInfo == null ? qInfo.getNick() : personInfo.name) + "(" + qInfo.getQqId() + ")" + "跑莉");
        } else if (subtype == 2) {
            if (beingOperateQQ == 2856986197L) {
                Autoreply.CQ.setGroupLeave(fromGroup, false);
                addBlack(fromGroup, fromQQ);
                return;
            }
            if (beingOperateQQ == Autoreply.CQ.getLoginQQ()) {
                addBlack(fromGroup, fromQQ);
                return;
            }
            QQInfo qInfo = Autoreply.CQ.getStrangerInfo(beingOperateQQ);
            QQInfo qInfo2 = Autoreply.CQ.getStrangerInfo(fromQQ);
            PersonInfo personInfo = Autoreply.instence.configManager.getPersonInfoFromQQ(beingOperateQQ);
            PersonInfo personInfo2 = Autoreply.instence.configManager.getPersonInfoFromQQ(fromQQ);
            sendMessage(fromGroup, 0, (personInfo == null ? qInfo.getNick() : personInfo.name) + "(" + qInfo.getQqId() + ")" + "被" + (personInfo2 == null ? qInfo2.getNick() : personInfo2.name) + "(" + qInfo2.getQqId() + ")" + "玩完扔莉");
        }
    }

    private void addBlack(long fromGroup, long fromQQ) {
        hashSetGroup.add(fromGroup);
        hashSetQQ.add(fromQQ);
        Autoreply.instence.configManager.configJavaBean.QQNotReply.add(fromQQ);
        for (GroupConfig groupConfig : Autoreply.instence.configManager.configJavaBean.groupConfigs) {
            if (groupConfig.groupNumber == fromGroup) {
                Autoreply.instence.configManager.configJavaBean.groupConfigs.remove(groupConfig);
                break;
            }
        }
        Autoreply.instence.configManager.saveConfig();
        saveConfig();
        sendToMaster("已将用户" + fromQQ + "加入黑名单");
        sendToMaster("已将群" + fromGroup + "加入黑名单");
    }

    public boolean qqInBlack(long qq) {
        return hashSetQQ.contains(qq);
    }

    public boolean groupInBlack(long group) {
        return hashSetGroup.contains(group);
    }

    private void saveConfig() {
        try {
            File file = new File(configPathGroup);
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            writer.write(new Gson().toJson(hashSetGroup));
            writer.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            File file = new File(configPathQQ);
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            writer.write(new Gson().toJson(hashSetQQ));
            writer.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
