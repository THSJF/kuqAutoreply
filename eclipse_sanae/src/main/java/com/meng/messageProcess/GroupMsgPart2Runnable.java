package com.meng.messageProcess;

import com.meng.*;
import com.meng.bilibili.live.*;
import com.meng.config.javabeans.*;
import com.meng.tools.*;
import java.io.*;
import java.util.*;

import static com.meng.Autoreply.sendMessage;

public class GroupMsgPart2Runnable implements Runnable {
    private int subType = 0;
    private int msgId = 0;
    private long fromGroup = 0;
    private long fromQQ = 0;
    private String fromAnonymous = "";
    private String msg = "";
    private int font = 0;
    private long timeStamp = 0;

    public GroupMsgPart2Runnable(MessageSender ms) {
        font = ms.font;
        fromGroup = ms.fromGroup;
        fromQQ = ms.fromQQ;
        msg = ms.msg;
        msgId = ms.msgId;
        subType = ms.subType;
        timeStamp = ms.timeStamp;
	}

    @Override
    public synchronized void run() {
        check();
	}

    private boolean check() {
        if (msg.equalsIgnoreCase("loaddic")) {
            Autoreply.instence.addGroupDic();
            sendMessage(fromGroup, fromQQ, "loaded");
            return true;
		}
		if (msg.startsWith(".nn ")) {
			if (msg.contains("~") || msg.contains("～")) {
				return true;
			}
			String name=msg.substring(4);
			if (name.length() > 30) {
				Autoreply.sendMessage(fromGroup, 0, "太长了,记不住");
				return true;
			}
			Autoreply.instence.configManager.setNickName(fromQQ, name);
			Autoreply.sendMessage(fromGroup, 0, "我以后会称呼你为" + name);
			return true;
		}
		if (msg.equals("原曲认知")) {
			File musicFragment=Autoreply.instence.musicManager.createMusicCut(new Random().nextInt(16), 10, fromQQ);
			Autoreply.sendMessage(fromGroup, 0, Autoreply.instence.CC.record(musicFragment.getName()));	
			return true;
		}
		if (msg.startsWith("原曲认知回答 ")) {
			Autoreply.instence.musicManager.judgeAnswer(fromGroup, fromQQ, msg.substring(7));
			return true;
		}
		if (msg.startsWith("原曲认知 ")) {
			switch (msg) {
				case "原曲认知 E":
				case "原曲认知 e":
				case "原曲认知 easy":
				case "原曲认知 Easy":
					Autoreply.sendMessage(fromGroup, 0, Autoreply.instence.CC.record(Autoreply.instence.musicManager.createMusicCut(new Random().nextInt(16), 10, fromQQ).getName()));	
					break;
				case "原曲认知 N":
				case "原曲认知 n":
				case "原曲认知 normal":
				case "原曲认知 Normal":
					Autoreply.sendMessage(fromGroup, 0, Autoreply.instence.CC.record(Autoreply.instence.musicManager.createMusicCut(new Random().nextInt(16), 6, fromQQ).getName()));
					break;
				case "原曲认知 H":
				case "原曲认知 h":
				case "原曲认知 hard":
				case "原曲认知 Hard":
					Autoreply.sendMessage(fromGroup, 0, Autoreply.instence.CC.record(Autoreply.instence.musicManager.createMusicCut(new Random().nextInt(16), 3, fromQQ).getName()));
					break;
				case "原曲认知 L":
				case "原曲认知 l":
				case "原曲认知 lunatic":
				case "原曲认知 Lunatic":
					Autoreply.sendMessage(fromGroup, 0, Autoreply.instence.CC.record(Autoreply.instence.musicManager.createMusicCut(new Random().nextInt(16), 1, fromQQ).getName()));
					break;		
			}
		}
		if (msg.equals(".nn")) {
			Autoreply.instence.configManager.setNickName(fromQQ, null);
			Autoreply.sendMessage(fromGroup, 0, "我以后会用你的QQ昵称称呼你");
			return true;
		}
        if (msg.equals("椰叶查询")) {
            sendMessage(fromGroup, fromQQ, "查询结果：" + Autoreply.instence.CC.at(fromQQ));
            return true;
		}
        if (Methods.checkAt(fromGroup, fromQQ, msg)) {//@
            return true;
		}
        GroupConfig groupConfig = Autoreply.instence.configManager.getGroupConfig(fromGroup);
        if (groupConfig.isRepeat() && Autoreply.instence.repeatManager.check(fromGroup, fromQQ, msg)) {// 复读
            return true;
		}
		if (Autoreply.instence.spellCollect.check(fromGroup, fromQQ, msg)) {
			return true;
		}
        if (msg.startsWith("[CQ:location,lat=")) {
            sendMessage(fromGroup, 0, Autoreply.instence.CC.location(35.594993, 118.869838, 15, "守矢神社", "此生无悔入东方 来世愿生幻想乡"));
            return true;
		}
        if (groupConfig.isCqCode() && Autoreply.instence.CQcodeManager.check(fromGroup, msg)) {// 特殊信息(签到分享等)
            return true;
		}
        if (groupConfig.isCuigeng() && Autoreply.instence.updateManager.check(fromGroup, msg)) {
            return true;
		}
        if (msg.equals("查看统计")) {
            sendMessage(fromGroup, fromQQ, Autoreply.instence.useCount.getMyCount(fromQQ));
            return true;
		}
        if (msg.equals("查看排行")) {
            sendMessage(fromGroup, fromQQ, Autoreply.instence.useCount.getTheFirst());
            return true;
		}
        if (msg.equals("查看群统计")) {
            sendMessage(fromGroup, fromQQ, Autoreply.instence.groupCount.getMyCount(fromGroup));
            return true;
		}
        if (msg.equals("查看群排行")) {
            sendMessage(fromGroup, fromQQ, Autoreply.instence.groupCount.getTheFirst());
            return true;
		}
        if (msg.equals("查看活跃数据")) {
            sendMessage(fromGroup, fromQQ, "https://qqweb.qq.com/m/qun/activedata/active.html?gc=" + fromGroup);
            return true;
		}
		if (Autoreply.instence.diceImitate.check(fromGroup, fromQQ, msg)) {
			return true;
		}
		if (Autoreply.instence.seqManager.check(fromGroup, fromQQ, msg)) {
			return true;
		}
		if (msg.contains("@") && Autoreply.instence.CC.getAt(msg) == -1000) {
            sendMessage(fromGroup, fromQQ, "野蛮假at");
            return true;
		}
        return groupConfig.isDic() && Autoreply.instence.dicReplyManager.check(fromGroup, fromQQ, msg);
	}
}
