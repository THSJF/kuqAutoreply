package com.meng.tools;

import com.meng.*;

public class BanListener {

    public BanListener() {

    }

    public void check(long fromGroup, long fromQQ, String msg) {
        if (fromQQ != 1000000) {
            return;
        }
        switch (msg) {
            case "管理员已禁止群内匿名聊天":
                onAnonymousForbid(fromGroup);
                break;
            case "管理员已允许群内匿名聊天":
                onAnonymousAllow(fromGroup);
                break;
            case "管理员开启了全员禁言，只有群主和管理员才能发言":
                onAllBan(fromGroup);
                break;
            case "管理员关闭了全员禁言":
                onAllRelease(fromGroup);
                break;
            default:
                String keyWord = "被管理员禁言";
                if (msg.contains(keyWord)) {
                    long qq = Long.parseLong(msg.substring(msg.lastIndexOf("(") + 1, msg.lastIndexOf(")")));
                    Autoreply.instence.useCount.incBanCount(qq);
                    Autoreply.instence.groupCount.incBanCount(fromGroup);
                    String timeStr = msg.substring(msg.indexOf(keyWord) + keyWord.length());
                    if (timeStr.equals("1月")) {
                        Autoreply.sendMessage(Autoreply.mainGroup, 0, "在群" + fromGroup + "中" + qq + "被禁言一个月", true);
                        return;
                    }
                    String[] time = timeStr.split("(天|小时)");
                    int minute = 0;
                    switch (time.length) {
                        case 1:
                            if (timeStr.contains("天")) {
                                minute = Integer.parseInt(time[0]) * 1440;
                            } else if (timeStr.contains("小时")) {
                                minute = Integer.parseInt(time[0]) * 60;
                            } else if (timeStr.contains("分钟")) {
                                minute = Integer.parseInt(time[0].replace("分钟", ""));
                            }
                            break;
                        case 2:
                            if (timeStr.contains("天") && timeStr.contains("小时")) {
                                minute = Integer.parseInt(time[0]) * 1440 + Integer.parseInt(time[1]) * 60;
                            } else if (timeStr.contains("天") && timeStr.contains("分钟")) {
                                minute = Integer.parseInt(time[0]) * 1440 + Integer.parseInt(time[1].replace("分钟", ""));
                            } else if (timeStr.contains("小时") && timeStr.contains("分钟")) {
                                minute = Integer.parseInt(time[0]) * 60 + Integer.parseInt(time[1].replace("分钟", ""));
                            }
                            break;
                        case 3:
                            minute = Integer.parseInt(time[0]) * 1440 + Integer.parseInt(time[1]) * 60 + Integer.parseInt(time[2].replace("分钟", ""));
                            break;
                    }
                    onBan(fromGroup, qq, minute);
                } else if (msg.contains("被管理员解除禁言")) {
                    long qq = Long.parseLong(msg.substring(msg.lastIndexOf("(") + 1, msg.lastIndexOf(")")));
                    onRelease(fromGroup, qq);
                }
        }
    }

    private void onAnonymousAllow(long fromGroup) {
        Autoreply.sendMessage(Autoreply.mainGroup, 0, fromGroup + "允许匿名");
    }

    private void onAnonymousForbid(long fromGroup) {
        Autoreply.sendMessage(Autoreply.mainGroup, 0, fromGroup + "禁止匿名");
    }

    private void onAllBan(long fromGroup) {
        Autoreply.sendMessage(Autoreply.mainGroup, 0, fromGroup + "全员禁言");
    }

    private void onAllRelease(long fromGroup) {
        Autoreply.sendMessage(Autoreply.mainGroup, 0, fromGroup + "解除全员禁言");
    }

    private void onBan(long fromGroup, long banQQ, int minute) {
        if (banQQ == Autoreply.CQ.getLoginQQ()) {
            Autoreply.instence.configManager.getGroupConfig(fromGroup).reply = false;
            Autoreply.sendMessage(Autoreply.mainGroup, 0, "在群" + fromGroup + "被禁言,关闭了回复群");
            Autoreply.instence.configManager.saveConfig();
        } else {
			//      Autoreply.sendMessage(Autoreply.mainGroup, 0, "在群" + fromGroup + "中" + banQQ + "被禁言" + minute + "min");
        }
    }

    private void onRelease(long fromGroup, long banQQ) {
        if (Autoreply.instence.configManager.isNotReplyGroup(fromGroup)) {
            return;
        }
		//    Autoreply.sendMessage(Autoreply.mainGroup, 0, "在群" + fromGroup + "中" + banQQ + "无罪释放");
		//     Autoreply.sendMessage(fromGroup, banQQ, "恭喜出狱");
    } 
}
