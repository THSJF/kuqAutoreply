package com.meng;

import com.google.gson.*;
import com.meng.bilibili.main.*;
import com.meng.config.javabeans.*;
import com.meng.tools.*;
import java.util.*;

public class OggInterface {

    public boolean processOgg(final long fromQQ, String msg) {
        if (msg.endsWith("喵")) {
            msg = msg.substring(0, msg.length() - 1);
        }
        if (msg.startsWith("findInAll:")) {
            final String finalMsg = msg;
            Autoreply.instence.threadPool.execute(new Runnable() {
					@Override
					public void run() {
						Tools.CQ.findQQInAllGroup(0, fromQQ, finalMsg);
					}
				});
            return true;
        }
        if (msg.startsWith("ban")) {
            String[] arr = msg.split("\\.");
            if (arr.length == 4) {
                Autoreply.instence.banner.checkBan(0, fromQQ, msg);
            }
            return true;
        }
        if (msg.startsWith("av更新时间:")) {
            sendPrivateMessage(fromQQ, String.valueOf(Autoreply.instence.updateManager.getAVLastUpdateTime(msg.substring(7))));
            return true;
        }
        if (msg.startsWith("avJson:")) {
            sendPrivateMessage(fromQQ, Autoreply.instence.updateManager.getAVJson(msg.substring(7)));
            return true;
        }
        if (msg.startsWith("cv更新时间:")) {
            sendPrivateMessage(fromQQ, String.valueOf(Autoreply.instence.updateManager.getCVLastUpdateTime(msg.substring(7))));
            return true;
        }
        if (msg.startsWith("cvJson:")) {
            sendPrivateMessage(fromQQ, Autoreply.instence.updateManager.getCVJson(msg.substring(7)));
            return true;
        }
        if (msg.startsWith("直播状态lid:")) {
            String html = Tools.Network.getSourceCode("https://live.bilibili.com/" + msg.substring(8));
            String jsonInHtml = html.substring(html.indexOf("{\"roomInitRes\":"), html.lastIndexOf("}") + 1);
            JsonObject data = new JsonParser().parse(jsonInHtml).getAsJsonObject().get("baseInfoRes").getAsJsonObject().get("data").getAsJsonObject();
            sendPrivateMessage(fromQQ, data.get("live_status").getAsInt() == 1 ? "true" : "false");
            return true;
        }
        if (Autoreply.instence.biliLinkInfo.checkOgg(0, fromQQ, msg)) {
            return true;
        }
        if (msg.startsWith("直播状态bid:")) {
            SpaceToLiveJavaBean sjb = Autoreply.gson.fromJson(Tools.Network.getSourceCode("https://api.live.bilibili.com/room/v1/Room/getRoomInfoOld?mid=" + msg.substring(8)), SpaceToLiveJavaBean.class);
            sendPrivateMessage(fromQQ, sjb.data.liveStatus == 1 ? "true" : "false");
            return true;
        }
        if (msg.startsWith("获取直播间:")) {
            sendPrivateMessage(fromQQ, Tools.Network.getSourceCode("https://api.live.bilibili.com/room/v1/Room/getRoomInfoOld?mid=" + msg.substring(6)));
            return true;
        }
        if (msg.startsWith("add{")) {
            PersonInfo personInfo;
            try {
                personInfo = Autoreply.gson.fromJson(msg.substring(3), PersonInfo.class);
            } catch (Exception e) {
                sendPrivateMessage(fromQQ, e.toString());
                return true;
            }
            if (personInfo != null) {
                Autoreply.instence.configManager.configJavaBean.personInfo.add(personInfo);
                Autoreply.instence.configManager.saveConfig();
                sendPrivateMessage(fromQQ, msg + "成功");
            } else {
                sendPrivateMessage(fromQQ, "一个玄学问题导致了失败");
            }
            return true;
        }
        if (msg.startsWith("del{")) {
            PersonInfo p;
            try {
                p = Autoreply.gson.fromJson(msg.substring(3), PersonInfo.class);
            } catch (Exception e) {
                sendPrivateMessage(fromQQ, e.toString());
                return true;
            }
            if (p != null) {
                Autoreply.instence.configManager.configJavaBean.personInfo.remove(p);
                Autoreply.instence.configManager.saveConfig();
                sendPrivateMessage(fromQQ, msg + "成功");
            } else {
                sendPrivateMessage(fromQQ, "一个玄学问题导致了失败");
            }
            return true;
        }
        if (msg.startsWith("find:")) {
            String name = msg.substring(5);
            HashSet<PersonInfo> hashSet = new HashSet<>();
            for (PersonInfo personInfo : Autoreply.instence.configManager.configJavaBean.personInfo) {
                if (personInfo.name.contains(name)) {
                    hashSet.add(personInfo);
                }
                if (personInfo.qq != 0 && String.valueOf(personInfo.qq).contains(name)) {
                    hashSet.add(personInfo);
                }
                if (personInfo.bid != 0 && String.valueOf(personInfo.bid).contains(name)) {
                    hashSet.add(personInfo);
                }
                if (personInfo.bliveRoom != 0 && String.valueOf(personInfo.bliveRoom).contains(name)) {
                    hashSet.add(personInfo);
                }
            }
            sendPrivateMessage(fromQQ, Autoreply.gson.toJson(hashSet));
            return true;
        }
        return false;
    }

    private void sendPrivateMessage(long qq, String msg) {
        Autoreply.sendMessage(0, qq, msg);
    }
}
