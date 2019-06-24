package com.meng;

import com.meng.config.ConfigManager;

public class Banner {
    private ConfigManager configManager;

    public Banner(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public boolean checkBan(long fromQQ, long fromGroup, String msg) {
        if (configManager.isAdmin(fromQQ)) {
            // 字符串无法转换为数字会NumberFormatException
            try {
                if (msg.equalsIgnoreCase("wholeban")) {
                    Autoreply.CQ.setGroupWholeBan(fromGroup, true);
                    return true;
                }
                if (msg.equalsIgnoreCase("wholerelease")) {
                    Autoreply.CQ.setGroupWholeBan(fromGroup, false);
                    return true;
                }
                String[] strs = msg.split("\\.");
                switch (strs.length) {
                    case 2:
                        if (strs[0].equalsIgnoreCase("wholeban")) {
                            Autoreply.CQ.setGroupWholeBan(Long.parseLong(strs[1]), true);
                        } else if (strs[0].equalsIgnoreCase("wholerelease")) {
                            Autoreply.CQ.setGroupWholeBan(Long.parseLong(strs[1]), false);
                        }
                        return true;
                    case 3:
                        if (strs[0].equals("ban")) {
                            int sleepTime = Integer.parseInt(strs[2]);
                            if (sleepTime > 2592000) {
                                sleepTime = 2592000;
                            }
                            if (configManager.isMaster(fromQQ)) {
                                Autoreply.CQ.setGroupBan(fromGroup, fromQQ, sleepTime);
                            } else {
                                Autoreply.CQ.setGroupBan(fromGroup, Long.parseLong(strs[1]), sleepTime);
                            }
                        }
                        return true;
                    case 4:
                        if (strs[0].equalsIgnoreCase("ban")) {
                            int sleepTime = Integer.parseInt(strs[3]);
                            if (sleepTime > 2592000) {
                                sleepTime = 2592000;
                            }
                            if (configManager.isMaster(fromQQ)) {
                                Autoreply.CQ.setGroupBan(Long.parseLong(strs[1]), fromQQ, sleepTime);
                            } else {
                                Autoreply.CQ.setGroupBan(Long.parseLong(strs[1]), Long.parseLong(strs[2]), sleepTime);
                            }
                        }
                        return true;
                }
            } catch (NumberFormatException e) {
                Autoreply.CQ.setGroupBan(fromGroup, fromQQ, 9961);
                return true;
            }
        } else {
            try {
                String[] strings = msg.split("\\.");
                if (strings.length == 3 && strings[0].equalsIgnoreCase("ban")) {
                    long banedQQ = Long.parseLong(strings[1]);
                    int sleepTime = Integer.parseInt(strings[2]);
                    if (sleepTime > 120) {
                        sleepTime = 120;
                    }
                    if (sleepTime < 0) {
                        sleepTime = 0;
                    }
                    if (configManager.isMaster(banedQQ)) {
                        Autoreply.CQ.setGroupBan(fromGroup, fromQQ, sleepTime);
                        return true;
                    }
                    if (Autoreply.instence.random.nextInt() % 2 == 0) {
                        Autoreply.CQ.setGroupBan(fromGroup, banedQQ, sleepTime);
                    } else {
                        Autoreply.CQ.setGroupBan(fromGroup, fromQQ, sleepTime);
                    }
                    return true;
                }
            } catch (NumberFormatException e) {
                return true;
            }
        }
        try {
            String[] st = msg.split("\\.");
            if (st.length == 3 && st[0].equalsIgnoreCase("sleep")) {
                if (configManager.isAdmin(fromQQ)) {
                    return true;
                }
                int time = 1;
                switch (st[1]) {
                    case "s":
                    case "second":
                    case "sec":
                        time = 1;
                        break;
                    case "min":
                    case "minute":
                        time = 60;
                        break;
                    case "h":
                    case "hour":
                        time = 3600;
                        break;
                    case "d":
                    case "day":
                        time = 86400;
                        break;
                    case "w":
                    case "week":
                        time = 604800;
                        break;
                    case "m":
                    case "month":
                        time = 2592000;
                        break;
                }
                int sleepTime = Integer.parseInt(st[2]) * time;
                if (sleepTime > 2592000) {
                    sleepTime = 2592000;
                }
                if (sleepTime <= 0) {
                    Autoreply.CQ.setGroupBan(fromGroup, fromQQ, 5400);
                } else {
                    Autoreply.CQ.setGroupBan(fromGroup, fromQQ, sleepTime);
                }
            }
        } catch (NumberFormatException e) {
            Autoreply.CQ.setGroupBan(fromGroup, fromQQ, 9961);
            return true;
        }
        return false;
    }
}
