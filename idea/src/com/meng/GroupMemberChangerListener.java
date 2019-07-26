package com.meng;

import com.meng.config.javabeans.GroupConfig;
import com.meng.config.javabeans.PersonInfo;
import com.meng.tools.Methods;
import com.sobte.cqp.jcq.entity.Group;
import com.sobte.cqp.jcq.entity.QQInfo;

import java.util.HashSet;

import static com.meng.Autoreply.sendMessage;

public class GroupMemberChangerListener {

    public GroupMemberChangerListener() {
    }

    public void checkIncrease(int subtype, int sendTime, long fromGroup, long fromQQ, long beingOperateQQ) {
        if (Autoreply.instence.configManager.isBlackQQ(fromQQ)) {
            Methods.ban(fromGroup, fromQQ, 300);
        }
        PersonInfo personInfo = Autoreply.instence.configManager.getPersonInfoFromQQ(beingOperateQQ);
        if (personInfo != null && personInfo.name.equals("熊哥")) {
            sendMessage(959615179L, 0, Autoreply.instence.CC.at(-1) + "熊加入了群" + fromGroup, true);
            return;
        }
        if (Autoreply.instence.configManager.isNotReplyGroup(fromGroup)) {
            return;
        }
        if (personInfo != null) {
            sendMessage(fromGroup, 0, "欢迎" + personInfo.name, true);
        } else {
            sendMessage(fromGroup, 0, "欢迎新大佬", true);
        }
        Autoreply.instence.banListener.checkSleepMsg(fromGroup, beingOperateQQ);
        if (fromGroup == 859561731L) { // 台长群
            sendMessage(859561731L, 0, "芳赛服务器炸了", true);
            /*
             * try { sendMessage(859561731L, 0, CC.image(new File(appDirectory +
             * "pic/sjf9961.jpg"))); } catch (IOException e) {
             * e.printStackTrace(); }
             */
        }
    }

    public void checkDecrease(int subtype, int sendTime, long fromGroup, long fromQQ, long beingOperateQQ) {
        if (subtype == 1) {
            if (beingOperateQQ == 2856986197L) {
                Autoreply.CQ.setGroupLeave(fromGroup, false);
                sendMessage(fromGroup, 0, "主人离开了，各位再见", true);
            }
            if (Autoreply.instence.configManager.isNotReplyGroup(fromGroup)) {
                return;
            }
            if (Autoreply.instence.configManager.isBlackQQ(beingOperateQQ)) {
                return;
            }
            QQInfo qInfo = Autoreply.CQ.getStrangerInfo(beingOperateQQ);
            PersonInfo personInfo = Autoreply.instence.configManager.getPersonInfoFromQQ(beingOperateQQ);
            sendMessage(fromGroup, 0, (personInfo == null ? qInfo.getNick() : personInfo.name) + "(" + qInfo.getQqId() + ")" + "跑莉", true);
        } else if (subtype == 2) {
            if (beingOperateQQ == 2856986197L) {
                Autoreply.instence.threadPool.execute(() -> {
                    addBlack(fromGroup, fromQQ);
                    Autoreply.CQ.setGroupLeave(fromGroup, false);
                });
                return;
            }
            if (beingOperateQQ == 2558395159L) {
                Autoreply.CQ.setGroupLeave(fromGroup, false);
                return;
            }
            if (beingOperateQQ == Autoreply.CQ.getLoginQQ()) {
                addBlack(fromGroup, fromQQ);
                return;
            }
            if (Autoreply.instence.configManager.isNotReplyGroup(fromGroup)) {
                return;
            }
            if (Autoreply.instence.configManager.isNotReplyQQ(beingOperateQQ)) {
                return;
            }
            QQInfo qInfo = Autoreply.CQ.getStrangerInfo(beingOperateQQ);
            QQInfo qInfo2 = Autoreply.CQ.getStrangerInfo(fromQQ);
            PersonInfo personInfo = Autoreply.instence.configManager.getPersonInfoFromQQ(beingOperateQQ);
            PersonInfo personInfo2 = Autoreply.instence.configManager.getPersonInfoFromQQ(fromQQ);
            sendMessage(fromGroup, 0, (personInfo == null ? qInfo.getNick() : personInfo.name) + "(" + qInfo.getQqId() + ")" + "被" + (personInfo2 == null ? qInfo2.getNick() : personInfo2.name) + "(" + qInfo2.getQqId() + ")" + "玩完扔莉", true);
        }
    }

    private void addBlack(long group, long qq) {
        Autoreply.instence.configManager.configJavaBean.blackListQQ.add(qq);
        Autoreply.instence.configManager.configJavaBean.blackListGroup.add(group);
        for (GroupConfig groupConfig : Autoreply.instence.configManager.configJavaBean.groupConfigs) {
            if (groupConfig.groupNumber == group) {
                Autoreply.instence.configManager.configJavaBean.groupConfigs.remove(groupConfig);
                break;
            }
        }
        Autoreply.instence.configManager.saveConfig();
        Autoreply.instence.threadPool.execute(() -> {
            HashSet<Group> groups = Methods.findQQInAllGroup(qq);
            for (Group g : groups) {
                if (Methods.ban(g.getId(), qq, 300)) {
                    //    sendMessage(g.getId(), 0, "不要问为什么你会进黑名单，你干了什么自己知道");
                }
            }
        });
        Autoreply.sendMessage(1023432971, 0, "已将用户" + qq + "加入黑名单");
        Autoreply.sendMessage(1023432971, 0, "已将群" + group + "加入黑名单");
    }
}
